package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class WorldHandler {
    @SubscribeEvent
    public static void onWorldSaving(WorldEvent.@NotNull Save event) {
        ServerWorld world = (ServerWorld) event.getWorld();
        if (world.dimension() == World.OVERWORLD) {

        }
    }
}
