package com.mattzm.gungale.property;

public class BasicProperty {
    public final float bodyDamage;
    public final int rateOfFire;
    public final int precision;
    public final int magazineSize;
    public final float effectiveRange;

    public BasicProperty(float damage, int rateOfFire, int precision, int ammo, float range) {
        this.bodyDamage = damage;
        this.rateOfFire = rateOfFire;
        this.precision = precision;
        this.magazineSize = ammo;
        this.effectiveRange = range;
    }
}
