package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.MagItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagSlot extends AttachmentSlot {
    public MagSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof MagItem && !this.container.getItem(this.weaponSlot).isEmpty();
    }
}
