package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.HopUpItem;
import org.jetbrains.annotations.Nullable;

public interface IHopUpProvider {
    @Nullable HopUpItem getHopUp();
}
