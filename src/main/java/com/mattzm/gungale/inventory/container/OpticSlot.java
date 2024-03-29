package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.OpticItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpticSlot extends AttachmentSlot {
    public OpticSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot, 4);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (this.container.getItem(this.weaponSlot).isEmpty()) {
            return false;
        } else {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            return stack.getItem() instanceof OpticItem && ((OpticItem) stack.getItem()).canFit(item);
        }
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (!this.container.getItem(this.weaponSlot).isEmpty()) {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            if (item.getOptic() == null) {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, AttachmentSlot.INCAPABLE_SLOT);
            }
        }

        return super.getNoItemIcon();
    }
}
