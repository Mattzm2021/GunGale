package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.*;
import com.mattzm.gungale.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HemlokBurstAR extends AbstractThreeRounded {
    public HemlokBurstAR(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Nullable
    @Override
    public MagItem getMag() {
        return null;
    }

    @Override
    public @NotNull Status getBarrel() {
        return Status.TRUE;
    }

    @Nullable
    @Override
    public StockItem getStock() {
        return null;
    }

    @Nullable
    @Override
    public OpticItem getOptic() {
        return null;
    }

    @Nullable
    @Override
    public HopUpItem getHopUp() {
        return null;
    }

    @Override
    public @NotNull AmmoType getBullet() {
        return AmmoType.HEAVY;
    }
}
