package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModWorkbenchContainer extends RecipeBookContainer<CraftingInventory> {
    private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
    private final CraftResultInventory resultSlots = new CraftResultInventory();
    private final IWorldPosCallable access;
    private final PlayerEntity player;

    public final ModPlayerInventory inventory;

    public ModWorkbenchContainer(int p_i50089_1_, PlayerInventory p_i50089_2_) {
        this(p_i50089_1_, p_i50089_2_, ModPlayerInventory.get(p_i50089_2_.player), IWorldPosCallable.NULL);
    }

    public ModWorkbenchContainer(int p_i50090_1_, @NotNull PlayerInventory p_i50090_2_, ModPlayerInventory inventory, IWorldPosCallable p_i50090_3_) {
        super(ModContainerTypes.MOD_CRAFTING, p_i50090_1_);
        this.access = p_i50090_3_;
        this.player = p_i50090_2_.player;
        this.inventory = inventory;
        this.addSlot(new CraftingResultSlot(p_i50090_2_.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new InventorySlot(p_i50090_2_, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new InventorySlot(p_i50090_2_, l, 8 + l * 18, 142));
        }

        int slotAmount = 10;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++, slotAmount--) {
                if (slotAmount > 0) {
                    this.addSlot(new CraftingUtilSlot(inventory, 12 + i * 4 + j, 53 + j * 18, 88 + i * 18));
                }
            }
        }
    }

    protected static void slotChangedCraftingGrid(int p_217066_0_, @NotNull World p_217066_1_, PlayerEntity p_217066_2_, CraftingInventory p_217066_3_, CraftResultInventory p_217066_4_) {
        if (!p_217066_1_.isClientSide) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)p_217066_2_;
            ItemStack itemstack = ItemStack.EMPTY;
            if (p_217066_1_.getServer() != null) {
                Optional<ICraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, p_217066_3_, p_217066_1_);
                if (optional.isPresent()) {
                    ICraftingRecipe icraftingrecipe = optional.get();
                    if (p_217066_4_.setRecipeUsed(p_217066_1_, serverplayerentity, icraftingrecipe)) {
                        itemstack = icraftingrecipe.assemble(p_217066_3_);
                    }
                }

                p_217066_4_.setItem(0, itemstack);
                serverplayerentity.connection.send(new SSetSlotPacket(p_217066_0_, 0, itemstack));
            }
        }
    }

    public void setToDefault() {
        for (Slot slot : this.slots) {
            if (slot instanceof CraftingUtilSlot) {
                ((CraftingUtilSlot) slot).isActive = false;
            } else if (slot instanceof InventorySlot) {
                ((InventorySlot) slot).isActive = true;
            }
        }
    }

    public void setToMod() {
        for (Slot slot : this.slots) {
            if (slot instanceof CraftingUtilSlot) {
                ((CraftingUtilSlot) slot).isActive = true;
            } else if (slot instanceof InventorySlot) {
                ((InventorySlot) slot).isActive = false;
            }
        }
    }

    @Override
    public void slotsChanged(@NotNull IInventory p_75130_1_) {
        this.access.execute((p_217069_1_, p_217069_2_) -> slotChangedCraftingGrid(this.containerId, p_217069_1_, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull RecipeItemHelper p_201771_1_) {
        this.craftSlots.fillStackedContents(p_201771_1_);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(@NotNull IRecipe<? super CraftingInventory> p_201769_1_) {
        return p_201769_1_.matches(this.craftSlots, this.player.level);
    }

    @Override
    public void removed(@NotNull PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217068_2_, p_217068_3_) -> this.clearContainer(p_75134_1_, p_217068_2_, this.craftSlots));
    }

    @Override
    public boolean stillValid(@NotNull PlayerEntity p_75145_1_) {
        return stillValid(this.access, p_75145_1_, Blocks.CRAFTING_TABLE);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull PlayerEntity p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (p_82846_2_ == 0) {
                this.access.execute((p_217067_2_, p_217067_3_) -> itemStack1.getItem().onCraftedBy(itemStack1, p_217067_2_, p_82846_1_));
                if (!this.moveItemStackTo(itemStack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemstack);
            } else if (p_82846_2_ >= 10 && p_82846_2_ < 46) {
                if (!this.moveItemStackTo(itemStack1, 1, 10, false)) {
                    if (p_82846_2_ < 37) {
                        if (!this.moveItemStackTo(itemStack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemStack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemStack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemStack2 = slot.onTake(p_82846_1_, itemStack1);
            if (p_82846_2_ == 0) {
                p_82846_1_.drop(itemStack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack p_94530_1_, @NotNull Slot p_94530_2_) {
        return p_94530_2_.container != this.resultSlots && super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 10;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull RecipeBookCategory getRecipeBookType() {
        return RecipeBookCategory.CRAFTING;
    }
}
