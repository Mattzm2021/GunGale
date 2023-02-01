package com.mattzm.gungale.integration.jei;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.client.gui.screen.inventory.ModCraftingScreen;
import com.mattzm.gungale.client.gui.screen.inventory.WeaponBenchScreen;
import com.mattzm.gungale.inventory.container.ModWorkbenchContainer;
import com.mattzm.gungale.inventory.container.WeaponBenchContainer;
import com.mattzm.gungale.item.crafting.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import mezz.jei.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unused")
@JeiPlugin
public class GunGaleJei implements IModPlugin {
    @Nullable private WeaponRecipeCategory weaponRecipeCategory;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(GunGale.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(
                weaponRecipeCategory = new WeaponRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        ErrorUtil.checkNotNull(weaponRecipeCategory, "weaponRecipeCategory");
        ClientWorld world = Minecraft.getInstance().level;
        ErrorUtil.checkNotNull(world, "minecraft world");
        RecipeManager manager = world.getRecipeManager();
        registration.addRecipes(new ArrayList<>(manager.getAllRecipesFor(ModRecipeTypes.WEAPON)), WeaponRecipeCategory.UID);
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(WeaponBenchScreen.class, 106, 32, 28, 23, WeaponRecipeCategory.UID);
        registration.addRecipeClickArea(ModCraftingScreen.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(WeaponBenchContainer.class, WeaponRecipeCategory.UID, 1, 15, 16, 36);
        registration.addRecipeTransferHandler(ModWorkbenchContainer.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 46);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WEAPON_BENCH), WeaponRecipeCategory.UID);
    }
}
