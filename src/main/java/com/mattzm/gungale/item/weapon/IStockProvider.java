package com.mattzm.gungale.item.weapon;

import org.jetbrains.annotations.Nullable;

public interface IStockProvider {
    enum Type {
        HEAVY, LIGHT
    }

    @Nullable Type getStock();
}
