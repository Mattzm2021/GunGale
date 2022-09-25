package com.mattzm.gungale.server.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.play.MessageHandler;
import com.mattzm.gungale.message.play.SFormalGameMessage;
import com.mattzm.gungale.util.game.FormalGameHandler;
import com.mattzm.gungale.util.inventory.StaticInventory;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class ServerPlayerHandler {
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            StaticInventory.serverClear(event.getPlayer().getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            if (FormalGameHandler.getInstance() != null) {
                MessageHandler.sendToPlayer(new SFormalGameMessage(true), event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            StaticInventory.serverClear(event.getPlayer().getUUID());
            StaticInventory.serverSet(event.getPlayer().getUUID(), ModPlayerInventory.createNew(event.getPlayer(), ModPlayerInventory.get(event.getPlayer())));
            StaticInventory.tempClear(event.getPlayer().getUUID());
            if (FormalGameHandler.getInstance() != null) {
                event.getPlayer().setGameMode(GameType.SPECTATOR);
            }
        }
    }
}
