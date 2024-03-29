package com.mattzm.gungale.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.block.ModOreBlock;
import com.mattzm.gungale.block.WeaponBenchBlock;
import com.mattzm.gungale.inventory.container.ModItemContainer;
import com.mattzm.gungale.inventory.container.ModWorkbenchContainer;
import com.mattzm.gungale.inventory.container.WeaponBenchContainer;
import com.mattzm.gungale.item.*;
import com.mattzm.gungale.item.crafting.WeaponRecipe;
import com.mattzm.gungale.item.weapon.*;
import com.mattzm.gungale.potion.ModEffect;
import com.mattzm.gungale.property.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onRegisterItem(final RegistryEvent.@NotNull Register<Item> event) {
        event.getRegistry().register(new Flatline(
                new BasicProperty(3.6f, 600, 45, 20, 184.4f),
                new DamageProperty(6.4f, 2.8f),
                new RecoilProperty(36, 13, 45, 7, 11, 16),
                new ReloadProperty(62, 48),
                new ADSProperty(5.4f), new MagProperty(5, 8, 10), 50).setRegistryName("flatline"));
        event.getRegistry().register(new R301Carbine(
                new BasicProperty(2.8f, 810, 55, 18, 190.0f),
                new DamageProperty(5.0f, 2.2f),
                new RecoilProperty(44, 16, 55, 8, 14, 17, 8, 14, 19),
                new ReloadProperty(64, 48),
                new ADSProperty(5.4f), new MagProperty(2, 7, 10), 50).setRegistryName("r301_carbine"));
        event.getRegistry().register(new HemlokBurstAR( // TODO check the rof mechanics
                new BasicProperty(4.0f, 414, 38, 18, 244.8f),
                new DamageProperty(7.0f, 2.0f),
                new RecoilProperty(30, 11, 38, 6, 10, 11, 6, 10, 13),
                new ReloadProperty(57, 48),
                new ADSProperty(5.4f), new MagProperty(6, 9, 12), 50).setRegistryName("hemlok_burst_ar"));
        event.getRegistry().register(new HavocRifle(
                new BasicProperty(3.6f, 672, 35, 24, 197.6f),
                new DamageProperty(6.4f, 3.0f),
                new RecoilProperty(28, 10, 35, 5, 9, 12),
                new ReloadProperty(64),
                new ADSProperty(5.4f), new MagProperty(4, 8, 12), 50).setRegistryName("havoc_rifle"));
        event.getRegistry().register(new AmmoItem(90).setRegistryName("heavy_rounds"));
        event.getRegistry().register(new AmmoItem(24).setRegistryName("shotgun_shells"));
        event.getRegistry().register(new AmmoItem(32).setRegistryName("sniper_ammo"));
        event.getRegistry().register(new AmmoItem(90).setRegistryName("light_ammo"));
        event.getRegistry().register(new AmmoItem(90).setRegistryName("energy_ammo"));

        event.getRegistry().register(new GearItem(ArmorMaterial.LEATHER, 10, 30).setRegistryName("evo_shield_1"));
        event.getRegistry().register(new GearItem(ArmorMaterial.IRON, 15, 110).setRegistryName("evo_shield_2"));
        event.getRegistry().register(new GearItem(ArmorMaterial.DIAMOND, 20, 180).setRegistryName("evo_shield_3"));
        event.getRegistry().register(new GearItem(ArmorMaterial.NETHERITE, 25, 0).setRegistryName("evo_shield_4"));
        event.getRegistry().register(new RestoreItem(60, 5, RestoreItem.Type.SHIELD, 4).setRegistryName("shield_cell"));
        event.getRegistry().register(new RestoreItem(100, -1, RestoreItem.Type.SHIELD, 2).setRegistryName("shield_battery"));
        event.getRegistry().register(new RestoreItem(100, 5, RestoreItem.Type.HEALTH, 4).setRegistryName("syringe"));
        event.getRegistry().register(new RestoreItem(160, -1, RestoreItem.Type.HEALTH, 2).setRegistryName("med_kit"));
        event.getRegistry().register(new RestoreItem(200, -1, RestoreItem.Type.MIXED, 1).setRegistryName("phoenix_kit"));

        event.getRegistry().register(new MagItem(IMagProvider.Type.HEAVY, 1).setRegistryName("heavy_mag_1"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.HEAVY, 2).setRegistryName("heavy_mag_2"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.HEAVY, 3).setRegistryName("heavy_mag_3"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.HEAVY, 4).setRegistryName("heavy_mag_4"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.LIGHT, 1).setRegistryName("light_mag_1"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.LIGHT, 2).setRegistryName("light_mag_2"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.LIGHT, 3).setRegistryName("light_mag_3"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.LIGHT, 4).setRegistryName("light_mag_4"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.ENERGY, 1).setRegistryName("energy_mag_1"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.ENERGY, 2).setRegistryName("energy_mag_2"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.ENERGY, 3).setRegistryName("energy_mag_3"));
        event.getRegistry().register(new MagItem(IMagProvider.Type.ENERGY, 4).setRegistryName("energy_mag_4"));
        event.getRegistry().register(new BarrelItem(1).setRegistryName("barrel_stabilizer_1"));
        event.getRegistry().register(new BarrelItem(2).setRegistryName("barrel_stabilizer_2"));
        event.getRegistry().register(new BarrelItem(3).setRegistryName("barrel_stabilizer_3"));
        event.getRegistry().register(new BarrelItem(4).setRegistryName("barrel_stabilizer_4"));
        event.getRegistry().register(new StockItem(IStockProvider.Type.HEAVY, 1).setRegistryName("standard_stock_1"));
        event.getRegistry().register(new StockItem(IStockProvider.Type.HEAVY, 2).setRegistryName("standard_stock_2"));
        event.getRegistry().register(new StockItem(IStockProvider.Type.HEAVY, 3).setRegistryName("standard_stock_3"));
        event.getRegistry().register(new OpticItem(new IOpticProvider.Type[]{IOpticProvider.Type.SHORT, IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG}, 1).setRegistryName("hcog_classic"));
        event.getRegistry().register(new OpticItem(new IOpticProvider.Type[]{IOpticProvider.Type.SHORT, IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG}, 1).setRegistryName("holo"));
        event.getRegistry().register(new OpticItem(new IOpticProvider.Type[]{IOpticProvider.Type.SHORT, IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG}, 2).setRegistryName("hcog_bruiser"));
        event.getRegistry().register(new VariableOpticItem(new IOpticProvider.Type[]{IOpticProvider.Type.SHORT, IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG}, 1, 1, 2).setRegistryName("variable_holo"));
        event.getRegistry().register(new OpticItem(new IOpticProvider.Type[]{IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG}, 3).setRegistryName("hcog_ranger"));
        event.getRegistry().register(new VariableOpticItem(new IOpticProvider.Type[]{IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG}, 2, 2, 4).setRegistryName("variable_aog"));
        event.getRegistry().register(new HopUpItem().setRegistryName("turbocharger"));

        event.getRegistry().register(new BlockItem(ModBlocks.SALTPETER_ORE, new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("saltpeter_ore"));
        event.getRegistry().register(new BlockItem(ModBlocks.SULFUR_ORE, new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("sulfur_ore"));
        event.getRegistry().register(new BlockItem(ModBlocks.WEAPON_BENCH, new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("weapon_bench"));

        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("saltpeter"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("sulfur"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("compressed_gunpowder"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("compressed_redstone"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("energy_storage"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("threaded_iron_ingot"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("steel_ingot"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)).setRegistryName("lens"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("heavy_mag"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("barrel"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("handguard"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("pistol_grip"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("receiver"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("rear_sight"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("flatline_rate_stabilizer"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("r301_attachment_booster"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("hemlok_3_round_burst_core"));
        event.getRegistry().register(new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)).setRegistryName("havoc_delayed_energy_core"));
    }

    @SubscribeEvent
    public static void onRegisterBlock(final @NotNull RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new ModOreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0f).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("saltpeter_ore"));
        event.getRegistry().register(new ModOreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0f).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("sulfur_ore"));
        event.getRegistry().register(new WeaponBenchBlock(AbstractBlock.Properties.of(Material.METAL).strength(5.0f)).setRegistryName("weapon_bench"));
    }

    @SubscribeEvent
    public static void onRegisterEffect(final @NotNull RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(new ModEffect(EffectType.HARMFUL, 1422747).setRegistryName("energy_burnt"));
    }

    @SubscribeEvent
    public static void onRegisterContainerType(final @NotNull RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(new ContainerType<>(ModItemContainer::new).setRegistryName("mod_inventory"));
        event.getRegistry().register(new ContainerType<>(ModWorkbenchContainer::new).setRegistryName("mod_crafting"));
        event.getRegistry().register(new ContainerType<>(WeaponBenchContainer::new).setRegistryName("weapon_bench"));
    }

    @SubscribeEvent
    public static void onRegisterRecipeSerializer(final @NotNull RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(new WeaponRecipe.Serializer().setRegistryName("crafting_weapon"));
    }
}
