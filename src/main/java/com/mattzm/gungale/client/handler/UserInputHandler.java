package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.nbt.ADSNBT;
import com.mattzm.gungale.client.settings.ModGameSettings;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.VariableOpticItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.play.*;
import com.mattzm.gungale.nbt.stack.FireModeNBT;
import com.mattzm.gungale.nbt.stack.OpticNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunGale.MOD_ID)
public class UserInputHandler {
    @SubscribeEvent
    public static void onUserInput(InputEvent event) {
        if (Minecraft.getInstance().player == null) return;
        PlayerEntity player = Minecraft.getInstance().player;
        ModPlayerInventory inventory = ModPlayerInventory.get(player);
        
        if (ModGameSettings.KEY_RELOAD.consumeClick()) {
            AbstractWeaponItem.checkIfCanReload(player);
        }

        if (ModGameSettings.KEY_INVENTORY.consumeClick()) {
            MessageHandler.sendToServer(new COpenInventoryMessage(COpenInventoryMessage.Action.OPEN_FULL));
        }

        if (ModGameSettings.KEY_SWAP.consumeClick()) {
            if (!inventory.status) {
                MessageHandler.sendToServer(new CChangeInventoryMessage(true));
            } else {
                MessageHandler.sendToServer(new CChangeInventoryMessage(false));
            }
        }

        if (ModGameSettings.KEY_CHANGE_WEAPON.consumeClick()) {
            if (inventory.status) {
                ItemStack stack;

                if (inventory.selected == 0) {
                    MessageHandler.sendToServer(new CChangeInventoryMessage(6));
                    stack = inventory.getItem(6);
                } else {
                    MessageHandler.sendToServer(new CChangeInventoryMessage(0));
                    stack = inventory.getItem(0);
                }

                ADSNBT.setFov(player, Minecraft.getInstance().options.fov);
                if (!stack.isEmpty()) {
                    ADSNBT.setSpeed(player, ((AbstractWeaponItem) stack.getItem()).getCertainAdsSpeed());
                }
            }
        }

        if (ModGameSettings.KEY_SWAP_FIRE_MODE.consumeClick()) {
            ItemStack stack = inventory.getSelected();
            if (FireModeNBT.get(stack) == 0) {
                FireModeNBT.setFromClient(stack, inventory.selected, 1);
            } else {
                FireModeNBT.setFromClient(stack, inventory.selected, 0);
            }
        }

        if (ModGameSettings.KEY_SWAP_OPTIC.consumeClick()) {
            ItemStack stack = inventory.getItem(inventory.selected + 3);
            if (stack.getItem() instanceof VariableOpticItem) {
                VariableOpticItem opticItem = (VariableOpticItem) stack.getItem();
                ADSNBT.setFov(player, Minecraft.getInstance().options.fov);
                if (OpticNBT.get(inventory.getSelected()) == opticItem.getSmallerInt()) {
                    OpticNBT.setFromClient(inventory.getSelected(), inventory.selected, opticItem.getLargerInt());
                } else {
                    OpticNBT.setFromClient(inventory.getSelected(), inventory.selected, opticItem.getSmallerInt());
                }
            }
        }

        if (ModGameSettings.KEY_SELECT_WEAPON_A.consumeClick()) {
            if (inventory.selected == 6) {
                ItemStack stack = inventory.getItem(0);
                MessageHandler.sendToServer(new CChangeInventoryMessage(0));
                ADSNBT.setFov(player, Minecraft.getInstance().options.fov);
                if (!stack.isEmpty()) {
                    ADSNBT.setSpeed(player, ((AbstractWeaponItem) stack.getItem()).getCertainAdsSpeed());
                }
            }
        }

        if (ModGameSettings.KEY_SELECT_WEAPON_B.consumeClick()) {
            if (inventory.selected == 0) {
                ItemStack stack = inventory.getItem(6);
                MessageHandler.sendToServer(new CChangeInventoryMessage(6));
                ADSNBT.setFov(player, Minecraft.getInstance().options.fov);
                if (!stack.isEmpty()) {
                    ADSNBT.setSpeed(player, ((AbstractWeaponItem) stack.getItem()).getCertainAdsSpeed());
                }
            }
        }

        if (ModGameSettings.KEY_DROP.consumeClick()) {
            if (!inventory.getSelected().isEmpty()) {
                player.drop(inventory.getSelected(), false, true);
                MessageHandler.sendToServer(new CDropItemMessage(inventory.getSelected()));
                inventory.setItem(inventory.selected, ItemStack.EMPTY);
                MessageHandler.sendToServer(new CChangeInventoryMessage(inventory, CChangeInventoryMessage.Action.ITEMS));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            ModPlayerInventory inventory = ModPlayerInventory.get(player);
            if (inventory.status) {
                event.setCanceled(true);
                if (player.isSpectator()) {
                    if (Minecraft.getInstance().gui.getSpectatorGui().isMenuActive()) {
                        Minecraft.getInstance().gui.getSpectatorGui().onMouseScrolled(-event.getScrollDelta());
                    } else {
                        float flyingSpeed = MathHelper.clamp(player.abilities.getFlyingSpeed() + (float) event.getScrollDelta() * 0.005F, 0.0F, 0.2F);
                        player.abilities.setFlyingSpeed(flyingSpeed);
                    }
                } else {
                    inventory.swapPaint();
                }
            }
        }
    }
}
