package com.mattzm.gungale.client.nbt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FireNBT {
    public static final String TAG_ID = "fire";
    public static final String TAG_STATUS = "status";
    public static final String TAG_TICK = "tick";

    public static void start(@NotNull PlayerEntity player) {
        CompoundNBT compoundNBT = player.getPersistentData().getCompound(TAG_ID);
        compoundNBT.putBoolean(TAG_STATUS, true);
        compoundNBT.putInt(TAG_TICK, 1);
        player.getPersistentData().put(TAG_ID, compoundNBT);
    }

    public static void end(@NotNull PlayerEntity player) {
        CompoundNBT compoundNBT = player.getPersistentData().getCompound(TAG_ID);
        compoundNBT.putBoolean(TAG_STATUS, false);
        compoundNBT.putInt(TAG_TICK, 0);
        player.getPersistentData().put(TAG_ID, compoundNBT);
    }

    public static void tick(@NotNull PlayerEntity player) {
        CompoundNBT compoundNBT = player.getPersistentData().getCompound(TAG_ID);
        compoundNBT.putInt(TAG_TICK, compoundNBT.getInt(TAG_TICK) + 1);
        player.getPersistentData().put(TAG_ID, compoundNBT);
    }

    public static boolean onFire(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getBoolean(TAG_STATUS);
    }

    public static int getTick(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_TICK);
    }
}
