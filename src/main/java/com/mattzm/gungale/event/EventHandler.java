package com.mattzm.gungale.event;

import com.mattzm.gungale.item.GunItem;
import com.mattzm.gungale.message.MessageHandler;
import com.mattzm.gungale.message.ReloadMessage;
import com.mattzm.gungale.nbt.BulletNBT;
import com.mattzm.gungale.nbt.ReloadNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class EventHandler {
    @SubscribeEvent
    public static void onFire(FireEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            PlayerEntity player = event.player;
            ItemStack stack = player.getMainHandItem();
            GunItem item = (GunItem) stack.getItem();
            World world = player.level;
            item.consumeFire(world, player, stack);
        } else if (event.side == LogicalSide.SERVER) {
            PlayerEntity player = event.player;
            GunItem item = (GunItem) player.getMainHandItem().getItem();
            GunItem.hurt(event.entity, player, item, event.distanceSqr);
        }
    }

    @SubscribeEvent
    public static void onReload(ReloadEvent event) {
        PlayerEntity player = event.player;
        ItemStack stack = player.getMainHandItem();
        if (event.side == LogicalSide.CLIENT) {
            GunItem item = (GunItem) stack.getItem();
            if (BulletNBT.hasSpace(stack) && !ReloadNBT.hasStart(stack) && item.getAllBullet(player) > 0)
                MessageHandler.channel.sendToServer(new ReloadMessage.ToServer());
        } else if (event.side == LogicalSide.SERVER) {
            ReloadNBT.start(stack);
        }
    }
}
