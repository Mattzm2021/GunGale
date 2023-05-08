package com.mattzm.gungale.property;

public class BasicProperty {
    public final float bodyDamage;
    public final int rateOfFire;
    public final int precision;
    public final int magazineSize;
    public final float effectiveRange;

    public BasicProperty(float bodyDamage, int rateOfFire, int precision, int magazineSize, float effectiveRange) {
        this.bodyDamage = bodyDamage;
        this.rateOfFire = rateOfFire;
        this.precision = precision;
        this.magazineSize = magazineSize;
        this.effectiveRange = effectiveRange;
    }
}
