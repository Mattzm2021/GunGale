package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.item.MagItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.item.weapon.IMagProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagSlot extends AttachmentSlot {
    public MagSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot) {
        super(inventory, index, posX, posY, weaponSlot, 1);
    }

    @Override
    protected void updateTextureIndex() {
        ItemStack stack = this.container.getItem(this.weaponSlot);
        if (stack.isEmpty()) return;
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (item.getMag() == IMagProvider.Type.HEAVY) {
            this.textureIndex = 1;
        } else if (item.getMag() == IMagProvider.Type.LIGHT) {
            this.textureIndex = 2;
        } else if (item.getMag() == IMagProvider.Type.ENERGY) {
            this.textureIndex = 3;
        }
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (this.container.getItem(this.weaponSlot).isEmpty()) {
            return false;
        } else {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            return stack.getItem() instanceof MagItem && item.getMag() == ((MagItem) stack.getItem()).getType();
        }
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (!this.container.getItem(this.weaponSlot).isEmpty()) {
            AbstractWeaponItem item = (AbstractWeaponItem) this.container.getItem(this.weaponSlot).getItem();
            if (item.getMag() == null) {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, AttachmentSlot.INCAPABLE_SLOT);
            }
        }

        return super.getNoItemIcon();
    }
}
