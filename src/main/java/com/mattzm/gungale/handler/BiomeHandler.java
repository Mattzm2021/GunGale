package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.world.gen.ModOreGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class BiomeHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(final @NotNull BiomeLoadingEvent event) {
        ModOreGeneration.generateOres(event.getGeneration());
    }
}
