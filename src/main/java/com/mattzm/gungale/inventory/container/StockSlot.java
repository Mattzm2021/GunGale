package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.StockItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StockSlot extends AttachmentSlot {
    public StockSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof StockItem && !this.container.getItem(this.weaponSlot).isEmpty();
    }
}
