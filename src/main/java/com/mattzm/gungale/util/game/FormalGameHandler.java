package com.mattzm.gungale.util.game;

import com.mattzm.gungale.message.play.MessageHandler;
import com.mattzm.gungale.message.play.SFormalGameMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FormalGameHandler {
    @Nullable
    private static FormalGameHandler instance = null;
    @NotNull
    private final MinecraftServer server;
    private final FormalGameTimer timer;
    private final int fieldRadius;
    private final int firstStageTime;
    private final int transitionStageTime;
    private final int finalStageTime;

    private FormalGameHandler(@NotNull MinecraftServer server, FormalGameTimer timer, int fieldRadius) {
        this.server = server;
        this.timer = timer;
        this.fieldRadius = fieldRadius;
        this.firstStageTime = this.timer.getGameTime() / 5;
        this.transitionStageTime = (int) (this.firstStageTime * 2.5);
        this.finalStageTime = this.transitionStageTime + this.firstStageTime;
    }

    public void startFormalGame() {
        LogManager.getLogger().info("Formal Game Started!");
        MessageHandler.sendToAll(new SFormalGameMessage(true));
        this.timer.startTimer();
        this.showStageTitle(1);
    }

    public void stopFormalGame() {
        LogManager.getLogger().info("Formal Game Stopped!");
        MessageHandler.sendToAll(new SFormalGameMessage(false));
        instance = null;
    }

    public void showStageTitle(int stage) {
        for (ServerPlayerEntity player : this.server.getPlayerList().getPlayers()) {
            player.connection.send(new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("game.formal.stage_" + stage + "_title")));
            player.connection.send(new STitlePacket(STitlePacket.Type.SUBTITLE, new TranslationTextComponent("game.formal.stage_" + stage + "_subtitle")));
        }
    }

    public static void createNewInstance(MinecraftServer server, int fieldRadius, int gameTime) {
        instance = new FormalGameHandler(server, new FormalGameTimer(gameTime), fieldRadius);
    }

    public static void createNewInstance(MinecraftServer server, int fieldRadius, int gameTime, int tickTime) {
        instance = new FormalGameHandler(server, new FormalGameTimer(gameTime, tickTime), fieldRadius);
    }

    public static @Nullable FormalGameHandler getInstance() {
        return instance;
    }

    public FormalGameTimer getTimer() {
        return this.timer;
    }

    public int getFieldRadius() {
        return this.fieldRadius;
    }

    public @NotNull MinecraftServer getServer() {
        return this.server;
    }

    public int getFirstStageTime() {
        return this.firstStageTime;
    }

    public int getTransitionStageTime() {
        return this.transitionStageTime;
    }

    public int getFinalStageTime() {
        return this.finalStageTime;
    }
}
