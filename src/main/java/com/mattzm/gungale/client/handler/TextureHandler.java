package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.inventory.container.AttachmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TextureHandler {
    @SubscribeEvent
    public static void onTextureStitch$Pre(final @NotNull TextureStitchEvent.Pre event) {
        event.addSprite(AttachmentSlot.INCAPABLE_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_HEAVY_MAG_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_LIGHT_MAG_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_ENERGY_MAG_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_BARREL_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_STOCK_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_OPTIC_SLOT);
        event.addSprite(AttachmentSlot.EMPTY_HOP_UP_SLOT);
    }
}
