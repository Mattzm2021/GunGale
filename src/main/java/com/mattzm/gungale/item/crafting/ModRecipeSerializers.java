package com.mattzm.gungale.item.crafting;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

@VanillaCode("IRecipeSerializer")
@ObjectHolder(GunGale.MOD_ID)
public class ModRecipeSerializers {
    public static final IRecipeSerializer<WeaponRecipe> WEAPON_RECIPE = null;
}
