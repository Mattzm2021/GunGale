package com.mattzm.gungale.message;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;

public enum DamageType {
    HEAD,
    BODY,
    LEGS;

    public float getDamage(AbstractWeaponItem item) {
        if (this == HEAD) {
            return item.getHeadDamage();
        } else if (this == BODY) {
            return item.getBodyDamage();
        } else {
            return item.getLegsDamage();
        }
    }

    public static DamageType getHitPart(@NotNull EntityRayTraceResult result) {
        PlayerEntity player = (PlayerEntity) result.getEntity();
        AxisAlignedBB bb = player.getBoundingBox().expandTowards(0.0, 0.2, 0.0);
        Vector3d position = result.getLocation();
        if (position.y > bb.maxY - 0.5) {
            return HEAD;
        } else if (position.y < bb.minY + 0.75) {
            return LEGS;
        } else {
            return BODY;
        }
    }
}
