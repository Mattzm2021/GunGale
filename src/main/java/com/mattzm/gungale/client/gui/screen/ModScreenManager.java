package com.mattzm.gungale.client.gui.screen;

import com.mattzm.gungale.inventory.container.ModContainerType;
import net.minecraft.client.gui.ScreenManager;

public class ModScreenManager {
    public static void setupScreen() {
        ScreenManager.register(ModContainerType.MOD_INVENTORY, ModInventoryScreen::new);
        ScreenManager.register(ModContainerType.MOD_CRAFTING, ModCraftingScreen::new);
        ScreenManager.register(ModContainerType.WEAPON_BENCH, WeaponBenchScreen::new);
    }
}
