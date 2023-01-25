package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.property.*;
import org.jetbrains.annotations.NotNull;

public class R301Carbine extends AbstractAutoWeaponItem {
    public R301Carbine(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Override
    public @NotNull Status getMag() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getBarrel() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getStock() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getOptic() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getHopUp() {
        return Status.FALSE;
    }

    @Override
    public @NotNull AmmoType getBullet() {
        return AmmoType.LIGHT;
    }
}
