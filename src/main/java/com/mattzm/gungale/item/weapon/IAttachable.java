package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface IAttachable {
    enum Status {
        TRUE, FALSE;

        public boolean get() {
            return this == TRUE;
        }
    }

    @Nullable MagItem getMag();

    @NotNull Status getBarrel();

    @Nullable StockItem getStock();

    @Nullable OpticItem getOptic();

    @Nullable HopUpItem getHopUp();
}
