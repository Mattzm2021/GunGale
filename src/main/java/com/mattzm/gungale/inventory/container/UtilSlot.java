package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.AmmoItem;
import com.mattzm.gungale.item.AttachmentItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UtilSlot extends Slot {
    public UtilSlot(IInventory inventory, int index, int posX, int posY) {
        super(inventory, index, posX, posY);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof AmmoItem || stack.getItem() instanceof AttachmentItem;
    }
}
