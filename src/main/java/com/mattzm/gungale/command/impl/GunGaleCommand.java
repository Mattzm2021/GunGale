package com.mattzm.gungale.command.impl;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.util.game.FormalGameHandler;
import com.mattzm.gungale.util.game.QuickGameHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class GunGaleCommand {
    public static void register(@NotNull CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("gungale").requires(GunGaleCommand::checkNormalPermission)
                .then(Commands.literal("clear").executes(GunGaleCommand::clearInventoryNoArgument)
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(GunGaleCommand::clearInventoryWithPlayers))).requires(GunGaleCommand::checkServerPermission)
                .then(Commands.literal("startFormalGame")
                        .then(Commands.argument("fieldRadius", IntegerArgumentType.integer(0))
                                .then(Commands.argument("gameTime", IntegerArgumentType.integer(0))
                                        .executes(GunGaleCommand::startFormalGameWithArgument))))
                .then(Commands.literal("reset")
                        .executes(GunGaleCommand::resetNoArgument))
                .then(Commands.literal("startQuickGame")
                        .then(Commands.argument("centerX", IntegerArgumentType.integer())
                                .then(Commands.argument("centerZ", IntegerArgumentType.integer())
                                        .then(Commands.argument("fieldRadius", IntegerArgumentType.integer(0))
                                                .then(Commands.argument("gameTime", IntegerArgumentType.integer(0))
                                                        .executes(GunGaleCommand::startQuickGameWithArgument)))))));
    }

    private static int clearModInventory(CommandSource ignoredSource, @NotNull Collection<ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            ModPlayerInventory.get(player).clearContent();
            player.containerMenu.broadcastChanges();
        }

        return 0;
    }

    private static int startFormalGame(@NotNull CommandSource source, int fieldRadius, int gameTime) {
        for (ServerPlayerEntity player : source.getServer().getPlayerList().getPlayers()) {
            player.inventory.clearContent();
            ModPlayerInventory.get(player).clearContent();
            player.containerMenu.broadcastChanges();
            player.inventoryMenu.slotsChanged(player.inventory);
            player.broadcastCarriedItem();
            player.removeAllEffects();
            if (!player.getPersistentData().isEmpty()) {
                for (String string : player.getPersistentData().getAllKeys()) {
                    player.getPersistentData().remove(string);
                }
            }

            player.setHealth(player.getMaxHealth());
            player.getFoodData().setFoodLevel(20);

            for (Advancement advancement : source.getServer().getAdvancements().getAllAdvancements()) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
                if (progress.hasProgress()) {
                    for (String s : progress.getCompletedCriteria()) {
                        player.getAdvancements().revoke(advancement, s);
                    }
                }
            }

            player.setGameMode(GameType.SURVIVAL);
            player.setExperienceLevels(0);
            player.addEffect(new EffectInstance(Effects.NIGHT_VISION, gameTime * 20, 0, false, false));
        }

        source.getServer().setPvpAllowed(true);
        source.getServer().setFlightAllowed(true);
        source.getServer().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(false, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(true, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(false, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_FALL_DAMAGE).set(true, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_FIRE_DAMAGE).set(true, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_DROWNING_DAMAGE).set(true, source.getServer());
        source.getServer().setDifficulty(Difficulty.PEACEFUL, true);
        source.getServer().getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, source.getServer());
        source.getServer().setDifficulty(Difficulty.PEACEFUL, true);
        source.getServer().getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(true, source.getServer());

        for (ServerWorld world : source.getServer().getAllLevels()) {
            world.setDayTime(0);
        }

        source.getLevel().setWeatherParameters(6000, 0, false, false);
        source.getLevel().getWorldBorder().setCenter(0.0, 0.0);
        source.getLevel().getWorldBorder().setWarningBlocks(10);
        source.getLevel().getWorldBorder().setWarningTime(60);
        String command = "spreadplayers 0 0 " + getSpreadDistance(source.getAllTeams().size(), fieldRadius) + " " + fieldRadius + " true @a";
        source.getServer().getCommands().performCommand(source, command);
        source.getLevel().getWorldBorder().setSize(fieldRadius * 2);
        source.getServer().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(true, source.getServer());

        for (ServerPlayerEntity player : source.getServer().getPlayerList().getPlayers()) {
            player.setRespawnPosition(source.getLevel().dimension(), player.blockPosition(), 0.0f, true, false);
        }

        FormalGameHandler.createNewInstance(source.getServer(), fieldRadius, gameTime * 20);
        if (FormalGameHandler.getInstance() != null) {
            FormalGameHandler.getInstance().startFormalGame();
        }

        return 0;
    }

    private static int getSpreadDistance(int teamAmount, int fieldRadius) {
        return (int) (2 * fieldRadius / Math.sqrt(teamAmount));
    }

    private static int startQuickGame(@NotNull CommandSource source, int centerX, int centerZ, int fieldRadius, int gameTime) {
        for (ServerPlayerEntity player : source.getServer().getPlayerList().getPlayers()) {
            player.inventory.clearContent();
            ModPlayerInventory.get(player).clearContent();
            player.containerMenu.broadcastChanges();
            player.inventoryMenu.slotsChanged(player.inventory);
            player.broadcastCarriedItem();
            player.removeAllEffects();
            if (!player.getPersistentData().isEmpty()) {
                for (String string : player.getPersistentData().getAllKeys()) {
                    player.getPersistentData().remove(string);
                }
            }

            player.setHealth(player.getMaxHealth());
            player.getFoodData().setFoodLevel(20);

            for (Advancement advancement : source.getServer().getAdvancements().getAllAdvancements()) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
                if (progress.hasProgress()) {
                    for (String s : progress.getCompletedCriteria()) {
                        player.getAdvancements().revoke(advancement, s);
                    }
                }
            }

            player.setGameMode(GameType.SURVIVAL);
            player.setExperienceLevels(0);
            player.addEffect(new EffectInstance(Effects.NIGHT_VISION, gameTime * 20, 0, false, false));
        }

        source.getServer().setPvpAllowed(false);
        source.getServer().setFlightAllowed(true);
        source.getServer().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(false, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_FALL_DAMAGE).set(false, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_FIRE_DAMAGE).set(false, source.getServer());
        source.getServer().getGameRules().getRule(GameRules.RULE_DROWNING_DAMAGE).set(false, source.getServer());
        source.getServer().setDifficulty(Difficulty.PEACEFUL, true);
        source.getServer().getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, source.getServer());

        for (ServerWorld world : source.getServer().getAllLevels()) {
            world.setDayTime(6000);
        }

        source.getLevel().setWeatherParameters(6000, 0, false, false);
        source.getLevel().getWorldBorder().setCenter(centerX, centerZ);
        source.getLevel().getWorldBorder().setWarningBlocks(5);
        source.getLevel().getWorldBorder().setWarningTime(10);
        String command = "spreadplayers 0 0 " + getSpreadDistance(source.getAllTeams().size() == 0 ? source.getServer().getPlayerCount() : source.getAllTeams().size(), fieldRadius) + " " + fieldRadius + " true @a";
        source.getServer().getCommands().performCommand(source, command);
        source.getLevel().getWorldBorder().setSize(fieldRadius * 2);
        source.getServer().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(true, source.getServer());

        for (ServerPlayerEntity player : source.getServer().getPlayerList().getPlayers()) {
            player.setRespawnPosition(source.getLevel().dimension(), player.blockPosition(), 0.0f, true, false);
        }

        QuickGameHandler.createNewInstance(source.getServer(), gameTime * 20);
        if (QuickGameHandler.getInstance() != null) {
            QuickGameHandler.getInstance().startGame();
        }

        return 0;
    }

    private static int reset(@NotNull CommandSource source) {
        for (ServerPlayerEntity player : source.getServer().getPlayerList().getPlayers()) {
            player.setGameMode(GameType.CREATIVE);
            player.removeAllEffects();
        }

        source.getLevel().getWorldBorder().setSize(10000000);
        if (FormalGameHandler.getInstance() != null) {
            FormalGameHandler.getInstance().stopFormalGame();
        }

        if (QuickGameHandler.getInstance() != null) {
            QuickGameHandler.getInstance().stopGame();
        }

        return 0;
    }

    private static boolean checkNormalPermission(@NotNull CommandSource source) {
        return source.hasPermission(2);
    }

    private static boolean checkServerPermission(@NotNull CommandSource source) {
        return source.hasPermission(2);
    }

    private static int clearInventoryNoArgument(@NotNull CommandContext<CommandSource> context) throws CommandSyntaxException {
        return clearModInventory(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException()));
    }

    private static int clearInventoryWithPlayers(@NotNull CommandContext<CommandSource> context) throws CommandSyntaxException {
        return clearModInventory(context.getSource(), EntityArgument.getPlayers(context, "targets"));
    }

    private static int startFormalGameWithArgument(@NotNull CommandContext<CommandSource> context) {
        return startFormalGame(context.getSource(), IntegerArgumentType.getInteger(context, "fieldRadius"), IntegerArgumentType.getInteger(context, "gameTime"));
    }

    private static int startQuickGameWithArgument(@NotNull CommandContext<CommandSource> context) {
        return startQuickGame(context.getSource(), IntegerArgumentType.getInteger(context, "centerX"), IntegerArgumentType.getInteger(context, "centerZ"), IntegerArgumentType.getInteger(context, "fieldRadius"), IntegerArgumentType.getInteger(context, "gameTime"));
    }

    private static int resetNoArgument(@NotNull CommandContext<CommandSource> context) {
        return reset(context.getSource());
    }
}
