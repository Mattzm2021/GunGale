package com.mattzm.gungale.client.nbt;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ShotCountNBT {
    public static final String TAG_ID = "shotCount";
    public static final String TAG_COUNT = "count";
    public static final String TAG_ITEM = "item";
    public static final String TAG_POS = "pos";

    public static boolean next(PlayerEntity player) {
        if (hasStart(player)) {
            if (hasFinish(player)) {
                reset(player);
                return true;
            } else {
                setCount(player, getCount(player) + 1);
            }
        }

        return false;
    }

    public static void start(PlayerEntity player) {
        setCount(player, 1);
        setItem(player, Objects.requireNonNull(ModPlayerInventory.get(player).getSelected().getItem().getRegistryName()).getPath());
        setPos(player, ModPlayerInventory.get(player).selected);
    }

    public static void reset(PlayerEntity player) {
        setCount(player, 0);
        setItem(player, "");
        setPos(player, 0);
    }

    public static boolean hasStart(PlayerEntity player) {
        return getCount(player) > 0 && !getItem(player).equals("");
    }

    public static boolean hasFinish(@NotNull PlayerEntity player) {
        return hasStart(player) && getCount(player) == 3;
    }

    public static boolean hasChangeStack(@NotNull PlayerEntity player) {
        if (!hasStart(player)) return false;
        if (!getItem(player).equals(Objects.requireNonNull(ModPlayerInventory.get(player).getSelected().getItem().getRegistryName()).getPath())) return true;
        return getPos(player) != ModPlayerInventory.get(player).selected;
    }

    public static int getCount(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_COUNT);
    }

    public static void setCount(@NotNull PlayerEntity player, int count) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putInt(TAG_COUNT, count);
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
}
