package com.mattzm.gungale.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class ModDamageSource {
    public static DamageSource gunShot(Entity entity) {
        return new EntityDamageSource("gunShot", entity);
    }
}
