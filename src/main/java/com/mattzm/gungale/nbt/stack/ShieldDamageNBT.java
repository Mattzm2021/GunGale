package com.mattzm.gungale.nbt.stack;

import com.mattzm.gungale.item.GearItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShieldDamageNBT {
    public static final String TAG_ID = "shieldDamage";

    public static void add(ItemStack stack, float restore) {
        set(stack, Math.max(0, get(stack) - restore));
    }

    public static void addFull(ItemStack stack) {
        set(stack, 0);
    }

    public static float getResistance(@NotNull ItemStack stack) {
        GearItem item = (GearItem) stack.getItem();
        return item.resistance - get(stack);
    }

    public static float get(@NotNull ItemStack stack) {
        return stack.getOrCreateTag().getFloat(TAG_ID);
    }

    public static void set(@NotNull ItemStack stack, float damage) {
        stack.getOrCreateTag().putFloat(TAG_ID, damage);
    }
}
