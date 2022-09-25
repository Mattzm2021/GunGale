package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.IModMessage;
import com.mattzm.gungale.util.inventory.StaticInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CChangeInventoryMessage implements IModMessage {
    public NonNullList<ItemStack> stacks;
    public final Action action;
    public final int selected;
    public final boolean status;

    public enum Action {
        ITEMS, ITEM, SELECTED, STATUS, ALL
    }

    public CChangeInventoryMessage(boolean status) {
        this(NonNullList.withSize(22, ItemStack.EMPTY), Action.STATUS, 0, status);
    }

    public CChangeInventoryMessage(int selected) {
        this(NonNullList.withSize(22, ItemStack.EMPTY), Action.SELECTED, selected, false);
    }

    public CChangeInventoryMessage(@NotNull ModPlayerInventory inventory, CChangeInventoryMessage.Action action) {
        this.stacks = inventory.items;
        this.action = action;
        this.selected = inventory.selected;
        this.status = inventory.status;
    }


    public CChangeInventoryMessage(NonNullList<ItemStack> stacks, Action action, int selected, boolean status) {
        this.stacks = stacks;
        this.action = action;
        this.selected = selected;
        this.status = status;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        for (ItemStack stack : this.stacks) {
            buffer.writeItemStack(stack, false);
        }

        buffer.writeEnum(this.action);
        buffer.writeInt(this.selected);
        buffer.writeBoolean(this.status);
    }

    public static @NotNull CChangeInventoryMessage decode(@NotNull PacketBuffer buffer) {
        return new CChangeInventoryMessage(combine(buffer), buffer.readEnum(Action.class), buffer.readInt(), buffer.readBoolean());
    }

    private static @NotNull NonNullList<ItemStack> combine(PacketBuffer buffer) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(StaticInventory.size, ItemStack.EMPTY);

        for (int i = 0; i < StaticInventory.size; i++) {
            stacks.set(i, buffer.readItem());
        }

        return stacks;
    }

    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            ModPlayerInventory inventory = ModPlayerInventory.get(player);
            if (this.action == Action.ALL) {
                for (int i = 0; i < ModPlayerInventory.get(player).getContainerSize(); i++) {
                    inventory.setItem(i, this.stacks.get(i));
                }

                inventory.selected = this.selected;
                inventory.status = this.status;
            } else if (this.action == Action.ITEMS) {
                for (int i = 0; i < ModPlayerInventory.get(player).getContainerSize(); i++) {
                    inventory.setItem(i, this.stacks.get(i));
                }
            } else if (this.action == Action.SELECTED) {
                inventory.selected = this.selected;
            } else if (this.action == Action.STATUS) {
                inventory.status = this.status;
            }
        }
    }
}
