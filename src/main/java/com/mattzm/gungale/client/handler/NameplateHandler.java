package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID)
public class NameplateHandler {
    @SubscribeEvent
    public static void onRenderNameplate(@NotNull RenderNameplateEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player.getTeam() == null) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
