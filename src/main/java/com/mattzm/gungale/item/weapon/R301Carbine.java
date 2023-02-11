package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.AmmoItem;
import com.mattzm.gungale.item.ModItems;
import com.mattzm.gungale.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class R301Carbine extends AbstractFlexibleAutoWeaponItem implements IAnvilReceiverAttachable {
    public R301Carbine(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Override
    public boolean getBarrel() {
        return true;
    }

    @Override
    public IMagProvider.@Nullable Type getMag() {
        return IMagProvider.Type.LIGHT;
    }

    @Override
    public IOpticProvider.@Nullable Type getOptic() {
        return IOpticProvider.Type.MIDDLE;
    }

    @Override
    public IStockProvider.@Nullable Type getStock() {
        return IStockProvider.Type.HEAVY;
    }

    @Override
    public @NotNull AmmoItem getBullet() {
        return (AmmoItem) ModItems.LIGHT_AMMO;
    }
}
