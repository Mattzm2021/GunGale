package com.mattzm.gungale.handler;

import com.mattzm.gungale.item.GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class TickHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            if (event.side == LogicalSide.SERVER) {
                for (ItemStack stack : player.inventory.items) {
                    if (stack.getItem() instanceof GunItem) {
                        GunItem item = (GunItem) stack.getItem();
                        item.onServerTick(player, stack);
                    }
                }
            }
        }
    }
}
