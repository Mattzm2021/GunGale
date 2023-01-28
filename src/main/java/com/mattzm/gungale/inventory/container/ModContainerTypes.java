package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@VanillaCode("ContainerType")
@ObjectHolder(GunGale.MOD_ID)
public class ModContainerTypes {
    public static final ContainerType<ModItemContainer> MOD_INVENTORY = null;
    public static final ContainerType<ModWorkbenchContainer> MOD_CRAFTING = null;
    public static final ContainerType<WeaponBenchContainer> WEAPON_BENCH = null;
}
