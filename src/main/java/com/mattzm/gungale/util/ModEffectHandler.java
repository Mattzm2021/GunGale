package com.mattzm.gungale.util;

import com.mattzm.gungale.potion.ModEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import org.jetbrains.annotations.NotNull;

public class ModEffectHandler {
    public static void onServerTick(PlayerEntity player) {
        checkEnergyBurnt(player);
    }

    private static void checkEnergyBurnt(@NotNull PlayerEntity player) {
        if (player.hasEffect(ModEffects.ENERGY_BURNT)) {
            player.hurt(DamageSource.ON_FIRE, 0.5f);
        }
    }
}
