package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.item.AmmoItem;
import com.mattzm.gungale.item.ModItems;
import org.jetbrains.annotations.NotNull;

public interface IReloadable {
    enum AmmoType {
        HEAVY, SHOTGUN, SNIPER, LIGHT, ENERGY;

        public AmmoItem get() {
            if (this == HEAVY) {
                return (AmmoItem) ModItems.HEAVY_ROUNDS;
            } else if (this == SHOTGUN) {
                return (AmmoItem) ModItems.SHOTGUN_SHELLS;
            } else if (this == SNIPER) {
                return (AmmoItem) ModItems.SNIPER_AMMO;
            } else if (this == LIGHT) {
                return (AmmoItem) ModItems.LIGHT_AMMO;
            } else {
                return (AmmoItem) ModItems.ENERGY_AMMO;
            }
        }
    }

    @NotNull AmmoType getBullet();
}
