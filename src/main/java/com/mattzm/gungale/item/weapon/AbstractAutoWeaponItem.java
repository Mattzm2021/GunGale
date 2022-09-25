package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.property.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAutoWeaponItem extends AbstractWeaponItem {
    protected AbstractAutoWeaponItem(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty);
    }
}
