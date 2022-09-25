package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.inventory.container.ModContainerType;
import com.mattzm.gungale.item.ModItems;
import com.mattzm.gungale.item.crafting.ModRecipes;
import com.mattzm.gungale.potion.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    @SubscribeEvent
    public static void registerItem(@NotNull RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ModItems.FLATLINE);
        event.getRegistry().register(ModItems.R301_CARBINE);
        event.getRegistry().register(ModItems.HEMLOK_BURST_AR);
        event.getRegistry().register(ModItems.HAVOC_RIFLE);

        event.getRegistry().register(ModItems.HEAVY_ROUNDS);
        event.getRegistry().register(ModItems.SHOTGUN_SHELLS);
        event.getRegistry().register(ModItems.SNIPER_AMMO);
        event.getRegistry().register(ModItems.LIGHT_AMMO);
        event.getRegistry().register(ModItems.ENERGY_AMMO);
        event.getRegistry().register(ModItems.EVO_SHIELD_1);
        event.getRegistry().register(ModItems.EVO_SHIELD_2);
        event.getRegistry().register(ModItems.EVO_SHIELD_3);
        event.getRegistry().register(ModItems.EVO_SHIELD_4);
        event.getRegistry().register(ModItems.SHIELD_CELL);
        event.getRegistry().register(ModItems.SHIELD_BATTERY);
        event.getRegistry().register(ModItems.SYRINGE);
        event.getRegistry().register(ModItems.MED_KIT);
        event.getRegistry().register(ModItems.PHOENIX_KIT);

        event.getRegistry().register(ModItems.HEAVY_MAG_1);
        event.getRegistry().register(ModItems.HEAVY_MAG_2);
        event.getRegistry().register(ModItems.HEAVY_MAG_3);
        event.getRegistry().register(ModItems.HEAVY_MAG_4);
        event.getRegistry().register(ModItems.BARREL_STABILIZER_1);
        event.getRegistry().register(ModItems.BARREL_STABILIZER_2);
        event.getRegistry().register(ModItems.BARREL_STABILIZER_3);
        event.getRegistry().register(ModItems.BARREL_STABILIZER_4);
        event.getRegistry().register(ModItems.STANDARD_STOCK_1);
        event.getRegistry().register(ModItems.STANDARD_STOCK_2);
        event.getRegistry().register(ModItems.STANDARD_STOCK_3);
        event.getRegistry().register(ModItems.HCOG_CLASSIC);
        event.getRegistry().register(ModItems.HOLO);
        event.getRegistry().register(ModItems.HCOG_BRUISER);
        event.getRegistry().register(ModItems.VARIABLE_HOLO);
        event.getRegistry().register(ModItems.HCOG_RANGER);
        event.getRegistry().register(ModItems.VARIABLE_AOG);

        event.getRegistry().register(ModItems.SALTPETER_ORE);
        event.getRegistry().register(ModItems.SULFUR_ORE);
        event.getRegistry().register(ModItems.WEAPON_BENCH);

        event.getRegistry().register(ModItems.SALTPETER);
        event.getRegistry().register(ModItems.SULFUR);
        event.getRegistry().register(ModItems.COMPRESSED_GUNPOWDER);
        event.getRegistry().register(ModItems.COMPRESSED_REDSTONE);
        event.getRegistry().register(ModItems.ENERGY_STORAGE);
        event.getRegistry().register(ModItems.THREADED_IRON_INGOT);
        event.getRegistry().register(ModItems.STEEL_INGOT);
        event.getRegistry().register(ModItems.LENS);
        event.getRegistry().register(ModItems.HEAVY_MAG);
        event.getRegistry().register(ModItems.BARREL);
        event.getRegistry().register(ModItems.HANDGUARD);
        event.getRegistry().register(ModItems.PISTOL_GRIP);
        event.getRegistry().register(ModItems.RECEIVER);
        event.getRegistry().register(ModItems.REAR_SIGHT);
        event.getRegistry().register(ModItems.FLATLINE_RATE_STABILIZER);
        event.getRegistry().register(ModItems.R301_ATTACHMENT_BOOSTER);
        event.getRegistry().register(ModItems.HEMLOK_3_ROUND_BURST_CORE);
        event.getRegistry().register(ModItems.HAVOC_DELAYED_ENERGY_CORE);
    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.@NotNull Register<Block> event) {
        event.getRegistry().register(ModBlocks.SALTPETER_ORE);
        event.getRegistry().register(ModBlocks.SULFUR_ORE);
        event.getRegistry().register(ModBlocks.WEAPON_BENCH);
    }

    @SubscribeEvent
    public static void registerEffect(RegistryEvent.@NotNull Register<Effect> event) {
        event.getRegistry().register(ModEffects.ENERGY_BURNT);
    }

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.@NotNull Register<TileEntityType<?>> event) {

    }

    @SubscribeEvent
    public static void registerContainer(RegistryEvent.@NotNull Register<ContainerType<?>> event) {
        event.getRegistry().register(ModContainerType.MOD_INVENTORY);
        event.getRegistry().register(ModContainerType.MOD_CRAFTING);
        event.getRegistry().register(ModContainerType.WEAPON_BENCH);
    }

    @SubscribeEvent
    public static void registerRecipe(RegistryEvent.@NotNull Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(ModRecipes.WEAPON_RECIPE);
    }
}
