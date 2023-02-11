package com.mattzm.gungale.property;

public class ReloadProperty {
    public final int fullReloadSpeed;
    public final int tacReloadSpeed;

    public ReloadProperty(int reloadSpeed) {
        this(reloadSpeed, reloadSpeed);
    }

    public ReloadProperty(int fullReloadSpeed, int tacReloadSpeed) {
        this.fullReloadSpeed = fullReloadSpeed;
        this.tacReloadSpeed = tacReloadSpeed;
    }
}
