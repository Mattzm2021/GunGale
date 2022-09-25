package com.mattzm.gungale.util;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemStackHelper {
    public static boolean sameItem(@NotNull ItemStack stackA, @NotNull ItemStack stackB) {
        return stackA.getItem() == stackB.getItem();
    }

    public static boolean sameHoverName(@NotNull ItemStack stackA, @NotNull ItemStack stackB) {
        return stackA.getHoverName().equals(stackB.getHoverName());
    }

    public static boolean sameHighlightTip(ItemStack stackA, ItemStack stackB) {
        return stackA.getHighlightTip(stackA.getHoverName()).equals(stackB.getHighlightTip(stackB.getHoverName()));
    }
}
