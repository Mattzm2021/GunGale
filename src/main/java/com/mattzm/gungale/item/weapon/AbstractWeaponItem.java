package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.client.object.ClientObjectHolder;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.entity.projectile.ModProjectileHelper;
import com.mattzm.gungale.entity.util.EntityHelper;
import com.mattzm.gungale.item.*;
import com.mattzm.gungale.message.*;
import com.mattzm.gungale.message.play.*;
import com.mattzm.gungale.client.nbt.ADSNBT;
import com.mattzm.gungale.client.nbt.FireNBT;
import com.mattzm.gungale.client.nbt.tick.CoolDownNBT;
import com.mattzm.gungale.client.nbt.tick.RecoilNBT;
import com.mattzm.gungale.client.nbt.tick.ReloadNBT;
import com.mattzm.gungale.nbt.stack.BulletNBT;
import com.mattzm.gungale.nbt.stack.MagSizeNBT;
import com.mattzm.gungale.nbt.stack.OpticNBT;
import com.mattzm.gungale.nbt.stack.PrecisionNBT;
import com.mattzm.gungale.property.*;
import com.mattzm.gungale.util.ModDamageSource;
import com.mattzm.gungale.util.inventory.StaticInventory;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractWeaponItem extends Item implements IAttachable, IReloadable {
    private static final UUID SPEED_UUID = UUID.fromString("f3286092-bd91-4661-8860-c405d7f39cca");
    private final float bodyDamage;
    private final float headDamage;
    private final float legsDamage;
    private final int rateOfFire;
    protected final int precision;
    private final int magazineSize;
    private final float effectiveRange;
    protected final int verticalRecoil;
    protected final int horizontalRecoil;
    private final int hipFireAccuracy;
    private final int fullReloadSpeed;
    private final int tacReloadSpeed;
    private final int mobility;
    private final ADSProperty adsProperty;
    private final MagProperty magProperty;
    private final RecoilProperty recoilProperty;

    protected AbstractWeaponItem(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(new Item.Properties().tab(ModItemGroup.TAB_WEAPON).stacksTo(1));
        this.bodyDamage = basicProperty.bodyDamage;
        this.headDamage = damageProperty.getHeadDamage();
        this.legsDamage = damageProperty.getLegsDamage();
        this.rateOfFire = basicProperty.rateOfFire;
        this.precision = basicProperty.precision;
        this.magazineSize = basicProperty.magazineSize;
        this.effectiveRange = basicProperty.effectiveRange;
        this.verticalRecoil = recoilProperty.verticalRecoil;
        this.horizontalRecoil = recoilProperty.horizontalRecoil;
        this.hipFireAccuracy = recoilProperty.hipFireAccuracy;
        this.fullReloadSpeed = reloadProperty.fullReloadSpeed;
        this.tacReloadSpeed = reloadProperty.tacReloadSpeed;
        this.magProperty = magProperty;
        this.adsProperty = adsProperty;
        this.recoilProperty = recoilProperty;
        this.mobility = mobility;
    }

    @OnlyIn(Dist.CLIENT)
    public void executeFire(World world, PlayerEntity player) {
        FireTarget target = this.getNearestTarget(world, player);
        if (target == FireTarget.BLOCK) {
            MessageHandler.sendToServer(new CBlockHitMessage(target.position));
        } else if (target == FireTarget.ENTITY) {
            float damage = this.getDamage(target, player, ModPlayerInventory.get(player).getSelected());
            if (damage < 0.0f) return;
            MessageHandler.sendToServer(new CEntityHitMessage(target.entity.getId(), damage));
            if (!target.entity.isDeadOrDying()) {
                ClientObjectHolder.getInstance().getMIngameGui().setMainDamageText(ModProjectileHelper.getTextByDamage(damage, target.entity, target.damageType));
                ClientObjectHolder.getInstance().getMIngameGui().setMainDamagePos(player.xRot, player.yRot);
                if (target.entity instanceof PlayerEntity) {
                    if (!((PlayerEntity) target.entity).isCreative()) {
                        playHitSound(player);
                    }
                } else {
                    playHitSound(player);
                }
            }
        }
    }

    protected float getDamage(@NotNull FireTarget target, PlayerEntity player, ItemStack stack) {
        return target.damageType.getDamage(this) * target.damageAmplifier;
    }

    public void executeReload(@NotNull PlayerEntity player, ItemStack itemStack) {
        int bullet = BulletNBT.getSpace(itemStack);
        if (bullet > 0) {
            for (int i = 12; i < ModPlayerInventory.get(player).getContainerSize(); i++) {
                ItemStack stack = ModPlayerInventory.get(player).getItem(i);
                if (this.isCorrectBullet(stack.getItem())) {
                    if (stack.getCount() >= bullet) {
                        stack.shrink(bullet);
                        BulletNBT.add(itemStack, bullet);

                        return;
                    }

                    bullet -= stack.getCount();
                    ModPlayerInventory.get(player).removeItemNoUpdate(i);
                    BulletNBT.add(itemStack, stack.getCount());
                }
            }
        }
    }

    public static void checkIfCanReload(@NotNull PlayerEntity player) {
        ItemStack stack = ModPlayerInventory.get(player).getSelected();
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (player.isCreative() || player.isSpectator()) return;
        if (BulletNBT.hasSpace(stack) && !ReloadNBT.hasStart(player) && item.getAllBullet(player) > 0) {
            ReloadNBT.startWith(player, BulletNBT.isEmpty(stack) ? item.fullReloadSpeed : item.tacReloadSpeed);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void checkIfOnADS(PlayerEntity player) {
        if (Minecraft.getInstance().player != player) return;
        double targetFov = ADSNBT.onADS(player) ? OpticItem.MAGNIFICATION_FOV[OpticNBT.get(ModPlayerInventory.get(player).getSelected())] : 70.0;
        double speed = (targetFov - ADSNBT.getFov(player)) / ADSNBT.getSpeed(player);
        if (Minecraft.getInstance().options.fov != targetFov) {
            if (speed > 0 && Minecraft.getInstance().options.fov + speed > targetFov) {
                Minecraft.getInstance().options.fov = targetFov;
            } else if (speed < 0 && Minecraft.getInstance().options.fov + speed < targetFov) {
                Minecraft.getInstance().options.fov = targetFov;
            } else {
                Minecraft.getInstance().options.fov += speed;
            }
        }

        if (ADSNBT.onADS(player)) {
            EntityHelper.stopSprinting(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(PlayerEntity player) {
        checkIfOnADS(player);
        if (ModPlayerInventory.get(player).getSelected().getItem() instanceof AbstractWeaponItem) {
            AbstractWeaponItem item = (AbstractWeaponItem) ModPlayerInventory.get(player).getSelected().getItem();
            item.checkIfCanFire(player, ModPlayerInventory.get(player).getSelected());
        }

        resetNBT(player, ModPlayerInventory.get(player).getSelected());
        tickNBT(player);
    }

    public static void onServerTick(@NotNull PlayerEntity player) {
        if (!player.level.isClientSide) {
            if (ModPlayerInventory.get(player).getItem(0).getItem() instanceof AbstractWeaponItem) {
                AbstractWeaponItem item = (AbstractWeaponItem) ModPlayerInventory.get(player).getItem(0).getItem();
                item.checkAndUpdateBullet(0, ModPlayerInventory.get(player).getItem(0), player);
                item.checkAndUpdatePrecision(0, ModPlayerInventory.get(player).getItem(0), player);
                item.checkAndUpdateOptic(0, ModPlayerInventory.get(player).getItem(0), player);
            }

            if (ModPlayerInventory.get(player).getItem(6).getItem() instanceof AbstractWeaponItem) {
                AbstractWeaponItem item = (AbstractWeaponItem) ModPlayerInventory.get(player).getItem(6).getItem();
                item.checkAndUpdateBullet(6, ModPlayerInventory.get(player).getItem(6), player);
                item.checkAndUpdatePrecision(6, ModPlayerInventory.get(player).getItem(6), player);
                item.checkAndUpdateOptic(6, ModPlayerInventory.get(player).getItem(6), player);
            }
        }
    }

    private void checkAndUpdateBullet(int index, ItemStack stack, @NotNull PlayerEntity player) {
        if (!player.level.isClientSide) {
            int bullet = this.magazineSize;
            if (!ModPlayerInventory.get(player).getItem(index + 2).isEmpty()) {
                bullet = ((MagItem) ModPlayerInventory.get(player).getItem(index + 2).getItem()).getMagazineSize(stack);
            }

            if (BulletNBT.get(stack) > bullet) {
                int count = BulletNBT.get(stack) - bullet;
                BulletNBT.set(stack, bullet);
                ModPlayerInventory.get(player).add(new ItemStack(this.getBullet(), count));
            }
        }
    }

    private void checkAndUpdatePrecision(int index, ItemStack stack, @NotNull PlayerEntity player) {
        if (!player.level.isClientSide) {
            int precision = this.precision;
            if (!ModPlayerInventory.get(player).getItem(index + 1).isEmpty()) {
                precision += ((BarrelItem) ModPlayerInventory.get(player).getItem(index + 1).getItem()).getPrecisionIncrease(stack);
            }

            if (!ModPlayerInventory.get(player).getItem(index + 4).isEmpty()) {
                precision += ((StockItem) ModPlayerInventory.get(player).getItem(index + 4).getItem()).getPrecisionIncrease(stack);
            }

            PrecisionNBT.set(stack, precision);
        }
    }

    private void checkAndUpdateOptic(int index, ItemStack stack, @NotNull PlayerEntity player) {
        if (!player.level.isClientSide) {
            ItemStack stack1 = ModPlayerInventory.get(player).getItem(index + 3);
            if (stack1.isEmpty()) {
                OpticNBT.set(stack, 0);
            } else if (stack1.getItem() instanceof VariableOpticItem) {
                if (OpticNBT.get(stack) != ((VariableOpticItem) stack1.getItem()).getSmallerInt() && OpticNBT.get(stack) != ((VariableOpticItem) stack1.getItem()).getLargerInt()) {
                    OpticNBT.set(stack, ((OpticItem) stack1.getItem()).getMagnification());
                }
            } else if (OpticNBT.get(stack) != ((OpticItem) stack1.getItem()).getMagnification()) {
                OpticNBT.set(stack, ((OpticItem) stack1.getItem()).getMagnification());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void checkIfCanFire(PlayerEntity player, ItemStack stack) {
        if (this.canFire(player, stack)) {
            this.executeFire(player.level, player);
            this.reactRecoil(player, stack);
            this.updateGun(player, stack);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void resetNBT(PlayerEntity player, ItemStack stack) {
        if (CoolDownNBT.hasFinishTick(player)) CoolDownNBT.reset(player);
        if (RecoilNBT.hasFinishTick(player)) RecoilNBT.reset(player);
        boolean flag = true;
        if (ReloadNBT.hasFinishTick(player)) {
            ReloadNBT.reset(player);
            flag = false;
            if (stack.getItem() instanceof AbstractWeaponItem) {
                int bullet = BulletNBT.getSpace(stack);
                MessageHandler.sendToServer(new CExecuteReloadMessage(bullet));
            }
        }

        if (ReloadNBT.hasChangeStack(player)) ReloadNBT.reset(player);
        if (stack.getItem() instanceof AbstractWeaponItem) {
            AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
            if (BulletNBT.isEmpty(stack) && !ReloadNBT.hasStart(player) && item.getAllBullet(player) > 0) {
                if (!player.isCreative() && flag) {
                    ReloadNBT.startWith(player, item.fullReloadSpeed);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void tickNBT(PlayerEntity player) {
        if (CoolDownNBT.hasStart(player)) CoolDownNBT.tick(player);
        if (ReloadNBT.hasStart(player)) ReloadNBT.tick(player);
        if (RecoilNBT.hasStart(player)) {
            if (RecoilNBT.getTick(player) > 1) {
                recoverRecoil(player);
            }

            RecoilNBT.tick(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addParticle(@NotNull World world, double x, double y, double z) {
        world.addAlwaysVisibleParticle(ParticleTypes.FLAME, true, x, y, z, 0.0, 0.0, 0.0);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean haveFireAbility(PlayerEntity player, ItemStack stack) {
        return !ReloadNBT.hasStart(player) && !BulletNBT.isEmpty(stack) || player.isCreative();
    }

    public void executeSlowDown(@NotNull PlayerEntity player) {
        EntityHelper.getSpeedAttributeInstance(player).addTransientModifier(getSpeedModifier(ModMathHelper.twoDigitsDouble(this.mobility / 1000.0 - 0.1)));
    }

    public void removeSlowDown(PlayerEntity player) {
        EntityHelper.getSpeedAttributeInstance(player).removeModifier(SPEED_UUID);
    }

    private static @NotNull AttributeModifier getSpeedModifier(double amount) {
        return new AttributeModifier(SPEED_UUID, "Speed", amount, AttributeModifier.Operation.ADDITION);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canFire(PlayerEntity player, ItemStack stack) {
        return haveFireAbility(player, stack) && !CoolDownNBT.hasStart(player) && FireNBT.onFire(player);
    }

    public void hurt(@NotNull LivingEntity entity, PlayerEntity player, float damage) {
        if (entity.isDeadOrDying() || player.level.isClientSide || damage <= 0) return;
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity) entity).isCreative()) return;
            if (!canHarmPlayer(player, (PlayerEntity) entity)) return;
        }

        if (entity.isSleeping()) entity.stopSleeping();
        entity.setNoActionTime(0);
        entity.animationSpeed = 1.5f;
        float realDamage = Math.max(damage - entity.getAbsorptionAmount(), 0.0f);
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - damage + realDamage);
        if (realDamage != 0) {
            entity.getCombatTracker().recordDamage(ModDamageSource.gunshot(player), entity.getHealth(), realDamage);
            entity.setHealth(entity.getHealth() - realDamage);
        }

        entity.hurtDuration = 10;
        entity.hurtTime = entity.hurtDuration;
        entity.hurtDir = 0.0f;
        entity.setLastHurtByPlayer(player);
        entity.level.broadcastEntityEvent(entity, (byte) 2);
        if (entity.isDeadOrDying()) {
            playSound(entity, SoundEvents.GENERIC_DEATH, 1.0f, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
            entity.die(ModDamageSource.gunshot(player));
            if (entity instanceof PlayerEntity) {
                GearItem.checkIfUpdateEvolve(player, 80.0f);
            }
        } else if (entity instanceof PlayerEntity) {
            PlayerEntity player1 = (PlayerEntity) entity;
            if (GearItem.hasGear(player1)) {
                playSound(entity, SoundEvents.GLASS_BREAK, 1.0f, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
            } else {
                playSound(entity, SoundEvents.GENERIC_HURT, 1.0f, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
            }
        } else {
            playSound(entity, SoundEvents.GENERIC_HURT, 1.0f, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
        }

        if (entity instanceof PlayerEntity)
            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayerEntity) entity, ModDamageSource.gunshot(player), damage, damage, false);
        CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayerEntity) player, entity, ModDamageSource.gunshot(player), damage, damage, false);
        GearItem.checkIfUpdateEvolve(player, damage);
        MessageHandler.sendToWorld(new SEntityHitMessage(entity.getId()), player.level);
    }

    public int getAllBullet(@NotNull PlayerEntity player) {
        int count = 0;

        for (int i = 12; i < StaticInventory.size; i++) {
            ItemStack stack = ModPlayerInventory.get(player).getItem(i);
            if (this.isCorrectBullet(stack.getItem())) {
                count += stack.getCount();
            }
        }

        return count;
    }

    private static boolean canHarmPlayer(@NotNull PlayerEntity harmer, @NotNull PlayerEntity receiver) {
        Team team = harmer.getTeam();
        Team team1 = receiver.getTeam();
        if (team == null) {
            return true;
        } else {
            return !team.isAlliedTo(team1) || team.isAllowFriendlyFire();
        }
    }

    private boolean isCorrectBullet(@NotNull Item item) {
        return item == this.getBullet();
    }

    @OnlyIn(Dist.CLIENT)
    private FireTarget getNearestTarget(World world, @NotNull PlayerEntity player) {
        boolean flag = Minecraft.getInstance().options.fov > OpticItem.MAGNIFICATION_FOV[OpticNBT.get(ModPlayerInventory.get(player).getSelected())];
        Vector3d eyePosition = player.getEyePosition(1.0f);
        Vector3d bulletVector = player.getViewVector(1.0f);
        if (flag) bulletVector = this.recoilProperty.getBulletVector(player);
        Vector3d position = eyePosition.add(bulletVector.x * this.effectiveRange, bulletVector.y * this.effectiveRange, bulletVector.z * this.effectiveRange);
        BlockRayTraceResult blockResult = world.clip(new RayTraceContext(eyePosition, position, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));

        double blockDis = blockResult.getLocation().distanceToSqr(eyePosition);
        AxisAlignedBB rangeBB = player.getBoundingBox().expandTowards(bulletVector.scale(this.effectiveRange)).inflate(1.0d, 1.0d, 1.0d);
        EntityRayTraceResult entityResult = ModProjectileHelper.getEntityHitResult(player, eyePosition, position, rangeBB, blockDis);
        return analysis(blockResult, entityResult, eyePosition, blockDis);
    }

    protected FireTarget analysis(BlockRayTraceResult blockResult, EntityRayTraceResult entityResult, Vector3d eyePosition, double blockDis) {
        if (entityResult != null) {
            double entityDis = eyePosition.distanceToSqr(entityResult.getLocation());
            if (entityDis < blockDis && entityResult.getEntity() instanceof LivingEntity) {
                if (entityResult.getEntity() instanceof PlayerEntity) {
                    return FireTarget.ENTITY.setEntity((LivingEntity) entityResult.getEntity()).setDamageType(DamageType.getHitPart(entityResult));
                } else {
                    return FireTarget.ENTITY.setEntity((LivingEntity) entityResult.getEntity()).setDamageType(DamageType.BODY);
                }
            }
        }

        return FireTarget.BLOCK.setPosition(blockResult.getLocation());
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateGun(@NotNull PlayerEntity player, ItemStack stack) {
        int period = this.getShootPeriod();
        CoolDownNBT.startWith(player, period);
        RecoilNBT.startWith(player, period + 1, RecoilProperty.getScreenVRecoil(this.verticalRecoil, this.precision, PrecisionNBT.get(stack)));
        if (player.isCreative()) return;
        BulletNBT.addFromClient(stack, ModPlayerInventory.get(player).selected, -1);
    }

    @OnlyIn(Dist.CLIENT)
    protected void reactRecoil(@NotNull PlayerEntity player, ItemStack stack) {
        player.xRot -= RecoilProperty.getScreenVRecoil(this.verticalRecoil, this.precision, PrecisionNBT.get(stack));
        player.yRot += RecoilProperty.getScreenHRecoil(this.horizontalRecoil, this.precision, PrecisionNBT.get(stack));
    }

    protected static void recoverRecoil(@NotNull PlayerEntity player) {
        player.xRot += RecoilNBT.getRecoil(player) / 10;
    }

    public static void playSound(@NotNull Entity entity, SoundEvent event, float volume, float frequency) {
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), event, entity.getSoundSource(), volume, frequency);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playLocalSound(@NotNull PlayerEntity player, SoundEvent event, float volume, float frequency) {
        player.level.playSound(player, player.getX(), player.getY(), player.getZ(), event, player.getSoundSource(), volume, frequency);
    }

    private int getShootPeriod() {
        float period = ModMathHelper.twoDigitsFloat(1200.0f / this.rateOfFire);
        return ModMathHelper.getCertainTick(period);
    }

    @OnlyIn(Dist.CLIENT)
    private static void playHitSound(PlayerEntity player) {
        playLocalSound(player, SoundEvents.ARROW_HIT_PLAYER, 0.18f, 0.45f);
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_, PlayerEntity p_195938_4_) {
        return false;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        textComponents.add(new TranslationTextComponent("tooltip.gungale." + Objects.requireNonNull(this.getRegistryName()).getPath()));
        textComponents.add(new TranslationTextComponent("tooltip.gungale.property.bullet_type",
                new TranslationTextComponent("item.gungale." + Objects.requireNonNull(this.getBullet().getRegistryName()).getPath())));
        if (!Screen.hasShiftDown()) textComponents.add(new TranslationTextComponent("tooltip.gungale.shift"));
        else {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.body_damage",
                    new StringTextComponent(Float.toString(this.bodyDamage / 2)).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.rate_of_fire",
                    new StringTextComponent(Integer.toString(this.rateOfFire)).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.precision",
                    new StringTextComponent(Integer.toString(PrecisionNBT.get(stack))).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.magazine_size",
                    new StringTextComponent(Integer.toString(BulletNBT.get(stack))).withStyle(TextFormatting.RED),
                    new StringTextComponent(Integer.toString(MagSizeNBT.get(stack))).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.effective_range",
                    new StringTextComponent(Float.toString(this.effectiveRange)).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.recoil",
                    new StringTextComponent(Double.toString(RecoilProperty.getRenderVRecoil(this.verticalRecoil, this.precision, PrecisionNBT.get(stack)))).withStyle(TextFormatting.RED),
                    new StringTextComponent(Double.toString(RecoilProperty.getRenderHRecoil(this.horizontalRecoil, this.precision, PrecisionNBT.get(stack)))).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.hip_fire_accuracy",
                    new StringTextComponent(Integer.toString(this.hipFireAccuracy)).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.reload_speed",
                    new StringTextComponent(Double.toString(this.fullReloadSpeed / 20.0)).withStyle(TextFormatting.RED),
                    new StringTextComponent(Double.toString(this.tacReloadSpeed / 20.0)).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.ads_speed",
                    new StringTextComponent(Double.toString(ModMathHelper.tickToSecond(this.adsProperty.getRenderAdsSpeed()))).withStyle(TextFormatting.RED)));
            textComponents.add(new TranslationTextComponent("tooltip.gungale.property.mobility",
                    new StringTextComponent(Integer.toString(this.mobility)).withStyle(TextFormatting.RED)));
        }
    }

    public float getBodyDamage() {
        return this.bodyDamage;
    }

    public float getHeadDamage() {
        return this.headDamage;
    }

    public float getLegsDamage() {
        return this.legsDamage;
    }

    public int getPrecision() {
        return this.precision;
    }

    public int getMagazineSize() {
        return this.magazineSize;
    }

    public int getCertainAdsSpeed() {
        return this.adsProperty.getCertainSpeed();
    }

    public int getMagazineByLevel(int level) {
        return this.magProperty.getByLevel(level);
    }

    public int getBarrelIncrementByLevel(int level) {
        return this.recoilProperty.getBarrelPrecisionIncrement(level);
    }

    public int getStockIncrementByLevel(int level) {
        return this.recoilProperty.getStockPrecisionIncrement(level);
    }
}
