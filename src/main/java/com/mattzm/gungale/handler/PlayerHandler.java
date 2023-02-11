package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.block.WeaponBenchBlock;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.AmmoItem;
import com.mattzm.gungale.item.AttachmentItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.util.nbt.ModDataManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class PlayerHandler {
    @SubscribeEvent
    public static void onItemPickup(@NotNull EntityItemPickupEvent event) {
        PlayerEntity player = event.getPlayer();
        ModPlayerInventory inventory = ModPlayerInventory.get(player);
        ItemEntity entity = event.getItem();
        ItemStack stack = entity.getItem();
        Item item = stack.getItem();
        int count = stack.getCount();

        if (item instanceof AmmoItem || item instanceof AttachmentItem || item instanceof AbstractWeaponItem) {
            event.setCanceled(true);
            if (!entity.hasPickUpDelay() && (entity.getOwner() == null || entity.lifespan - entity.getAge() <= 200 || entity.getOwner().equals(player.getUUID()))) {
                if (item instanceof AmmoItem) {
                    if (!inventory.add(stack) && count > 0) {
                        return;
                    }
                } else if (item instanceof AttachmentItem) {
                    if (!inventory.checkAndSetAttachment(stack) && count > 0) {
                        return;
                    }
                } else {
                    if (!inventory.checkAndSetWeapon(stack) && count > 0) {
                        return;
                    }
                }

                player.take(entity, count);
                if (stack.isEmpty()) {
                    entity.remove();
                    stack.setCount(count);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), count);
                player.onItemPickup(entity);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoading(PlayerEvent.@NotNull LoadFromFile event) throws IOException {
        File file = event.getPlayerFile("mdat");
        CompoundNBT data = new CompoundNBT();
        if (file.exists() && file.isFile()) {
            data = CompressedStreamTools.readCompressed(file);
        }

        ModDataManager.loadInventoryData(data.copy(), event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerSaving(PlayerEvent.@NotNull SaveToFile event) throws IOException {
        File file = event.getPlayerFile("mdat");
        CompoundNBT data = new CompoundNBT();
        ModDataManager.saveInventoryData(data, event.getPlayer());
        CompressedStreamTools.writeCompressed(data, file);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if (block == Blocks.CRAFTING_TABLE) {
            if (event.getSide() == LogicalSide.SERVER) {
                event.setCanceled(true);
                event.getPlayer().openMenu(ModPlayerInventory.get(event.getPlayer()).getMenuProviderForCrafting(event.getWorld(), event.getPos()));
                event.getPlayer().awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            }
        } else if (block == ModBlocks.WEAPON_BENCH) {
            event.setCanceled(true);
            if (event.getSide() == LogicalSide.SERVER && event.getHand() == Hand.MAIN_HAND) {
                event.getPlayer().openMenu(((WeaponBenchBlock) block).getContainerProvider(event.getWorld(), event.getPos()));
            }
        }
    }
}
