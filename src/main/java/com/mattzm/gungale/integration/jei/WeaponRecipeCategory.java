package com.mattzm.gungale.integration.jei;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.item.crafting.WeaponRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

public class WeaponRecipeCategory implements IRecipeCategory<WeaponRecipe> {
    protected static final ResourceLocation UID = new ResourceLocation(GunGale.MOD_ID, "weapon");
    private static final ResourceLocation TEXTURE = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/weapon_bench.png");
    private final IDrawable background;
    private final IDrawable icon;

    public WeaponRecipeCategory(@NotNull IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.WEAPON_BENCH));
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }

    @Override
    public @NotNull Class<? extends WeaponRecipe> getRecipeClass() {
        return WeaponRecipe.class;
    }

    @Override
    @Deprecated
    public @NotNull String getTitle() {
        return new TranslationTextComponent("container.weapon_bench").getString();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(@NotNull WeaponRecipe recipe, @NotNull IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayout recipeLayout, @NotNull WeaponRecipe recipe, @NotNull IIngredients ingredients) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                recipeLayout.getItemStacks().init(j + i * 5, true, 11 + j * 18, 16 + i * 18);
            }
        }

        recipeLayout.getItemStacks().init(15, false, 142, 35);
        recipeLayout.getItemStacks().set(ingredients);
    }
}
