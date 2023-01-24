package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HemlokBurstAR extends AbstractThreeRounded {
    public HemlokBurstAR(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Nullable
    @Override
    public Status getMag() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getBarrel() {
        return Status.TRUE;
    }

    @Nullable
    @Override
    public Status getStock() {
        return Status.TRUE;
    }

    @Nullable
    @Override
    public Status getOptic() {
        return Status.TRUE;
    }

    @Nullable
    @Override
    public Status getHopUp() {
        return Status.TRUE;
    }

    @Override
    public @NotNull AmmoType getBullet() {
        return AmmoType.HEAVY;
    }
}
