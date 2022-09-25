package com.mattzm.gungale.util.nbt;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.util.game.FormalGameHandler;
import com.mattzm.gungale.util.inventory.StaticInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class ModDataManager {
    public static void saveInventoryData(@NotNull CompoundNBT compoundNBT, PlayerEntity player) {
        CompoundNBT tag = new CompoundNBT();
        tag.put("items", ModPlayerInventory.get(player).save(new ListNBT()));
        tag.putInt("selected", ModPlayerInventory.get(player).selected);
        tag.putBoolean("status", ModPlayerInventory.get(player).status);
        compoundNBT.put("inventory", tag);
        StaticInventory.tempClear(player.getUUID());
    }

    public static void loadInventoryData(@NotNull CompoundNBT compoundNBT, @NotNull PlayerEntity player) {
        CompoundNBT tag = compoundNBT.getCompound("inventory");
        StaticInventory.serverSet(player.getUUID(), ModPlayerInventory.createNew(player, tag));
    }

    public static void saveServerData(CompoundNBT compoundNBT, MinecraftServer server) {
        if (FormalGameHandler.getInstance() != null) {
            CompoundNBT formalGameData = new CompoundNBT();
            formalGameData.putInt("tickTime", FormalGameHandler.getInstance().getTimer().getTickTime());
            formalGameData.putInt("gameTime", FormalGameHandler.getInstance().getTimer().getGameTime());
            formalGameData.putInt("fieldRadius", FormalGameHandler.getInstance().getFieldRadius());
            compoundNBT.put("formalGame", formalGameData);
        }
    }

    public static void loadServerData(@NotNull CompoundNBT compoundNBT, MinecraftServer server) {
        CompoundNBT formalGameData = compoundNBT.getCompound("formalGame");
        if (!formalGameData.equals(new CompoundNBT())) {
            FormalGameHandler.createNewInstance(server, formalGameData.getInt("fieldRadius"), formalGameData.getInt("gameTime"), formalGameData.getInt("tickTime"));
        }
    }
}
