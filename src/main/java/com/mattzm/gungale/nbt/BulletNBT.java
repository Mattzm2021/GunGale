package com.mattzm.gungale.nbt;

import com.mattzm.gungale.item.GunItem;
import net.minecraft.item.ItemStack;

public class BulletNBT {
    public static final String TAG_ID = "bullet";

    public static void add(ItemStack stack, int value) {
        set(stack, get(stack) + value);
    }

    public static boolean hasAny(ItemStack stack) {
        return get(stack) > 0;
    }

    public static boolean hasSpace(ItemStack stack) {
        return getSpace(stack) > 0;
    }

    public static int getSpace(ItemStack stack) {
        GunItem item = (GunItem) stack.getItem();
        return item.capacity - get(stack);
    }

    public static int get(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TAG_ID);
    }

    private static void set(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(TAG_ID, value);
    }
}
