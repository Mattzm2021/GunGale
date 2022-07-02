package com.mattzm.gungale.client.settings;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.event.FireEvent;
import com.mattzm.gungale.event.ReloadEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

@OnlyIn(Dist.CLIENT)
public class UserInputHandler {
    @SubscribeEvent
    public static void userInputHandler(InputEvent event) {
        if (ModSettings.getModKey(0).consumeClick())
            GunGale.FORGE_EB.post(new FireEvent(LogicalSide.CLIENT, Minecraft.getInstance().player));
        if (ModSettings.getModKey(1).consumeClick())
            GunGale.FORGE_EB.post(new ReloadEvent(LogicalSide.CLIENT, Minecraft.getInstance().player));
    }
}
