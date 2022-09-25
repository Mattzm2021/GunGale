package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.OpticItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OpticSlot extends AttachmentSlot {
    public OpticSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof OpticItem && !this.container.getItem(this.weaponSlot).isEmpty();
    }
}
