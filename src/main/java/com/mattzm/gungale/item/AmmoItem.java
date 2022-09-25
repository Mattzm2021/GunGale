package com.mattzm.gungale.item;

import net.minecraft.item.Item;

public class AmmoItem extends Item {
    public AmmoItem(int maxStack) {
        super(new Properties().tab(ModItemGroup.TAB_WEAPON).stacksTo(maxStack));
    }
}
