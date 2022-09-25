package com.mattzm.gungale.nbt.stack;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PrecisionNBT {
    public static final String TAG_ID = "precision";

    public static void add(ItemStack stack, int add) {
        set(stack, get(stack) + add);
    }

    public static int get(@NotNull ItemStack stack) {
        if (!stack.getOrCreateTag().contains(TAG_ID)) {
            stack.getOrCreateTag().putInt(TAG_ID, ((AbstractWeaponItem) stack.getItem()).precision);
        }

        return stack.getOrCreateTag().getInt(TAG_ID);
    }

    public static void set(@NotNull ItemStack stack, int precision) {
        stack.getOrCreateTag().putInt(TAG_ID, precision);
    }
}
