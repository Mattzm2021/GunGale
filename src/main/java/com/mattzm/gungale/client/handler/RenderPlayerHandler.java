package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.object.ClientObjectHolder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID)
public class RenderPlayerHandler {
    @SubscribeEvent
    public static void onPlayerRender(final @NotNull RenderPlayerEvent.Pre event) {
        event.setCanceled(true);
        ClientObjectHolder.getInstance().getMPlayerRenderer().setModel(event.getRenderer().getModel());
        ClientObjectHolder.getInstance().getMPlayerRenderer().render((AbstractClientPlayerEntity) event.getPlayer(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight());
    }
}
