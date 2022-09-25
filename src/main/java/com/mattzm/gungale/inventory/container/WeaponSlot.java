package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WeaponSlot extends Slot {
    public WeaponSlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof AbstractWeaponItem;
    }
}
