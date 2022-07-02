package com.mattzm.gungale.client.settings;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.IKeyConflictContext;

public enum ModKeyConflictContext implements IKeyConflictContext {
    ON_SHOOTING {
        @Override
        public boolean isActive() {
            if (Minecraft.getInstance().player == null) {
                GunGale.LOGGER.error("GunItem cannot provide a ClientPlayerEntity");
                return false;
            } else return Minecraft.getInstance().player.getMainHandItem().getItem() instanceof GunItem;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    OFF_SHOOTING {
        @Override
        public boolean isActive() {
            return !ON_SHOOTING.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    }
}
