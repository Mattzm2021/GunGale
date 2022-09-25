package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagItem extends AttachmentItem {
    public final int level;

    public MagItem(int level) {
        this.level = level;
    }

    public int getMagazineSize(@NotNull ItemStack stack) {
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (this.level == 1) {
            return item.magProperty.level1;
        } else if (this.level == 2) {
            return item.magProperty.level2;
        } else {
            return item.magProperty.level3;
        }
    }
}
