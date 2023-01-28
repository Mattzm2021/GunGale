package com.mattzm.gungale.item.weapon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IDelayedWeapon {
    int getDelayTick(PlayerEntity player, ItemStack stack);
}
