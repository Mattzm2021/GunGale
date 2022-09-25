package com.mattzm.gungale.item;

import com.mattzm.gungale.nbt.stack.ShieldDamageNBT;
import com.mattzm.gungale.nbt.player.ShieldNBT;
import com.mattzm.gungale.nbt.stack.ShieldEvolveNBT;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class GearItem extends ArmorItem {
    public final int resistance;
    public final int damageToEvolve;

    public GearItem(ArmorMaterial material, int resistance, int damageToEvolve) {
        super(material, EquipmentSlotType.CHEST, new Properties().stacksTo(1).tab(ModItemGroup.TAB_WEAPON));

        this.resistance = resistance;
        this.damageToEvolve = damageToEvolve;
    }

    public static void onServerTick(@NotNull PlayerEntity player) {
        if (!ShieldNBT.get(player) && hasGear(player)) {
            player.setAbsorptionAmount(ShieldDamageNBT.getResistance(get(player)));
        }

        ShieldNBT.set(player, hasGear(player));
        if (ShieldNBT.get(player)) {
            GearItem item = (GearItem) get(player).getItem();
            ShieldDamageNBT.set(get(player), item.resistance - player.getAbsorptionAmount());
        } else resetAbsorption(player);
    }

    private static void resetAbsorption(@NotNull PlayerEntity player) {
        if (player.getAbsorptionAmount() > 0) player.setAbsorptionAmount(0);
    }

    public static boolean hasGear(@NotNull LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof GearItem;
    }

    public static ItemStack get(PlayerEntity player) {
        if (hasGear(player)) return player.getItemBySlot(EquipmentSlotType.CHEST);
        else return ItemStack.EMPTY;
    }

    public static void update(@NotNull PlayerEntity player, ItemStack stack) {
        player.setAbsorptionAmount(ShieldDamageNBT.getResistance(stack));
    }

    public static void checkIfUpdateEvolve(PlayerEntity player, float damage) {
        if (hasGear(player)) {
            ItemStack stack = get(player);
            GearItem item = (GearItem) stack.getItem();
            if (item.isEvolvable()) {
                float evolve = Math.min(damage, ShieldEvolveNBT.get(stack));
                float remain = damage - evolve;
                if (remain > 0) {
                    ShieldEvolveNBT.addFull(stack);
                    item.executeEvolve(player, remain);
                } else {
                    ShieldEvolveNBT.add(stack, evolve);
                }
            }
        }
    }

    private void executeEvolve(@NotNull PlayerEntity player, float remain) {
        ItemStack stack = new ItemStack(this.getNext());
        player.setItemSlot(EquipmentSlotType.CHEST, stack);
        player.setAbsorptionAmount(player.getAbsorptionAmount() + 5.0f);
        if (this.getNext().isEvolvable()) {
            ShieldEvolveNBT.add(stack, remain);
        }
    }

    public boolean isEvolvable() {
        return this != ModItems.EVO_SHIELD_4;
    }

    private GearItem getNext() {
        if (this == ModItems.EVO_SHIELD_1) {
            return (GearItem) ModItems.EVO_SHIELD_2;
        } else if (this == ModItems.EVO_SHIELD_2) {
            return (GearItem) ModItems.EVO_SHIELD_3;
        } else {
            return (GearItem) ModItems.EVO_SHIELD_4;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_, PlayerEntity p_195938_4_) {
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        GearItem item = (GearItem) stack.getItem();
        if (isEvolvable()) {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.evo_shield.evolvable",
                    new StringTextComponent(Double.toString(ModMathHelper.twoDigitsDouble(item.resistance / 2.0))).withStyle(TextFormatting.RED)));
        } else {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.evo_shield.evolved"));
        }

        textComponents.add(new TranslationTextComponent("tooltip.gungale.evo_shield.resistance",
                new StringTextComponent(Double.toString(ModMathHelper.twoDigitsDouble(ShieldDamageNBT.getResistance(stack) / 2))).withStyle(TextFormatting.RED)));
        if (isEvolvable()) {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.evo_shield.damage_to_evolve",
                    new StringTextComponent(Double.toString(ModMathHelper.twoDigitsDouble(ShieldEvolveNBT.get(stack) / 2))).withStyle(TextFormatting.RED)));
        } else {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.evo_shield.max_evolve").withStyle(TextFormatting.RED));
        }
    }
}
