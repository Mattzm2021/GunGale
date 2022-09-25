package com.mattzm.gungale.client.nbt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientNBTHelper {
    public static void checkAndSetAll(@NotNull PlayerEntity player) {
        onFireCheck(player);
    }

    private static void onFireCheck(PlayerEntity player) {
        if (FireNBT.onFire(player)) {
            FireNBT.tick(player);
        }
    }
}
