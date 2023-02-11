package com.mattzm.gungale.property;

public class DamageProperty {
    private final float headDamage;
    private final float legsDamage;

    public DamageProperty(float headDamage, float legsDamage) {
        this.headDamage = headDamage;
        this.legsDamage = legsDamage;
    }

    public float getHeadDamage() {
        return this.headDamage;
    }

    public float getLegsDamage() {
        return this.legsDamage;
    }
}
