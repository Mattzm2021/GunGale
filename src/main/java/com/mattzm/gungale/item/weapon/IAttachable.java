package com.mattzm.gungale.item.weapon;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface IAttachable {
    enum Status {
        TRUE, FALSE;

        public boolean get() {
            return this == TRUE;
        }
    }

    @Nullable Status getMag();

    @NotNull Status getBarrel();

    @Nullable Status getStock();

    @Nullable Status getOptic();

    @Nullable Status getHopUp();
}
