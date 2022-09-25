package com.mattzm.gungale.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ModItemGroup {
    public static final ItemGroup TAB_WEAPON = new ItemGroup("weapon") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.FLATLINE);
        }
    };

    public static final ItemGroup TAB_UTIL = new ItemGroup("util") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.SALTPETER);
        }
    };
}
