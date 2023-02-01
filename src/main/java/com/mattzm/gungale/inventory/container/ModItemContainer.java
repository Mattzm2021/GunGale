package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemContainer extends Container {
    public final ModPlayerInventory inventory;

    public ModItemContainer(int id, @NotNull PlayerInventory inventory) {
        this(id, ModPlayerInventory.get(inventory.player));
    }

    public ModItemContainer(int id, ModPlayerInventory inventory) {
        super(ModContainerTypes.MOD_INVENTORY, id);
        this.inventory = inventory;
        for (int i = 0; i < 2; i++) {
            this.addSlot(new WeaponSlot(this.inventory, i * 6, 11, 11 + i * 38));
            this.addSlot(new BarrelSlot(this.inventory, i * 6 + 1, 69, 21 + i * 38, i * 6));
            this.addSlot(new MagSlot(this.inventory, i * 6 + 2, 89, 21 + i * 38, i * 6));
            this.addSlot(new OpticSlot(this.inventory, i * 6 + 3, 109, 21 + i * 38, i * 6));
            this.addSlot(new StockSlot(this.inventory, i * 6 + 4, 129, 21 + i * 38, i * 6));
            this.addSlot(new HopUpSlot(this.inventory, i * 6 + 5, 149, 21 + i * 38, i * 6));
        }

        int slotAmount = 10;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++, slotAmount--) {
                if (slotAmount > 0) {
                    this.addSlot(new UtilSlot(this.inventory, 12 + i * 4 + j, 53 + j * 18, 88 + i * 18));
                }
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull PlayerEntity player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(stack1, 6, 7, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 6) {
                if (!this.moveItemStackTo(stack1, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 12) {
                if (!this.moveItemStackTo(stack1, 12, 22, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack1, 0, 12, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack1);
        }

        return stack;
    }
}
