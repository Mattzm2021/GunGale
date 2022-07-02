package com.mattzm.gungale.nbt;

import com.mattzm.gungale.item.GunItem;
import net.minecraft.item.ItemStack;

public class RecoilNBT {
    public static final String TAG_ID = "recoil";

    public static void tick(ItemStack stack) {
        set(stack, get(stack) + 1);
    }

    public static void start(ItemStack stack) {
        set(stack, 1);
    }

    public static void reset(ItemStack stack) {
        set(stack, 0);
    }

    public static boolean hasStart(ItemStack stack) {
        return get(stack) > 0;
    }

    public static boolean hasFinish(ItemStack stack) {
        GunItem item = (GunItem) stack.getItem();
        return get(stack) == item.recoverSpeed;
    }

    public static int get(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TAG_ID);
    }

    private static void set(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(TAG_ID, value);
    }
}
