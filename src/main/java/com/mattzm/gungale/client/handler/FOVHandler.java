package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.entity.player.ClientPlayerEntityHelper;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID)
public class FOVHandler {
    @SubscribeEvent
    public static void onFOVUpdate(final @NotNull FOVUpdateEvent event) {
        event.setNewfov(ClientPlayerEntityHelper.getFieldOfViewModifier((AbstractClientPlayerEntity) event.getEntity()));
    }
}
