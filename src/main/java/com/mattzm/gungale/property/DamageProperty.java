package com.mattzm.gungale.property;

import com.mattzm.gungale.item.BodyArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class DamageProperty {
    public final float stage1;
    public final float stage2;
    public final float discount1;
    public final float discount2;
    public final float discount3;
    
    private DamageProperty(float stage1, float stage2, float discount1, float discount2, float discount3) {
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.discount1 = discount1;
        this.discount2 = discount2;
        this.discount3 = discount3;
    }
    
    public static DamageProperty create2To3(float stage1, float stage2, float discount1, float discount2, float discount3) {
        return new DamageProperty(stage1, stage2, discount1, discount2, discount3);
    }

    public float getRealDamage(float damage, double distanceSqr, LivingEntity entity) {
        double distance = Math.sqrt(distanceSqr);
        float discount = entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof BodyArmorItem
                ? 1 - ((BodyArmorItem) entity.getItemBySlot(EquipmentSlotType.CHEST).getItem()).strength : 1.0f;

        damage -= this.discount1 * Math.min(distance, this.stage1);
        if (distance <= this.stage1) return Math.max(damage * discount, 0.1f);
        distance -= this.stage1;
        damage -= this.discount2 * Math.min(distance, this.stage2);
        if (distance <= this.stage2) return Math.max(damage * discount, 0.1f);
        distance -= this.stage2;
        damage -= this.discount3 * distance;
        return Math.max(damage * discount, 0.1f);
    }
}
