package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.FireTarget;
import com.mattzm.gungale.property.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HavocRifle extends AbstractDelayedWeaponItem {
    public HavocRifle(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    private boolean hasTurbocharger(PlayerEntity player, ItemStack stack) {
        for (int i = 0; i < 2; i++) {
            if (stack == ModPlayerInventory.get(player).getItem(i * 6)) {
                if (!ModPlayerInventory.get(player).getItem(i * 6 + 5).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected float getDamage(@NotNull FireTarget target, PlayerEntity player, ItemStack stack) {
        return super.getDamage(target, player, stack) - (this.hasTurbocharger(player, stack) ? 0.2f : 0.0f);
    }

    @Override
    public @NotNull Status getMag() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getBarrel() {
        return Status.FALSE;
    }

    @Override
    public @NotNull Status getStock() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getOptic() {
        return Status.TRUE;
    }

    @Override
    public @NotNull Status getHopUp() {
        return Status.TRUE;
    }

    @Override
    public int getDelayTick(PlayerEntity player, ItemStack stack) {
        return this.hasTurbocharger(player, stack) ? 0 : 10;
    }

    @Override
    public @NotNull AmmoType getBullet() {
        return AmmoType.ENERGY;
    }
}
