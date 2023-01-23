package com.mattzm.gungale.client.entity.player;

import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientPlayerEntityHelper {
    @VanillaCode
    public static float getFieldOfViewModifier(@NotNull AbstractClientPlayerEntity player) {
        float f = 1.0F;
        if (player.abilities.flying) {
            f *= 1.1F;
        }

        f = (float) ((double) f * (((player.isSprinting() ? 0.13 : 0.1) / (double) player.abilities.getWalkingSpeed() + 1.0) / 2.0));
        if (player.abilities.getWalkingSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        if (player.isUsingItem() && player.getUseItem().getItem() == Items.BOW) {
            int i = player.getTicksUsingItem();
            float f1 = (float) i / 20.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        return f;
    }
}
