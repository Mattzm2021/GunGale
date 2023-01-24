package com.mattzm.gungale.item.crafting;

import com.mattzm.gungale.GunGale;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class ModRecipeTypes {
    public static final IRecipeType<WeaponRecipe> WEAPON = register("weapon");

    static <T extends IRecipe<?>> @NotNull IRecipeType<T> register(final String name) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(GunGale.MOD_ID, name), new IRecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }
}
