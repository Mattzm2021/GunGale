package com.mattzm.gungale.item.weapon;

import org.jetbrains.annotations.NotNull;

public interface IAttachable {
    enum Status {
        TRUE, FALSE;

        public boolean get() {
            return this == TRUE;
        }
    }

    @NotNull Status getMag();

    @NotNull Status getBarrel();

    @NotNull Status getStock();

    @NotNull Status getOptic();

    @NotNull Status getHopUp();
}
