package com.mattzm.gungale.integration.jei;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.item.crafting.WeaponRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@JeiPlugin
public class GunGaleJei implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(GunGale.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WeaponRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
            registration.addRecipes(manager.getAllRecipesFor(IRecipeType.CRAFTING).stream().filter(recipe -> recipe instanceof WeaponRecipe).collect(Collectors.toList()), WeaponRecipeCategory.UID);
        }
    }
}
