package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StockItem extends AttachmentItem {
    public final int level;

    public StockItem(int level) {
        this.level = level;
    }

    public int getPrecisionIncrease(@NotNull ItemStack stack) {
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (this.level == 1) {
            return item.recoilProperty.stockLevel1;
        } else if (this.level == 2) {
            return item.recoilProperty.stockLevel2;
        } else {
            return item.recoilProperty.stockLevel3;
        }
    }
}
