package com.mattzm.gungale;

import com.mattzm.gungale.client.settings.ModSettings;
import com.mattzm.gungale.handler.InitializeHandler;
import com.mattzm.gungale.message.MessageHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GunGale.MOD_ID)
public class GunGale {
    public static final String MOD_ID = "gungale";
    public static final Logger LOGGER = LogManager.getLogger();
    public static IEventBus MOD_EB;
    public static IEventBus FORGE_EB;

    public GunGale() {
        InitializeHandler.setupAll();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        InitializeHandler.setupClient();
        ModSettings.setupAll();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        InitializeHandler.setupCommon();
        MessageHandler.setupAll();
    }
}
