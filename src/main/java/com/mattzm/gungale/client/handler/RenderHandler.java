package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.item.GunItem;
import com.mattzm.gungale.nbt.BulletNBT;
import com.mattzm.gungale.nbt.ReloadNBT;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == ElementType.TEXT) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player == null) GunGale.LOGGER.error("Undefined Player in RenderGameOverlayEvent!");
            else if (player.getMainHandItem().getItem() instanceof GunItem) {
                ItemStack stack = player.getMainHandItem();
                GunItem item = (GunItem) stack.getItem();
                FontRenderer renderer = Minecraft.getInstance().font;
                String bulletT = BulletNBT.get(stack) + " / " + item.getAllBullet(player);
                String reloadT = ReloadNBT.hasStart(stack)
                        ? ModMathHelper.tickToSecond(ReloadNBT.getRealSpace(stack)) + "s"
                        : null;
                GunItem.renderBullet(event.getWindow(), renderer, player, event.getMatrixStack(), bulletT);
                if (reloadT != null) GunItem.renderReloadTick(event.getWindow(), renderer, player, event.getMatrixStack(), reloadT);
            }
        }
    }
}
