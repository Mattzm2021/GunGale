package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.client.nbt.FireNBT;
import com.mattzm.gungale.nbt.stack.FireModeNBT;
import com.mattzm.gungale.property.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFlexibleAutoWeaponItem extends AbstractAutoWeaponItem {
    protected AbstractFlexibleAutoWeaponItem(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Override
    public boolean canFire(PlayerEntity player, ItemStack stack) {
        if (FireModeNBT.get(stack) == 0) {
            return super.canFire(player, stack);
        } else {
            return super.canFire(player, stack) && FireNBT.onFirstTick(player);
        }
    }
}
