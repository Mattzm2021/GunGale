package com.mattzm.gungale.entity.player;

import com.mattzm.gungale.client.nbt.ADSNBT;
import com.mattzm.gungale.inventory.container.ModItemContainer;
import com.mattzm.gungale.inventory.container.ModWorkbenchContainer;
import com.mattzm.gungale.item.*;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.play.CChangeInventoryMessage;
import com.mattzm.gungale.message.play.MessageHandler;
import com.mattzm.gungale.message.play.SUpdateInventoryMessage;
import com.mattzm.gungale.nbt.stack.MagSizeNBT;
import com.mattzm.gungale.util.inventory.StaticInventory;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.INameable;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ModPlayerInventory implements INamedContainerProvider, IInventory, INameable {
    public final NonNullList<ItemStack> items = NonNullList.withSize(22, ItemStack.EMPTY);
    private final PlayerEntity player;
    public int selected;
    public boolean status;

    public ModPlayerInventory(PlayerEntity player) {
        this.player = player;
        this.selected = 0;
        this.status = false;
    }

    private ModPlayerInventory(@NotNull PlayerEntity player, @NotNull CompoundNBT tag) {
        this.player = player;
        this.load(tag.getList("items", 10));
        this.selected = tag.getInt("selected");
        this.status = tag.getBoolean("status");
    }

    private ModPlayerInventory(PlayerEntity player, @NotNull ModPlayerInventory inventory) {
        this.player = player;
        this.load(inventory.items);
        this.selected = inventory.selected;
        this.status = inventory.status;
    }

    public @NotNull ItemStack getSelected() {
        return this.status ? this.getItem(this.selected) : ItemStack.EMPTY;
    }

    public static ModPlayerInventory get(@NotNull PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            return StaticInventory.getSInventory(player.getUUID());
        } else {
            return StaticInventory.getCInventory(player, player.getUUID());
        }
    }

    public static @NotNull ModPlayerInventory createNew(PlayerEntity player, CompoundNBT inventory) {
        return new ModPlayerInventory(player, inventory);
    }

    public static @NotNull ModPlayerInventory createNew(PlayerEntity player, ModPlayerInventory inventory) {
        return new ModPlayerInventory(player, inventory);
    }

    public static void onServerTick(@NotNull PlayerEntity player) {
        if (!player.level.isClientSide) {
            ModPlayerInventory inventory = get(player);
            inventory.checkIfRemoveAttachment();

            for (ItemStack stack : player.inventory.items) {
                if (stack.getItem() instanceof AttachmentItem) {
                    player.inventory.removeItem(stack);
                    inventory.checkAndSetAttachment(stack);
                } else if (stack.getItem() instanceof AbstractWeaponItem) {
                    player.inventory.removeItem(stack);
                    inventory.checkAndSetWeapon(stack);
                } else if (stack.getItem() instanceof AmmoItem) {
                    player.inventory.removeItem(stack);
                    inventory.add(stack);
                }
            }

            for (int i = 0; i < 2; i++) {
                if (!inventory.getItem(i * 6).isEmpty()) {
                    ItemStack stack = inventory.getItem(i * 6);
                    if (!inventory.getItem(i * 6 + 1).isEmpty()) {
                        MagSizeNBT.set(stack, ((MagItem) inventory.getItem(i * 6 + 1).getItem()).getMagazineSize(stack));
                    } else {
                        MagSizeNBT.set(stack, ((AbstractWeaponItem) (stack.getItem())).magazineSize);
                    }

                    inventory.setItem(i * 6, stack);
                }
            }

            for (ItemStack stack : inventory.items) {
                if (!stack.isEmpty()) {
                    if (stack.getPopTime() > 0) {
                        stack.setPopTime(stack.getPopTime() - 1);
                    }
                }
            }

            if (!(player.containerMenu instanceof ModItemContainer)) {
                MessageHandler.sendToPlayer(new SUpdateInventoryMessage(inventory, SUpdateInventoryMessage.Action.ALL), player);
            }
        }
    }

    public void dropAll() {
        for (int i = 0; i < this.items.size(); i++) {
            ItemStack stack = this.items.get(i);
            if (!stack.isEmpty()) {
                this.player.drop(stack, true, false);
                this.items.set(i, ItemStack.EMPTY);
            }
        }
    }

    public void checkIfRemoveAttachment() {
        if (this.getItem(0).isEmpty()) {
            for (int i = 1; i < 6; i++) {
                if (!this.getItem(i).isEmpty()) {
                    this.setUtilOrDrop(this.getItem(i));
                }
            }
        }

        if (this.getItem(6).isEmpty()) {
            for (int i = 1; i < 6; i++) {
                if (!this.getItem(i + 6).isEmpty()) {
                    this.setUtilOrDrop(this.getItem(i + 6));
                }
            }
        }
    }

    private void setUtilOrDrop(@NotNull ItemStack stack) {
        ItemStack itemStack = stack.copy();
        if (!this.add(stack)) {
            this.player.drop(itemStack, false, true);
        }
    }

    public boolean checkAndSetWeapon(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof AbstractWeaponItem) {
            if (this.getFreeWeaponSlot() != -1) {
                this.setItem(this.getFreeWeaponSlot(), stack.copy());
                stack.setCount(0);
                return true;
            }
        }

        return false;
    }

    public boolean checkAndSetAttachment(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof MagItem) {
            if (this.getFreeMagSlot() != -1) {
                this.setItem(this.getFreeMagSlot(), stack.copy());
                stack.setCount(0);
                return true;
            }
        } else if (stack.getItem() instanceof BarrelItem) {
            if (this.getFreeBarrelSlot() != -1) {
                this.setItem(this.getFreeBarrelSlot(), stack.copy());
                stack.setCount(0);
                return true;
            }
        } else if (stack.getItem() instanceof StockItem) {
            if (this.getFreeStockSlot() != -1) {
                this.setItem(this.getFreeStockSlot(), stack.copy());
                stack.setCount(0);
                return true;
            }
        } else if (stack.getItem() instanceof OpticItem) {
            if (this.getFreeOpticSlot() != -1) {
                this.setItem(this.getFreeOpticSlot(), stack.copy());
                stack.setCount(0);
                return true;
            }
        } else if (stack.getItem() instanceof HopUpItem) {
            if (this.getFreeHopUpSlot() != -1) {
                this.setItem(this.getFreeHopUpSlot(), stack.copy());
                stack.setCount(0);
                return true;
            }
        }

        return this.add(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public void swapPaint() {
        ItemStack stack = ItemStack.EMPTY;

        if (this.selected == 0) {
            MessageHandler.sendToServer(new CChangeInventoryMessage(6));
            stack = this.getItem(6);
        } else if (this.selected == 6) {
            MessageHandler.sendToServer(new CChangeInventoryMessage(0));
            stack = this.getItem(0);
        }

        ADSNBT.setFov(this.player, Minecraft.getInstance().options.fov);
        if (!stack.isEmpty()) {
            ADSNBT.setSpeed(this.player, ((AbstractWeaponItem) stack.getItem()).adsSpeed);
        }
    }

    public int getFreeWeaponSlot() {
        if (this.getItem(0).isEmpty()) {
            return 0;
        } else if (this.getItem(6).isEmpty()) {
            return 6;
        } else {
            return -1;
        }
    }

    public int getFreeMagSlot() {
        if (!this.getItem(0).isEmpty() && this.getItem(1).isEmpty()) {
            return 1;
        } else if (!this.getItem(6).isEmpty() && this.getItem(7).isEmpty()) {
            return 7;
        }

        return -1;
    }

    public int getFreeBarrelSlot() {
        if (!this.getItem(0).isEmpty()) {
            if (this.getItem(2).isEmpty() && ((AbstractWeaponItem) this.getItem(0).getItem()).getBarrel().get()) {
                return 2;
            }
        }

        if (!this.getItem(6).isEmpty() && ((AbstractWeaponItem) this.getItem(6).getItem()).getBarrel().get()) {
            if (this.getItem(8).isEmpty()) {
                return 8;
            }
        }

        return -1;
    }

    public int getFreeStockSlot() {
        if (!this.getItem(0).isEmpty()) {
            if (this.getItem(3).isEmpty()) {
                return 3;
            }
        }

        if (!this.getItem(6).isEmpty()) {
            if (this.getItem(9).isEmpty()) {
                return 9;
            }
        }

        return -1;
    }

    public int getFreeOpticSlot() {
        if (!this.getItem(0).isEmpty()) {
            if (this.getItem(4).isEmpty()) {
                return 4;
            }
        }

        if (!this.getItem(6).isEmpty()) {
            if (this.getItem(10).isEmpty()) {
                return 10;
            }
        }

        return -1;
    }

    public int getFreeHopUpSlot() {
        if (!this.getItem(0).isEmpty()) {
            if (this.getItem(5).isEmpty()) {
                return 5;
            }
        }

        if (!this.getItem(6).isEmpty()) {
            if (this.getItem(11).isEmpty()) {
                return 11;
            }
        }

        return -1;
    }

    public int getFreeUtilSlot() {
        for (int i = 12; i < this.getContainerSize(); i++) {
            if (this.getItem(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private boolean hasRemainingSpaceForItem(@NotNull ItemStack stackA, ItemStack stackB) {
        return !stackA.isEmpty() && isSameItem(stackA, stackB) && stackA.isStackable() && stackA.getCount() < stackA.getMaxStackSize() && stackA.getCount() < this.getMaxStackSize();
    }

    private static boolean isSameItem(@NotNull ItemStack stackA, @NotNull ItemStack stackB) {
        return stackA.getItem() == stackB.getItem() && ItemStack.tagMatches(stackA, stackB);
    }

    public int getSlotWithRemainingSpace(ItemStack stack) {
        for (int i = 12; i < this.items.size(); i++) {
            if (this.hasRemainingSpaceForItem(this.items.get(i), stack)) {
                return i;
            }
        }

        return -1;
    }

    private int addResource(@NotNull ItemStack stack) {
        int i = this.getSlotWithRemainingSpace(stack);
        if (i == -1) {
            i = this.getFreeUtilSlot();
        }

        return i == -1 ? stack.getCount() : this.addResource(stack, i);
    }

    private int addResource(@NotNull ItemStack stack, int index) {
        int i = stack.getCount();
        ItemStack itemstack = this.getItem(index);
        if (itemstack.isEmpty()) {
            itemstack = stack.copy();
            itemstack.setCount(0);
            if (stack.hasTag()) {
                itemstack.setTag(Objects.requireNonNull(stack.getTag()).copy());
            }

            this.setItem(index, itemstack);
        }

        int j = ModMathHelper.min(i, itemstack.getMaxStackSize() - itemstack.getCount(), this.getMaxStackSize() - itemstack.getCount());

        if (j != 0) {
            i = i - j;
            itemstack.grow(j);
            itemstack.setPopTime(5);
        }

        return i;
    }

    public boolean add(ItemStack stack) {
        return this.add(-1, stack);
    }

    public boolean add(int index, @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            try {
                int i;
                do {
                    i = stack.getCount();
                    if (index == -1) {
                        stack.setCount(this.addResource(stack));
                    } else {
                        stack.setCount(this.addResource(stack, index));
                    }
                } while(!stack.isEmpty() && stack.getCount() < i);

                if (stack.getCount() == i && this.player.abilities.instabuild) {
                    stack.setCount(0);
                    return true;
                } else {
                    return stack.getCount() < i;
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being added");
                crashreportcategory.setDetail("Registry Name", () -> String.valueOf(stack.getItem().getRegistryName()));
                crashreportcategory.setDetail("Item Class", () -> stack.getItem().getClass().getName());
                crashreportcategory.setDetail("Item ID", Item.getId(stack.getItem()));
                crashreportcategory.setDetail("Item data", stack.getDamageValue());
                crashreportcategory.setDetail("Item name", () -> stack.getHoverName().getString());
                throw new ReportedException(crashreport);
            }
        }
    }

    public ListNBT save(ListNBT tag) {
        for (int i = 0; i < this.items.size(); i++) {
            if (!this.items.get(i).isEmpty()) {
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putByte("Slot", (byte) i);
                this.items.get(i).save(compoundNBT);
                tag.add(compoundNBT);
            }
        }

        return tag;
    }

    public void load(@NotNull ListNBT tag) {
        this.items.clear();

        for (int i = 0; i < tag.size(); i++) {
            CompoundNBT compoundNBT = tag.getCompound(i);
            int j = compoundNBT.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.of(compoundNBT);
            if (!itemStack.isEmpty()) {
                if (j < this.items.size()) {
                    this.items.set(j, itemStack);
                }
            }
        }
    }

    private void load(@NotNull NonNullList<ItemStack> nonNullList) {
        this.items.clear();

        for (int i = 0; i < nonNullList.size(); i++) {
            this.items.set(i, nonNullList.get(i));
        }
    }

    public @NotNull INamedContainerProvider getMenuProviderForCrafting(World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((counter, inventory, player) ->
                new ModWorkbenchContainer(counter, inventory, this, IWorldPosCallable.create(world, pos)), new TranslationTextComponent("container.crafting"));
    }

    @Override
    public int getContainerSize() {
       return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack stack = this.getItem(index);
        ItemStack copy = stack.copy();
        if (count > copy.getCount()) {
            return this.removeItemNoUpdate(index);
        } else {
            stack.shrink(count);
            setItem(index, stack);
            copy.setCount(count);
            return copy;
        }
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        ItemStack copy = this.getItem(index).copy();
        this.setItem(index, ItemStack.EMPTY);
        return copy;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        this.items.set(index, stack);
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(@NotNull PlayerEntity player) {
        if (!this.player.isAlive()) {
            return false;
        } else {
            return !(player.distanceToSqr(this.player) > 64.0);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public @NotNull ITextComponent getName() {
        return this.getDisplayName();
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.mod_inventory");
    }

    @Override
    public int getMaxStackSize() {
        return 128;
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, @NotNull PlayerInventory p_createMenu_2_, @NotNull PlayerEntity p_createMenu_3_) {
        return new ModItemContainer(p_createMenu_1_, this);
    }
}
