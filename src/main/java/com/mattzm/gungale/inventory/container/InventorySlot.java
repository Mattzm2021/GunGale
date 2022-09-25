package com.mattzm.gungale.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class InventorySlot extends Slot {
    public boolean isActive = true;

    public InventorySlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }
}
