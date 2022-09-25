package com.mattzm.gungale.item;

import com.mattzm.gungale.client.nbt.tick.RestoreNBT;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.NBTAction;
import com.mattzm.gungale.message.play.CRestoreMessage;
import com.mattzm.gungale.message.play.MessageHandler;
import com.mattzm.gungale.nbt.stack.ShieldDamageNBT;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RestoreItem extends Item {
    private static final UUID SPEED_UUID = UUID.fromString("23184933-bf3f-4352-ac1e-6f54048babe0");
    public final int timeToUse;
    public final int effect;
    public final Type type;

    public RestoreItem(int timeToUse, int effect, Type type, int stacksTo) {
        super(new Properties().tab(ModItemGroup.TAB_WEAPON).stacksTo(stacksTo));

        this.timeToUse = timeToUse;
        this.effect = effect;
        this.type = type;
    }

    public enum Type {
        HEALTH, SHIELD, MIXED
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(@NotNull PlayerEntity player) {
        resetNBT(player, player.getMainHandItem());
        tickNBT(player);
    }

    @OnlyIn(Dist.CLIENT)
    private static void resetNBT(PlayerEntity player, ItemStack stack) {
        if (RestoreNBT.hasFinishTick(player)) {
            RestoreNBT.reset(player);
            if (stack.getItem() instanceof RestoreItem) {
                RestoreItem item = (RestoreItem) stack.getItem();
                item.executeRestore(player, stack);
                MessageHandler.sendToServer(new CRestoreMessage(NBTAction.ELSE));
            }
        }

        if (RestoreNBT.hasChangeStack(player)) RestoreNBT.reset(player);
    }

    @OnlyIn(Dist.CLIENT)
    private static void tickNBT(@NotNull PlayerEntity player) {
        if (RestoreNBT.hasStart(player)) {
            stopSprinting(player);
            RestoreNBT.tick(player);
        }
    }

    public static void executeSlowDown(@NotNull PlayerEntity player) {
        if (!(player.getMainHandItem().getItem() instanceof RestoreItem)) return;
        RestoreItem item = (RestoreItem) player.getMainHandItem().getItem();
        if (item.effect != -1) getSpeed(player).addTransientModifier(getSpeedModifier(-0.01));
        else if (item.type != Type.MIXED) getSpeed(player).addTransientModifier(getSpeedModifier(-0.025));
        else getSpeed(player).addTransientModifier(getSpeedModifier(-0.04));
    }

    public static void stopSprinting(@NotNull PlayerEntity player) {
        player.setSprinting(false);
    }

    public boolean checkUsing(ItemStack gearStack, PlayerEntity player) {
        if (ModPlayerInventory.get(player).status) return false;
        if (this.type == RestoreItem.Type.SHIELD && gearStack.isEmpty()) return false;
        if (this.type == RestoreItem.Type.SHIELD && ShieldDamageNBT.get(gearStack) == 0) return false;
        if (this.type == RestoreItem.Type.HEALTH && player.getHealth() == player.getMaxHealth()) return false;
        if (this.type == RestoreItem.Type.MIXED && player.getHealth() == player.getMaxHealth()) {
            if (gearStack.isEmpty()) return false;
            return ShieldDamageNBT.get(gearStack) > 0;
        } else return true;
    }

    public static void removeSlowDown(PlayerEntity player) {
        getSpeed(player).removeModifier(SPEED_UUID);
    }

    public void executeRestore(PlayerEntity player, @NotNull ItemStack stack) {
        stack.shrink(1);
        if (this.type == Type.SHIELD || this.type == Type.MIXED) {
            ItemStack gearStack = GearItem.get(player);
            if (gearStack != null) {
                if (this.effect == -1) ShieldDamageNBT.addFull(gearStack);
                else ShieldDamageNBT.add(gearStack, this.effect);
                GearItem.update(player, gearStack);
            }
        }
        if (this.type == Type.HEALTH || this.type == Type.MIXED) {
            if (this.effect == -1) player.setHealth(player.getMaxHealth());
            else player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + this.effect));
        }
    }

    private static ModifiableAttributeInstance getSpeed(@NotNull PlayerEntity player) {
        return Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED));
    }

    private static @NotNull AttributeModifier getSpeedModifier(double amount) {
        return new AttributeModifier(SPEED_UUID, "Speed", amount, AttributeModifier.Operation.ADDITION);
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_, PlayerEntity p_195938_4_) {
        return false;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        RestoreItem item = (RestoreItem) stack.getItem();
        textComponents.add(new TranslationTextComponent("tooltip.gungale.restore." + Objects.requireNonNull(item.getRegistryName()).getPath()));
        textComponents.add(new TranslationTextComponent("tooltip.gungale.restore.time_to_use",
                new StringTextComponent(Float.toString(ModMathHelper.tickToSecond(item.timeToUse))).withStyle(TextFormatting.RED)));
    }
}
