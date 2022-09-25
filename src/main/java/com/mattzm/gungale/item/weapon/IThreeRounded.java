package com.mattzm.gungale.item.weapon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IThreeRounded {
    boolean isFiring(@NotNull PlayerEntity player, @NotNull ItemStack stack);

    default float get1stModifier() {
        return 0.1f;
    }

    default float get2ndModifier() {
        return 0.3f;
    }

    default float get3rdModifier() {
        return 0.6f;
    }
}
