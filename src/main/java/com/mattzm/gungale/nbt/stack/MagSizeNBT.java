package com.mattzm.gungale.nbt.stack;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagSizeNBT {
    public static final String TAG_ID = "magSize";

    public static int get(@NotNull ItemStack stack) {
        if (!stack.getOrCreateTag().contains(TAG_ID)) {
            stack.getOrCreateTag().putInt(TAG_ID, ((AbstractWeaponItem) stack.getItem()).magazineSize);
        }

        return stack.getOrCreateTag().getInt(TAG_ID);
    }

    public static void set(@NotNull ItemStack stack, int magSize) {
        stack.getOrCreateTag().putInt(TAG_ID, magSize);
    }
}
