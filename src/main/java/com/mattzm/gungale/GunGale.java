package com.mattzm.gungale;

import com.mattzm.gungale.client.object.ClientObjectHolder;
import com.mattzm.gungale.message.play.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(GunGale.MOD_ID)
public class GunGale {
    public static final String MOD_ID = "gungale";

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(MessageHandler::setupAll);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(final @NotNull FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Minecraft minecraft = event.getMinecraftSupplier().get();
            new ClientObjectHolder(minecraft).setup();
        });
    }
}
