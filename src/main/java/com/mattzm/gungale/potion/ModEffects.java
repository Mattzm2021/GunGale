package com.mattzm.gungale.potion;

import com.mattzm.gungale.GunGale;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;

@ObjectHolder(GunGale.MOD_ID)
public class ModEffects {
    public static final Effect ENERGY_BURNT = register("energy_burnt", new ModEffect(EffectType.HARMFUL, 1422747));

    private static @NotNull Effect register(String location, @NotNull Effect effect) {
        return effect.setRegistryName(location);
    }
}
