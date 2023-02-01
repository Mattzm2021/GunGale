package com.mattzm.gungale.util.inventory;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaticInventory {
    public static final int size = 22;
    private static final Map<UUID, ModPlayerInventory> tempInventoryList = new HashMap<>();
    private static final Map<UUID, ModPlayerInventory> serverInventoryList = new HashMap<>();
    private static final Map<UUID, ModPlayerInventory> clientInventoryList = new HashMap<>();

    public static @Nullable ModPlayerInventory getSInventory(UUID uuid) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            return serverInventoryList.getOrDefault(uuid, tempInventoryList.getOrDefault(uuid, null));
        } else {
            throw new IllegalStateException("Cannot get Server Inventory from Client Thread!");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static @Nullable ModPlayerInventory getCInventory(PlayerEntity player, UUID uuid) {
        clientSet(uuid, new ModPlayerInventory(player));
        return clientInventoryList.getOrDefault(uuid, null);
    }

    public static void serverSet(UUID uuid, ModPlayerInventory inventory) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            if (!serverInventoryList.containsKey(uuid)) {
                serverInventoryList.put(uuid, inventory);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSet(UUID uuid, ModPlayerInventory inventory) {
        if (!clientInventoryList.containsKey(uuid)) {
            clientInventoryList.put(uuid, inventory);
        }
    }

    public static void serverClear(UUID uuid) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            if (!tempInventoryList.containsKey(uuid)) {
                tempInventoryList.put(uuid, serverInventoryList.getOrDefault(uuid, null));
            }

            serverInventoryList.remove(uuid);
            clientInventoryList.remove(uuid);
        }
    }

    public static void tempClear(UUID uuid) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            tempInventoryList.remove(uuid);
        }
    }
}
