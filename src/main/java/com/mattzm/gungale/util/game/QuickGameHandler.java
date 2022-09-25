package com.mattzm.gungale.util.game;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuickGameHandler {
    @Nullable
    private static QuickGameHandler instance = null;
    @NotNull
    private final MinecraftServer server;
    private final QuickGameTimer timer;

    private QuickGameHandler(@NotNull MinecraftServer server, QuickGameTimer timer) {
        this.server = server;
        this.timer = timer;
    }

    public void startGame() {
        LogManager.getLogger().info("Quick Game Started!");
        this.timer.startTimer();
        showStageTitle(1, this.server);
    }

    public void stopGame() {
        LogManager.getLogger().info("Quick Game Stopped!");
        instance = null;
    }

    public static void showStageTitle(int stage, @NotNull MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            player.connection.send(new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("game.formal.stage_" + stage + "_title")));
            player.connection.send(new STitlePacket(STitlePacket.Type.SUBTITLE, new TranslationTextComponent("game.formal.stage_" + stage + "_subtitle")));
        }
    }

    public static void createNewInstance(MinecraftServer server, int gameTime) {
        instance = new QuickGameHandler(server, new QuickGameTimer(server, gameTime, gameTime / 5));
    }

    public static @Nullable QuickGameHandler getInstance() {
        return instance;
    }

    public QuickGameTimer getTimer() {
        return this.timer;
    }

    public @NotNull MinecraftServer getServer() {
        return this.server;
    }
}
