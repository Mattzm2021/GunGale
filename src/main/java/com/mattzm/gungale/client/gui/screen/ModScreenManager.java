package com.mattzm.gungale.client.gui.screen;

import com.mattzm.gungale.client.gui.screen.inventory.WeaponBenchScreen;
import com.mattzm.gungale.inventory.container.ModContainerTypes;
import net.minecraft.client.gui.ScreenManager;

public class ModScreenManager {
    public static void setupScreen() {
        ScreenManager.register(ModContainerTypes.MOD_INVENTORY, ModInventoryScreen::new);
        ScreenManager.register(ModContainerTypes.MOD_CRAFTING, ModCraftingScreen::new);
        ScreenManager.register(ModContainerTypes.WEAPON_BENCH, WeaponBenchScreen::new);
    }
}
