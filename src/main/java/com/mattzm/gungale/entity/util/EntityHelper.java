package com.mattzm.gungale.entity.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EntityHelper {
    public static ModifiableAttributeInstance getSpeedAttributeInstance(@NotNull LivingEntity entity) {
        return Objects.requireNonNull(entity.getAttribute(Attributes.MOVEMENT_SPEED));
    }

    public static void stopSprinting(@NotNull PlayerEntity player) {
        player.setSprinting(false);
    }
}
