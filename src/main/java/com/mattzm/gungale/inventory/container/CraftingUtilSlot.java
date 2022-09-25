package com.mattzm.gungale.inventory.container;

import net.minecraft.inventory.IInventory;

public class CraftingUtilSlot extends UtilSlot {
    public boolean isActive = false;

    public CraftingUtilSlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }
}
