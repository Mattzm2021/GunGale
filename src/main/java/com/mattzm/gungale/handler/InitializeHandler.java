package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.handler.RenderHandler;
import com.mattzm.gungale.client.settings.UserInputHandler;
import com.mattzm.gungale.event.EventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class InitializeHandler {
    public static void setupAll() {
        setupEventBus();
        setupMod();
    }

    @OnlyIn(Dist.CLIENT)
    public static void setupClient() {
        GunGale.FORGE_EB.register(UserInputHandler.class);
        GunGale.FORGE_EB.register(RenderHandler.class);
    }

    public static void setupCommon() {
        GunGale.FORGE_EB.register(TickHandler.class);
        GunGale.FORGE_EB.register(EventHandler.class);
    }

    private static void setupEventBus() {
        GunGale.MOD_EB = FMLJavaModLoadingContext.get().getModEventBus();
        GunGale.FORGE_EB = MinecraftForge.EVENT_BUS;
    }

    private static void setupMod() {
        GunGale.MOD_EB.register(GunGale.class);
        GunGale.MOD_EB.register(RegistryHandler.class);
    }
}
