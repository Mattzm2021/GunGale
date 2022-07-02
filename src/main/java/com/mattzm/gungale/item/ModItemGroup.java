package com.mattzm.gungale.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

@SuppressWarnings("NullableProblems")
public class ModItemGroup {
    public static final ItemGroup TAB_MOD = new ItemGroup("gungale") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.P9_PISTOL);
        }
    };
}
