package com.mattzm.gungale.client.nbt.tick;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.NBTAction;
import com.mattzm.gungale.message.play.CRestoreMessage;
import com.mattzm.gungale.message.play.MessageHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class RestoreNBT {
    public static final String TAG_ID = "restore";
    public static final String TAG_TICK = "tick";
    public static final String TAG_TARGET = "target";
    public static final String TAG_ITEM = "item";
    public static final String TAG_POS = "pos";
    public static final String TAG_COUNT = "count";

    public static void tick(PlayerEntity player) {
        setTick(player, getTick(player) + 1);
    }

    public static void startWith(PlayerEntity player, int target) {
        if (hasStart(player)) return;
        setTarget(player, target);
        setItem(player, Objects.requireNonNull(player.getMainHandItem().getItem().getRegistryName()).getPath());
        setPos(player, ModPlayerInventory.get(player).selected);
        setCount(player, player.getMainHandItem().getCount());
        setTick(player, 1);
        MessageHandler.sendToServer(new CRestoreMessage(NBTAction.START));
    }

    public static void reset(PlayerEntity player) {
        if (!hasStart(player)) return;
        setTarget(player, 0);
        setItem(player, "");
        setPos(player, 0);
        setCount(player, 0);
        setTick(player, 0);
        MessageHandler.sendToServer(new CRestoreMessage(NBTAction.END));
    }

    public static boolean hasStart(PlayerEntity player) {
        return getTick(player) > 0 && !getItem(player).equals("") && getTarget(player) != 0 && getCount(player) != 0;
    }

    public static boolean hasFinishTick(@NotNull PlayerEntity player) {
        return hasStart(player) && getTick(player) == getTarget(player);
    }

    public static boolean hasChangeStack(@NotNull PlayerEntity player) {
        if (!hasStart(player)) return false;
        if (!getItem(player).equals(Objects.requireNonNull(player.getMainHandItem().getItem().getRegistryName()).getPath())) return true;
        if (getCount(player) != player.getMainHandItem().getCount()) return true;
        return getPos(player) != ModPlayerInventory.get(player).selected;
    }

    public static int getRealSpace(@NotNull PlayerEntity player) {
        return getTarget(player) - getTick(player) + 1;
    }

    public static int getTick(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_TICK);
    }

    private static void setTick(@NotNull PlayerEntity player, int value) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putInt(TAG_TICK, value);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static int getTarget(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_TARGET);
    }

    public static void setTarget(@NotNull PlayerEntity player, int target) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putInt(TAG_TARGET, target);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static @NotNull String getItem(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getString(TAG_ITEM);
    }

    public static void setItem(@NotNull PlayerEntity player, String id) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putString(TAG_ITEM, id);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static int getPos(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_POS);
    }

    public static void setPos(@NotNull PlayerEntity player, int pos) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putInt(TAG_POS, pos);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static int getCount(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_COUNT);
    }

    public static void setCount(@NotNull PlayerEntity player, int count) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putInt(TAG_COUNT, count);
        player.getPersistentData().put(TAG_ID, nbt);
    }

}
