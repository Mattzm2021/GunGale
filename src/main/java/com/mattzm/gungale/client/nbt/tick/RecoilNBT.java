package com.mattzm.gungale.client.nbt.tick;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class RecoilNBT {
    public static final String TAG_ID = "recoil";
    public static final String TAG_TICK = "tick";
    public static final String TAG_TARGET = "target";
    public static final String TAG_RECOIL = "recoil";

    public static void tick(PlayerEntity player) {
        setTick(player, getTick(player) + 1);
    }

    public static void start(PlayerEntity player) {
        setTick(player, 1);
    }

    public static void startWith(PlayerEntity player, int target, double recoil) {
        setTarget(player, target);
        setRecoil(player, recoil);
        start(player);
    }

    public static void reset(PlayerEntity player) {
        setTarget(player, 0);
        setTick(player, 0);
    }

    public static boolean hasStart(PlayerEntity player) {
        return getTick(player) > 0;
    }

    public static boolean hasFinishTick(@NotNull PlayerEntity player) {
        return hasStart(player) && getTick(player) == getTarget(player);
    }

    public static double getRecoil(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getDouble(TAG_RECOIL);
    }

    public static void setRecoil(@NotNull PlayerEntity player, double value) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putDouble(TAG_RECOIL, value);
        player.getPersistentData().put(TAG_ID, nbt);
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
}
