package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.ClientUtil;
import com.mattzm.gungale.client.nbt.ClientNBTHelper;
import com.mattzm.gungale.client.object.ClientObjectHolder;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.GearItem;
import com.mattzm.gungale.item.RestoreItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.util.ModEffectHandler;
import com.mattzm.gungale.util.game.FormalGameHandler;
import com.mattzm.gungale.util.game.FormalGameTimer;
import com.mattzm.gungale.util.game.QuickGameHandler;
import com.mattzm.gungale.util.game.QuickGameTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class TickHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(@NotNull TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientObjectHolder.getInstance().getMOptions().onClientTick();
            ClientObjectHolder.getInstance().getMIngameGui().tick();
            ClientObjectHolder.getInstance().getMItemInHandRenderer().tick();
            if (Minecraft.getInstance().player != null) {
                ClientNBTHelper.checkAndSetAll(Minecraft.getInstance().player);
            }
            ClientUtil.tick();
        }
    }

    @SubscribeEvent
    public static void onServerTick(@NotNull TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (FormalGameHandler.getInstance() != null) {
                FormalGameHandler gameHandler = FormalGameHandler.getInstance();
                FormalGameTimer timer = gameHandler.getTimer();
                MinecraftServer server = gameHandler.getServer();
                WorldBorder border = server.overworld().getWorldBorder();
                if (timer.getTickTime() == gameHandler.getFirstStageTime()) {
                    border.lerpSizeBetween(border.getSize(), border.getSize() / 2, (gameHandler.getTransitionStageTime() - timer.getTickTime()) * 50L);
                    gameHandler.showStageTitle(2);
                } else if (timer.getTickTime() == gameHandler.getTransitionStageTime()) {
                    border.setSize(gameHandler.getFieldRadius());
                    gameHandler.showStageTitle(3);
                } else if (timer.getTickTime() == gameHandler.getFinalStageTime()) {
                    border.lerpSizeBetween(border.getSize(), 1, (timer.getGameTime() - timer.getTickTime()) * 50L);
                    gameHandler.showStageTitle(4);
                }

                timer.tick();
            }

            if (QuickGameHandler.getInstance() != null) {
                QuickGameHandler gameHandler = QuickGameHandler.getInstance();
                QuickGameTimer timer = gameHandler.getTimer();
                timer.tick();
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(@NotNull TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.SERVER) {
                World world = event.world;
                if (world.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).get()) {
                    world.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, world.getServer());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(@NotNull TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.CLIENT) {
                ClientObjectHolder.getInstance().getMOptions().setSensitivity(player);
                AbstractWeaponItem.onClientTick(player);
                RestoreItem.onClientTick(player);
            } else {
                GearItem.onServerTick(player);
                ModPlayerInventory.onServerTick(player);
                AbstractWeaponItem.onServerTick(player);
                ModEffectHandler.onServerTick(player);
            }
        }
    }
}
