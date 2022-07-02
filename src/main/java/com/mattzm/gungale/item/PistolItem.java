package com.mattzm.gungale.item;

import com.mattzm.gungale.property.BasicProperty;
import com.mattzm.gungale.property.DamageProperty;
import com.mattzm.gungale.property.RecoilProperty;
import net.minecraft.item.Item;

public class PistolItem extends GunItem {
    protected PistolItem(BasicProperty basicProperty, DamageProperty damageProperty, RecoilProperty recoilProperty) {
        super(basicProperty, damageProperty, recoilProperty);
    }

    @Override
    protected boolean isCorrectBullet(Item item) {
        return item instanceof SMGAmmoItem;
    }
}
