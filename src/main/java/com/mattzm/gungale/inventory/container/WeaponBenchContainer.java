package com.mattzm.gungale.inventory.container;

import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.item.crafting.ModRecipeTypes;
import com.mattzm.gungale.item.crafting.WeaponRecipe;
import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@VanillaCode
public class WeaponBenchContainer extends RecipeBookContainer<CraftingInventory> {
    private final CraftingInventory craftSlots = new CraftingInventory(this, 5, 3);
    private final CraftResultInventory resultSlots = new CraftResultInventory();
    private final IWorldPosCallable access;
    private final PlayerEntity player;

    public WeaponBenchContainer(int containerId, PlayerInventory inventory) {
        this(containerId, inventory, IWorldPosCallable.NULL);
    }

    public WeaponBenchContainer(int containerId, @NotNull PlayerInventory inventory, IWorldPosCallable access) {
        super(ModContainerTypes.WEAPON_BENCH, containerId);
        this.access = access;
        this.player = inventory.player;
        this.addSlot(new CraftingResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, 142, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 5, 12 + j * 18, 17 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
        }
    }

    protected static void slotChangedCraftingGrid(int containerId, @NotNull World world, PlayerEntity player, CraftingInventory craftSlots, CraftResultInventory resultSlots) {
        if (world.isClientSide) return;
        ServerPlayerEntity player1 = (ServerPlayerEntity) player;
        ItemStack stack = ItemStack.EMPTY;
        if (world.getServer() == null) return;
        Optional<WeaponRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(ModRecipeTypes.WEAPON, craftSlots, world);
        if (optional.isPresent()) {
            ICraftingRecipe recipe = optional.get();
            if (resultSlots.setRecipeUsed(world, player1, recipe)) {
                stack = recipe.assemble(craftSlots);
            }
        }

        resultSlots.setItem(0, stack);
        player1.connection.send(new SSetSlotPacket(containerId, 0, stack));
    }

    @Override
    public void slotsChanged(@NotNull IInventory inventory) {
        this.access.execute((world, pos) -> slotChangedCraftingGrid(this.containerId, world, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull RecipeItemHelper helper) {
        this.craftSlots.fillStackedContents(helper);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(@NotNull IRecipe<? super CraftingInventory> recipe) {
        return recipe instanceof WeaponRecipe && recipe.matches(this.craftSlots, this.player.level);
    }

    @Override
    public void removed(@NotNull PlayerEntity player) {
        super.removed(player);
        this.access.execute((world, pos) -> this.clearContainer(player, world, this.craftSlots));
    }

    @Override
    public boolean stillValid(@NotNull PlayerEntity player) {
        return stillValid(this.access, player, ModBlocks.WEAPON_BENCH);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull PlayerEntity player, int slotId) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (slotId == 0) {
                this.access.execute((world, pos) -> stack1.getItem().onCraftedBy(stack1, world, player));
                if (!this.moveItemStackTo(stack1, 16, 52, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack1, stack);
            } else if (slotId >= 16 && slotId < 52) {
                if (!this.moveItemStackTo(stack1, 1, 16, false)) {
                    if (slotId < 43) {
                        if (!this.moveItemStackTo(stack1, 43, 52, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(stack1, 16, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(stack1, 16, 52, false)) {
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

            ItemStack stack2 = slot.onTake(player, stack1);
            if (slotId == 0) {
                player.drop(stack2, false);
            }
        }

        return stack;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
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
        return 16;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull RecipeBookCategory getRecipeBookType() {
        return RecipeBookCategory.CRAFTING;
    }
}
