package com.mattzm.gungale.client.gui;

import com.mattzm.gungale.client.gui.screen.inventory.ModCraftingScreen;
import com.mattzm.gungale.client.gui.screen.inventory.ModInventoryScreen;
import com.mattzm.gungale.client.gui.screen.inventory.WeaponBenchScreen;
import com.mattzm.gungale.inventory.container.ModContainerTypes;
import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@VanillaCode("ScreenManager")
@OnlyIn(Dist.CLIENT)
public class ModScreenManager {
    public static void registerScreens() {
        ScreenManager.register(ModContainerTypes.MOD_INVENTORY, ModInventoryScreen::new);
        ScreenManager.register(ModContainerTypes.MOD_CRAFTING, ModCraftingScreen::new);
        ScreenManager.register(ModContainerTypes.WEAPON_BENCH, WeaponBenchScreen::new);
    }
}
