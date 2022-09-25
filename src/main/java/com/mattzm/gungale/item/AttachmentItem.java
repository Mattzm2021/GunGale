package com.mattzm.gungale.item;

import net.minecraft.item.Item;

public class AttachmentItem extends Item {
    public AttachmentItem() {
        super(new Properties().stacksTo(1).tab(ModItemGroup.TAB_WEAPON));
    }
}
