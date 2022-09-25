package com.mattzm.gungale.item.crafting;

import com.mattzm.gungale.GunGale;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModRecipes {
    public static IRecipeSerializer<WeaponRecipe> WEAPON_RECIPE = register("crafting_weapon", new WeaponRecipe.Serializer());

    static <S extends IRecipeSerializer<T>, T extends IRecipe<?>> @NotNull S register(String location, @NotNull S recipe) {
        recipe.setRegistryName(new ResourceLocation(GunGale.MOD_ID, location));
        return recipe;
    }
}
