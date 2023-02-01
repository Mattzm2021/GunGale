package com.mattzm.gungale.item.weapon;

import org.jetbrains.annotations.Nullable;

public interface IOpticProvider {
    enum Type {
        SHORT, MIDDLE, LONG
    }

    @Nullable Type getOptic();
}
