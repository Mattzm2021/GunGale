package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.GunGale;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttachmentSlot extends Slot {
    protected static final ResourceLocation INCAPABLE_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/incapable_slot");
    private static final ResourceLocation EMPTY_MAG_SLOT = new ResourceLocation(GunGale.MOD_ID, "textures/item/empty_mag_slot.png");
    private static final ResourceLocation EMPTY_BARREL_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_barrel_slot");
    private static final ResourceLocation EMPTY_STOCK_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_stock_slot");
    private static final ResourceLocation EMPTY_OPTIC_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_optic_slot");
    private static final ResourceLocation EMPTY_SPECIAL_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_special_slot");
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = {EMPTY_MAG_SLOT, EMPTY_BARREL_SLOT, EMPTY_STOCK_SLOT, EMPTY_OPTIC_SLOT, EMPTY_SPECIAL_SLOT};

    protected final int weaponSlot;
    private final int textureIndex;

    public AttachmentSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot, int textureIndex) {
        super(inventory, index, posX, posY);
        this.weaponSlot = weaponSlot;
        this.textureIndex = textureIndex;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        if (this.container.getItem(this.weaponSlot).isEmpty()) {
            return super.getNoItemIcon();
        } else {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[this.textureIndex]);
        }
    }
}
