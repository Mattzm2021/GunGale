package com.mattzm.gungale.nbt.stack;

import com.mattzm.gungale.item.GearItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShieldEvolveNBT {
    public static final String TAG_ID = "shieldEvolve";

    public static void add(ItemStack stack, float evolve) {
        set(stack, Math.max(0, get(stack) - evolve));
    }

    public static void addFull(ItemStack stack) {
        set(stack, 0);
    }

    public static float getEvolveAmount(@NotNull ItemStack stack) {
        GearItem item = (GearItem) stack.getItem();
        return item.damageToEvolve - get(stack);
    }

    public static float get(@NotNull ItemStack stack) {
        if (!stack.getOrCreateTag().contains(TAG_ID)) {
            stack.getOrCreateTag().putFloat(TAG_ID, ((GearItem) stack.getItem()).damageToEvolve);
        }

        return stack.getOrCreateTag().getFloat(TAG_ID);
    }

    public static void set(@NotNull ItemStack stack, float damage) {
        stack.getOrCreateTag().putFloat(TAG_ID, damage);
    }
}
