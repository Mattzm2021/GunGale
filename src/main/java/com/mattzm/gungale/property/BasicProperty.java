package com.mattzm.gungale.property;

public class BasicProperty {
    public final float damage;
    public final int range;
    public final int firingRate;
    public final int capacity;
    public final int reloadSpeed;

    public BasicProperty(float damage, int range, int rate, int capacity, int reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
        this.firingRate = rate;
        this.capacity = capacity;
        this.damage = damage;
        this.range = range;
    }
}
