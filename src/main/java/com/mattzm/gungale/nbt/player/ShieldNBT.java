package com.mattzm.gungale.nbt.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShieldNBT {
    public static final String TAG_ID = "shield";

    public static boolean get(@NotNull PlayerEntity player) {
        return player.getPersistentData().getBoolean(TAG_ID);
    }

    public static void set(@NotNull PlayerEntity player, boolean status) {
        player.getPersistentData().putBoolean(TAG_ID, status);
    }
}
