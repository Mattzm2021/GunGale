package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.AttachmentItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemContainer extends Container {
    public final ModPlayerInventory inventory;

    public ModItemContainer(int id, @NotNull PlayerInventory inventory) {
        this(id, ModPlayerInventory.get(inventory.player));
    }

    public ModItemContainer(int id, ModPlayerInventory inventory) {
        super(ModContainerType.MOD_INVENTORY, id);
        this.inventory = inventory;
        for (int i = 0; i < 2; i++) {
            this.addSlot(new WeaponSlot(this.inventory, i * 6, 11, 11 + i * 38));
            this.addSlot(new MagSlot(this.inventory, i * 6 + 1, 69, 21 + i * 38, i * 6));
            this.addSlot(new BarrelSlot(this.inventory, i * 6 + 2, 89, 21 + i * 38, i * 6));
            this.addSlot(new StockSlot(this.inventory, i * 6 + 3, 109, 21 + i * 38, i * 6));
            this.addSlot(new OpticSlot(this.inventory, i * 6 + 4, 129, 21 + i * 38, i * 6));
            this.addSlot(new AttachmentSlot(this.inventory, i * 6 + 5, 149, 21 + i * 38, i * 6));
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
    public @NotNull ItemStack quickMoveStack(@NotNull PlayerEntity player, int index) {
        ItemStack stack = this.getSlot(index).getItem();
        if (!stack.isEmpty()) {
            if (index == 0) {
                if (this.getSlot(6).getItem().isEmpty()) {
                    this.setItem(6, stack);
                    this.setItem(index, ItemStack.EMPTY);
                }
            } else if (index == 6) {
                if (this.getSlot(0).getItem().isEmpty()) {
                    this.setItem(0, stack);
                    this.setItem(index, ItemStack.EMPTY);
                }
            } else if (index < 12) {
                this.inventory.add(stack);
            } else if (stack.getItem() instanceof AttachmentItem) {
                this.inventory.checkAndSetAttachment(stack);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull PlayerEntity player) {
        return this.inventory.stillValid(player);
    }
}
