package com.mattzm.gungale.item;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.message.*;
import com.mattzm.gungale.nbt.BulletNBT;
import com.mattzm.gungale.nbt.CoolDownNBT;
import com.mattzm.gungale.nbt.RecoilNBT;
import com.mattzm.gungale.nbt.ReloadNBT;
import com.mattzm.gungale.property.BasicProperty;
import com.mattzm.gungale.property.DamageProperty;
import com.mattzm.gungale.property.RecoilProperty;
import com.mattzm.gungale.util.ModDamageSource;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

public abstract class GunItem extends Item {
    public final float damage;
    public final int range;
    public final int firingRate;
    public final int capacity;
    public final int reloadSpeed;
    public final float verticalRecoil;
    public final float horizontalRecoil;
    public final float verticalRecover;
    public final float horizontalRecover;
    public final int recoverSpeed;
    public final DamageProperty damageProperty;

    protected GunItem(BasicProperty basicProperty, DamageProperty damageProperty, RecoilProperty recoilProperty) {
        super(new Item.Properties().tab(ModItemGroup.TAB_MOD).stacksTo(1));

        this.damage = basicProperty.damage;
        this.range = basicProperty.range;
        this.firingRate = basicProperty.firingRate;
        this.capacity = basicProperty.capacity;
        this.reloadSpeed = basicProperty.reloadSpeed;
        this.verticalRecoil = recoilProperty.verticalAngle();
        this.horizontalRecoil = recoilProperty.horizontalAngle();
        this.verticalRecover = recoilProperty.verticalRecover();
        this.horizontalRecover = recoilProperty.horizontalRecover();
        this.recoverSpeed = recoilProperty.recoverSpeed;
        this.damageProperty = damageProperty;
    }

    public void consumeFire(World world, PlayerEntity player, ItemStack stack) {
        if (!ReloadNBT.hasStart(stack) && !CoolDownNBT.hasStart(stack) && BulletNBT.hasAny(stack)) {
            FireTarget target = this.getNearestTarget(world, player);
            if (target == FireTarget.BLOCK)
                MessageHandler.channel.sendToServer(new BlockHitMessage.ToServer(target.position.x, target.position.y, target.position.z));
            else if (target == FireTarget.ENTITY)
                MessageHandler.channel.sendToServer(new EntityHitMessage.ToServer(target.entity.getId(), target.distanceSqr));
            this.reactRecoil(player);
            MessageHandler.channel.sendToServer(new FireMessage.ToServer());
        }
    }

    public void consumeReload(PlayerEntity player, int bullet, ItemStack itemStack) {
        if (bullet > 0) {
            for (int i = 0; i < player.inventory.items.size(); i++) {
                ItemStack stack = player.inventory.getItem(i);
                if (this.isCorrectBullet(stack.getItem())) {
                    if (stack.getCount() >= bullet) {
                        stack.shrink(bullet);
                        BulletNBT.add(itemStack, bullet);
                        return;
                    }
                    bullet -= stack.getCount();
                    BulletNBT.add(itemStack, stack.getCount());
                    player.inventory.removeItem(stack);
                }
            }
        }
    }

    public void onServerTick(PlayerEntity player, ItemStack stack) {
        if (CoolDownNBT.hasStart(stack)) onCoolDownTick(stack);
        if (RecoilNBT.hasStart(stack)) this.onRecoilTick(player, stack);
        if (stack == player.getMainHandItem()) {
            if (this.getAllBullet(player) > 0) {
                if (!BulletNBT.hasAny(stack) && !ReloadNBT.hasStart(stack)) ReloadNBT.start(stack);
                if (ReloadNBT.hasStart(stack)) this.onReloadTick(player, stack);
            }
        } else {
            if (ReloadNBT.hasStart(stack)) ReloadNBT.reset(stack);
        }
    }

    public static void addParticle(World world, double x, double y, double z) {
        world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    public static void hurt(LivingEntity entity, PlayerEntity player, GunItem item, double distanceSqr) {
        if (!entity.isDeadOrDying()) {
            if (entity.isSleeping()) entity.stopSleeping();
            entity.setNoActionTime(0);
            entity.animationSpeed = 1.5f;
            entity.hurtDuration = 10;
            entity.hurtTime = entity.hurtDuration;
            entity.hurtDir = 0.0f;
            entity.setHealth(entity.getHealth() - item.damageProperty.getRealDamage(item.damage, distanceSqr, entity));
            if (entity.isDeadOrDying()) {
                entity.playSound(SoundEvents.GENERIC_DEATH, 1.0f, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
                entity.die(ModDamageSource.gunShot(player));
            } else entity.playSound(SoundEvents.GENERIC_HURT, 1.0f, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
        }
    }

    public int getAllBullet(PlayerEntity player) {
        int count = 0;

        for (ItemStack stack : player.inventory.items)
            if (this.isCorrectBullet(stack.getItem())) count += stack.getCount();

        return count;
    }

    public void consumeRecover(PlayerEntity player) {
        GunGale.LOGGER.debug("Count!");
        player.xRot += this.verticalRecover;
        player.yRot -= this.horizontalRecover;
    }

    public static void updateGun(ItemStack stack) {
        BulletNBT.add(stack, -1);
        CoolDownNBT.start(stack);
    }

    public static void renderBullet(MainWindow window, FontRenderer renderer, PlayerEntity player, MatrixStack stack, String text) {
        float posX = ((float) window.getGuiScaledWidth() - (float) renderer.width(text)) / 2.0F;
        float posY = (float) window.getGuiScaledHeight() - (player.isCreative() ? 35.0F : 48.0F);

        renderer.draw(stack, text, posX + 1.0F, posY, 0);
        renderer.draw(stack, text, posX - 1.0F, posY, 0);
        renderer.draw(stack, text, posX, posY + 1.0F, 0);
        renderer.draw(stack, text, posX, posY - 1.0F, 0);
        renderer.draw(stack, text, posX, posY, 8453920);
    }

    public static void renderReloadTick(MainWindow window, FontRenderer renderer, PlayerEntity player, MatrixStack stack, String text) {
        float posX = ((float) window.getGuiScaledWidth() - (float) renderer.width(text)) / 2.0F + 50.0F;
        float posY = (float) window.getGuiScaledHeight() - (player.isCreative() ? 35.0F : 48.0F);

        renderer.draw(stack, text, posX + 1.0F, posY, 0);
        renderer.draw(stack, text, posX - 1.0F, posY, 0);
        renderer.draw(stack, text, posX, posY + 1.0F, 0);
        renderer.draw(stack, text, posX, posY - 1.0F, 0);
        renderer.draw(stack, text, posX, posY, 8453920);
    }

    protected abstract boolean isCorrectBullet(Item item);

    private FireTarget getNearestTarget(World world, PlayerEntity player) {
        Vector3d eyePosition = player.getEyePosition(1.0f);
        Vector3d viewVector = player.getViewVector(1.0F);
        Vector3d position = eyePosition.add(viewVector.x * this.range, viewVector.y * this.range, viewVector.z * this.range);
        BlockRayTraceResult blockResult = world.clip(new RayTraceContext(eyePosition, position, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));

        double blockDis = blockResult.getLocation().distanceToSqr(eyePosition);
        AxisAlignedBB rangeBB = player.getBoundingBox().expandTowards(viewVector.scale(this.range)).inflate(1.0d, 1.0d, 1.0d);
        EntityRayTraceResult entityResult = ProjectileHelper.getEntityHitResult(player, eyePosition, position, rangeBB, (p_215312_0_) -> !p_215312_0_.isSpectator(), blockDis);

        if (entityResult != null) {
            double entityDis = eyePosition.distanceToSqr(entityResult.getLocation());
            if (entityDis < blockDis && entityResult.getEntity() instanceof LivingEntity)
                return FireTarget.ENTITY.setEntity((LivingEntity) entityResult.getEntity()).setDistanceSqr(entityDis);
        }

        return FireTarget.BLOCK.setPosition(blockResult.getLocation());
    }

    private static void onCoolDownTick(ItemStack stack) {
        if (CoolDownNBT.hasFinish(stack)) CoolDownNBT.reset(stack);
        else CoolDownNBT.tick(stack);
    }

    private void onRecoilTick(PlayerEntity player, ItemStack stack) {
        if (RecoilNBT.hasFinish(stack)) RecoilNBT.reset(stack);
        else RecoilNBT.tick(stack);
        MessageHandler.channel.send(PacketDistributor.DIMENSION.with(player.level::dimension), new FireMessage.ToClient(player.getId()));
    }

    private void onReloadTick(PlayerEntity player, ItemStack stack) {
        if (ReloadNBT.hasFinish(stack)) {
            ReloadNBT.reset(stack);
            this.consumeReload(player, BulletNBT.getSpace(stack), stack);
        } else ReloadNBT.tick(stack);
    }

    private void reactRecoil(PlayerEntity player) {
        player.xRot -= this.verticalRecoil;
        player.yRot += this.horizontalRecoil;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_, PlayerEntity p_195938_4_) {
        return false;
    }
}
