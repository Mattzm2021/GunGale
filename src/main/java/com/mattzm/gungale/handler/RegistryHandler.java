package com.mattzm.gungale.handler;

import com.mattzm.gungale.item.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegistryHandler {
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ModItems.P9_PISTOL);
        event.getRegistry().register(ModItems.SMG_AMMO);
        event.getRegistry().register(ModItems.BODY_ARMOR_3);
    }
}
