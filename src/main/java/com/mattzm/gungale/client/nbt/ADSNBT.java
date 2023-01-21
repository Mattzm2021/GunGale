package com.mattzm.gungale.client.nbt;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.NBTAction;
import com.mattzm.gungale.message.play.CADSMessage;
import com.mattzm.gungale.message.play.MessageHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ADSNBT {
    public static final String TAG_ID = "ads";
    public static final String TAG_STATUS = "status";
    public static final String TAG_SPEED = "speed";
    public static final String TAG_START_FOV = "startFov";

    public static void start(@NotNull PlayerEntity player, double fov) {
        AbstractWeaponItem item = (AbstractWeaponItem) ModPlayerInventory.get(player).getSelected().getItem();
        MessageHandler.sendToServer(new CADSMessage(NBTAction.START));
        setSpeed(player, item.adsSpeed);
        setFov(player, fov);
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putBoolean(TAG_STATUS, true);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static void end(@NotNull PlayerEntity player) {
        MessageHandler.sendToServer(new CADSMessage(NBTAction.END));
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putBoolean(TAG_STATUS, false);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static boolean onADS(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getBoolean(TAG_STATUS);
    }

    public static int getSpeed(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getInt(TAG_SPEED);
    }

    public static void setSpeed(@NotNull PlayerEntity player, int adsSpeed) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putInt(TAG_SPEED, adsSpeed);
        player.getPersistentData().put(TAG_ID, nbt);
    }

    public static double getFov(@NotNull PlayerEntity player) {
        return player.getPersistentData().getCompound(TAG_ID).getDouble(TAG_START_FOV);
    }

    public static void setFov(@NotNull PlayerEntity player, double fov) {
        CompoundNBT nbt = player.getPersistentData().getCompound(TAG_ID);
        nbt.putDouble(TAG_START_FOV, fov);
        player.getPersistentData().put(TAG_ID, nbt);
    }
}
