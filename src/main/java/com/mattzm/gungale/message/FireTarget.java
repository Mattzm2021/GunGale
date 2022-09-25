package com.mattzm.gungale.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public enum FireTarget {
    ENTITY,
    BLOCK;

    public Vector3d position = null;
    public LivingEntity entity = null;
    public DamageType damageType = null;
    public float damageAmplifier = 1.0f;

    public FireTarget setPosition(Vector3d position) {
        this.position = position;
        return this;
    }

    public FireTarget setEntity(LivingEntity entity) {
        this.entity = entity;
        return this;
    }

    public FireTarget setDamageType(DamageType damageType) {
        this.damageType = damageType;
        return this;
    }

    public FireTarget setDamageAmplifier(float damageAmplifier) {
        this.damageAmplifier = damageAmplifier;
        return this;
    }
}
