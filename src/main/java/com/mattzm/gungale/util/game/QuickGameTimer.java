package com.mattzm.gungale.util.game;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class QuickGameTimer {
    private int tickCount = 0;
    private int serverCount;
    private final int delay;
    private final int gameTime;
    private final int firstStageTime;
    private boolean isActive = false;
    private final MinecraftServer server;

    public QuickGameTimer(@NotNull MinecraftServer server, int gameTime, int firstStageTime) {
        this.server = server;
        this.serverCount = server.getTickCount();
        this.delay = this.serverCount;
        this.gameTime = gameTime;
        this.firstStageTime = firstStageTime;
    }

    public void startTimer() {
        this.isActive = true;
    }

    public void tick() {
        if (!this.isActive) {
            return;
        }

        if (this.tickCount == this.firstStageTime) {
            QuickGameHandler.showStageTitle(4, this.server);
            this.server.overworld().getWorldBorder().lerpSizeBetween(this.server.overworld().getWorldBorder().getSize(), 1.0, (this.gameTime - this.firstStageTime) * 50L);
        }

        this.serverCount = this.server.getTickCount();
        this.tickCount = this.serverCount - this.delay;
    }
}
