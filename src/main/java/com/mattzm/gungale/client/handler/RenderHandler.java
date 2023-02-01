package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.object.ClientObjectHolder;
import com.mattzm.gungale.client.renderer.ModFirstPersonRenderer;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.OpticItem;
import com.mattzm.gungale.item.RestoreItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.nbt.stack.OpticNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID)
public class RenderHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onRenderOverlay$Pre(@NotNull RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS) {
            if (Minecraft.getInstance().options.fov < 70) {
                event.setCanceled(true);
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null && !ModPlayerInventory.get(player).getSelected().isEmpty()) {
                    for (int i = 0; i < OpticItem.MAGNIFICATION_FOV.length; i++) {
                        if (Minecraft.getInstance().options.fov == OpticItem.MAGNIFICATION_FOV[i] && OpticNBT.get(ModPlayerInventory.get(player).getSelected()) == i) {
                            int index = ModPlayerInventory.get(player).selected + 3;
                            ItemStack stack = ModPlayerInventory.get(player).getItem(index);
                            OpticItem item = stack.isEmpty() ? null : (OpticItem) stack.getItem();
                            ClientObjectHolder.getInstance().getMIngameGui().renderAds(item, event.getMatrixStack());
                        }
                    }
                }
            }
        } else if (event.getType() == ElementType.HOTBAR) {
            event.setCanceled(true);
            if (Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR) {
                Minecraft.getInstance().gui.getSpectatorGui().renderHotbar(event.getMatrixStack(), event.getPartialTicks());
            } else {
                ClientObjectHolder.getInstance().getMIngameGui().renderHotbar(event.getPartialTicks(), event.getMatrixStack());
            }
        } else if (event.getType() == ElementType.ALL) {
            if (Minecraft.getInstance().options.heldItemTooltips) {
                Minecraft.getInstance().options.heldItemTooltips = false;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay$Post(RenderGameOverlayEvent.Post event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            LOGGER.error("RenderGameOverlayEvent cannot provide a PlayerEntity!");
        } else if (event.getType() == ElementType.TEXT) {
            ClientObjectHolder.getInstance().getMIngameGui().render(event.getMatrixStack(), event.getPartialTicks());
            if (ModPlayerInventory.get(player).getSelected().getItem() instanceof AbstractWeaponItem) {
                ClientObjectHolder.getInstance().getMIngameGui().renderBulletText(event.getMatrixStack());
                ClientObjectHolder.getInstance().getMIngameGui().renderReloadTickText(event.getMatrixStack());
            } else if (player.getMainHandItem().getItem() instanceof RestoreItem) {
                ClientObjectHolder.getInstance().getMIngameGui().renderRestoreTickText(event.getMatrixStack());
            }

            ClientObjectHolder.getInstance().getMIngameGui().renderEvolveAmountText(event.getMatrixStack());
            if (ClientObjectHolder.getInstance().getMIngameGui().showBorderText) {
                ClientObjectHolder.getInstance().getMIngameGui().renderBorderText(event.getMatrixStack());
            }
        } else if (event.getType() == ElementType.ALL) {
            if (!Minecraft.getInstance().options.hideGui) {
                if (Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.getPlayerMode() != GameType.SPECTATOR) {
                    ClientObjectHolder.getInstance().getMIngameGui().renderSelectedItemName(event.getMatrixStack());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(@NotNull RenderHandEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        ModFirstPersonRenderer itemInHandRenderer = ClientObjectHolder.getInstance().getMItemInHandRenderer();
        if (player != null) {
            event.setCanceled(true);
            itemInHandRenderer.renderHandsWithItems(event.getPartialTicks(), event.getMatrixStack(), (IRenderTypeBuffer.Impl) event.getBuffers(), player, event.getLight());
        }
    }
}
