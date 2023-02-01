package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.HopUpItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HopUpSlot extends AttachmentSlot {
    public HopUpSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot, 6);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (this.container.getItem(this.weaponSlot).isEmpty()) {
            return false;
        } else {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            return stack.getItem() instanceof HopUpItem && item.getHopUp() == stack.getItem();
        }
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (!this.container.getItem(this.weaponSlot).isEmpty()) {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            if (item.getHopUp() == null) {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, AttachmentSlot.INCAPABLE_SLOT);
            }
        }

        return super.getNoItemIcon();
    }
}
