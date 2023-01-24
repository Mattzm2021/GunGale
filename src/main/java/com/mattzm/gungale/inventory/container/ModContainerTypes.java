package com.mattzm.gungale.inventory.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import org.jetbrains.annotations.NotNull;

public class ModContainerTypes {
    public static final ContainerType<ModItemContainer> MOD_INVENTORY = register("mod_inventory", ModItemContainer::new);
    public static final ContainerType<ModWorkbenchContainer> MOD_CRAFTING = register("mod_crafting", ModWorkbenchContainer::new);
    public static final ContainerType<WeaponBenchContainer> WEAPON_BENCH = register("weapon_bench", WeaponBenchContainer::new);

    private static @NotNull <T extends Container> ContainerType<T> register(String location, @NotNull ContainerType.IFactory<T> factory) {
        ContainerType<T> containerType = new ContainerType<>(factory);
        containerType.setRegistryName(location);
        return containerType;
    }
}