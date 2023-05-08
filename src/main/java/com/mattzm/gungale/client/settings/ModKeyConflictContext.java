package com.mattzm.gungale.client.settings;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.RestoreItem;
import com.mattzm.gungale.item.weapon.AbstractFlexibleAutoWeaponItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.client.nbt.tick.RestoreNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;

@OnlyIn(Dist.CLIENT)
public enum ModKeyConflictContext implements IKeyConflictContext {
    ON_SHOOTABLE_0 {
        @Override
        public boolean isActive() {
            if (Minecraft.getInstance().player == null) {
                return false;
            } else {
                ItemStack stack = ModPlayerInventory.get(Minecraft.getInstance().player).getSelected();
                return KeyConflictContext.IN_GAME.isActive() && stack.getItem() instanceof AbstractWeaponItem;
            }
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    ON_UNSHOOTABLE_1 {
        @Override
        public boolean isActive() {
            return !ON_SHOOTABLE_0.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    ON_RESTORABLE_2 {
        @Override
        public boolean isActive() {
            if (Minecraft.getInstance().player == null) {
                return false;
            } else {
                ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
                return KeyConflictContext.IN_GAME.isActive() && stack.getItem() instanceof RestoreItem;
            }
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    ON_UNRESTORABLE_3 {
        @Override
        public boolean isActive() {
            return !ON_RESTORABLE_2.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    ON_RESTORING_4 {
        @Override
        public boolean isActive() {
            if (Minecraft.getInstance().player == null) {
                return false;
            } else {
                return KeyConflictContext.IN_GAME.isActive() && RestoreNBT.hasStart(Minecraft.getInstance().player);
            }
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    OFF_RESTORING_5 {
        @Override
        public boolean isActive() {
            return !ON_RESTORING_4.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    ON_WEAPON_SLOT_6 {
        @Override
        public boolean isActive() {
            if (Minecraft.getInstance().player == null) {
                return false;
            } else {
                return KeyConflictContext.IN_GAME.isActive() && ModPlayerInventory.get(Minecraft.getInstance().player).status;
            }
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    OFF_WEAPON_SLOT_7 {
        @Override
        public boolean isActive() {
            return !ON_WEAPON_SLOT_6.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    NONE_8 {
        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    KEY_SWAP_FIRE_MODE_9 {
        @Override
        public boolean isActive() {
            if (Minecraft.getInstance().player == null) {
                return false;
            } else {
                ItemStack stack = ModPlayerInventory.get(Minecraft.getInstance().player).getSelected();
                return KeyConflictContext.IN_GAME.isActive() && stack.getItem() instanceof AbstractFlexibleAutoWeaponItem;
            }
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    MIXED_1_7 {
        @Override
        public boolean isActive() {
            return ON_UNSHOOTABLE_1.isActive() && OFF_WEAPON_SLOT_7.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },

    MIXED_1_3_7 {
        @Override
        public boolean isActive() {
            return ON_UNSHOOTABLE_1.isActive() && ON_UNRESTORABLE_3.isActive() && OFF_WEAPON_SLOT_7.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    }
}
