package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.command.impl.GunGaleCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class CommandHandler {
    @SubscribeEvent
    public static void onCommandRegister(@NotNull final RegisterCommandsEvent event) {
        GunGaleCommand.register(event.getDispatcher());
    }
}
