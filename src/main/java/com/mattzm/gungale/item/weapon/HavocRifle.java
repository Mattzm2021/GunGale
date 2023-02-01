package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.AmmoItem;
import com.mattzm.gungale.item.HopUpItem;
import com.mattzm.gungale.item.ModItems;
import com.mattzm.gungale.message.FireTarget;
import com.mattzm.gungale.property.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public IMagProvider.@Nullable Type getMag() {
        return IMagProvider.Type.ENERGY;
    }

    @Override
    public IOpticProvider.@Nullable Type getOptic() {
        return IOpticProvider.Type.MIDDLE;
    }

    @Override
    public IStockProvider.@Nullable Type getStock() {
        return IStockProvider.Type.HEAVY;
    }

    @Override
    public @Nullable HopUpItem getHopUp() {
        return (HopUpItem) ModItems.TURBOCHARGER;
    }

    @Override
    public int getDelayTick(PlayerEntity player, ItemStack stack) {
        return this.hasTurbocharger(player, stack) ? 0 : 10;
    }

    @Override
    public @NotNull AmmoItem getBullet() {
        return (AmmoItem) ModItems.ENERGY_AMMO;
    }
}
