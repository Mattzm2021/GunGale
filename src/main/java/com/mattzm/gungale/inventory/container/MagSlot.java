package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.MagItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagSlot extends AttachmentSlot {
    public MagSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot, 0);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (this.container.getItem(this.weaponSlot).isEmpty()) {
            return false;
        } else {
            return stack.getItem() instanceof MagItem && ((AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem()).getMag().get();
        }
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (!this.container.getItem(this.weaponSlot).isEmpty()) {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            if (!item.getMag().get()) {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, AttachmentSlot.INCAPABLE_SLOT);
            }
        }

        return super.getNoItemIcon();
    }
}
