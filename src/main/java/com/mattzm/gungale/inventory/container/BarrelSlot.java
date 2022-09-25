package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.BarrelItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BarrelSlot extends AttachmentSlot {
    public BarrelSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (this.container.getItem(this.weaponSlot).isEmpty()) {
            return false;
        } else {
            return stack.getItem() instanceof BarrelItem && ((AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem()).getBarrel().get();
        }
    }
}
