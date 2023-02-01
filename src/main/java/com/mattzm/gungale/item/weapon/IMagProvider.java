package com.mattzm.gungale.item.weapon;

import org.jetbrains.annotations.Nullable;

public interface IMagProvider {
    enum Type {
        HEAVY, LIGHT, ENERGY, SNIPER, SHOTGUN
    }

    @Nullable Type getMag();
}
