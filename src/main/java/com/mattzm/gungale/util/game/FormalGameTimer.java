package com.mattzm.gungale.util.game;

public class FormalGameTimer {
    private final int gameTime;
    private int tickTime = 0;
    private boolean isActive = false;

    public FormalGameTimer(int gameTime) {
        this.gameTime = gameTime;
    }

    public FormalGameTimer(int gameTime, int tickTime) {
        this.gameTime = gameTime;
        this.tickTime = tickTime;
        this.isActive = true;
    }

    public void startTimer() {
        this.tickTime = 0;
        this.isActive = true;
    }

    public void tick() {
        if (this.isActive) {
            if (this.tickTime < this.gameTime) {
                this.tickTime++;
            } else {
                this.stopTimer();
            }
        } else if (this.tickTime != 0) {
            this.tickTime = 0;
        }
    }

    private void stopTimer() {
        this.isActive = false;
        this.tickTime = 0;
        if (FormalGameHandler.getInstance() != null) {
            FormalGameHandler.getInstance().stopFormalGame();
        }
    }

    public int getTickTime() {
        return this.tickTime;
    }

    public int getGameTime() {
        return this.gameTime;
    }
}
