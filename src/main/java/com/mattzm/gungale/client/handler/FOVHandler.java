package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID)
public class FOVHandler {
    @SubscribeEvent
    public static void onFOVUpdate(final @NotNull FOVUpdateEvent event) {
        LogManager.getLogger().debug(event.getFov());
    }
}
