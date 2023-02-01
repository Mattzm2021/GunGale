package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.HopUpItem;
import org.jetbrains.annotations.Nullable;

public interface IAttachable extends IBarrelProvider, IMagProvider, IOpticProvider, IStockProvider, IHopUpProvider {
    @Override
    default boolean getBarrel() {
        return false;
    }

    @Override
    default IMagProvider.@Nullable Type getMag() {
        return null;
    }

    @Override
    default IOpticProvider.@Nullable Type getOptic() {
        return null;
    }

    @Override
    default IStockProvider.@Nullable Type getStock() {
        return null;
    }

    @Override
    default @Nullable HopUpItem getHopUp() {
        return null;
    }
}