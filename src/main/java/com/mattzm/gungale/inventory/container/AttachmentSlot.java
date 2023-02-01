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
    public static final ResourceLocation INCAPABLE_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/incapable_slot");
    public static final ResourceLocation EMPTY_BARREL_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_barrel_slot");
    public static final ResourceLocation EMPTY_HEAVY_MAG_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_heavy_mag_slot");
    public static final ResourceLocation EMPTY_LIGHT_MAG_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_light_mag_slot");
    public static final ResourceLocation EMPTY_ENERGY_MAG_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_energy_mag_slot");
    public static final ResourceLocation EMPTY_OPTIC_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_optic_slot");
    public static final ResourceLocation EMPTY_STOCK_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_stock_slot");
    public static final ResourceLocation EMPTY_HOP_UP_SLOT = new ResourceLocation(GunGale.MOD_ID, "item/empty_hop_up_slot");
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = {EMPTY_BARREL_SLOT, EMPTY_HEAVY_MAG_SLOT, EMPTY_LIGHT_MAG_SLOT, EMPTY_ENERGY_MAG_SLOT, EMPTY_OPTIC_SLOT, EMPTY_STOCK_SLOT, EMPTY_HOP_UP_SLOT};
    protected final int weaponSlot;
    protected int textureIndex;

    public AttachmentSlot(IInventory inventory, int index, int posX, int posY, int weaponSlot, int textureIndex) {
        super(inventory, index, posX, posY);
        this.weaponSlot = weaponSlot;
        this.textureIndex = textureIndex;
    }

    protected void updateTextureIndex() {}

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
            this.updateTextureIndex();
            return Pair.of(PlayerContainer.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[this.textureIndex]);
        }
    }
}
