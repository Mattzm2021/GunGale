package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.AmmoItem;
import org.jetbrains.annotations.NotNull;

public interface IReloadable {
    @NotNull AmmoItem getBullet();
}
