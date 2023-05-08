package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.client.nbt.FireNBT;
import com.mattzm.gungale.client.nbt.tick.CoolDownNBT;
import com.mattzm.gungale.property.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAutoWeaponItem extends AbstractWeaponItem {
    protected AbstractAutoWeaponItem(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Override
    public boolean canFire(PlayerEntity player, ItemStack stack) {
        return haveFireAbility(player, stack) && !CoolDownNBT.hasStart(player) && FireNBT.onFire(player);
    }
}
