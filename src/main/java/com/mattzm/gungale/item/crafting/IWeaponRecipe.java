package com.mattzm.gungale.item.crafting;

import com.mattzm.gungale.GunGale;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IWeaponRecipe extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(GunGale.MOD_ID, "weapon");

    @Override
    default @NotNull IRecipeType<?> getType() {
        Optional<IRecipeType<?>> optional = Registry.RECIPE_TYPE.getOptional(TYPE_ID);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NullPointerException("IWeaponRecipe cannot find a RecipeType");
        }
    }
}
