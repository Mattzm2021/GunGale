package com.mattzm.gungale.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class BodyArmorItem extends ArmorItem {
    public final float strength;

    public BodyArmorItem(float strength) {
        super(ArmorMaterial.NETHERITE, EquipmentSlotType.CHEST, new Properties().tab(ModItemGroup.TAB_MOD));

        this.strength = strength;
    }
}
