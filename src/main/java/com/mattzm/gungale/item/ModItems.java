package com.mattzm.gungale.item;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.block.ModBlocks;
import com.mattzm.gungale.item.weapon.*;
import com.mattzm.gungale.property.*;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;

@ObjectHolder(GunGale.MOD_ID)
public class ModItems {
    public static final Item HEAVY_ROUNDS = register("heavy_rounds", new HeavyRoundsItem(90));
    public static final Item SHOTGUN_SHELLS = register("shotgun_shells", new ShotgunShellsItem(24));
    public static final Item SNIPER_AMMO = register("sniper_ammo", new SniperAmmoItem(32));
    public static final Item LIGHT_AMMO = register("light_ammo", new LightAmmoItem(90));
    public static final Item ENERGY_AMMO = register("energy_ammo", new EnergyAmmoItem(90));

    public static final Item FLATLINE = register("flatline", new Flatline(
            new BasicProperty(3.6f, 600, 45, 22, 184.4f),
            new DamageProperty(6.4f, 3.6f, 2.8f),
            new RecoilProperty(36, 13, 45, 7, 11, 16),
            new ReloadProperty(56, 40),
            new ADSProperty(5), new MagProperty(27, 31, 34), 30));
    public static final Item R301_CARBINE = register("r301_carbine", new R301Carbine(
            new BasicProperty(2.8f, 816, 55, 20, 190.0f),
            new DamageProperty(5.0f, 2.8f, 2.4f),
            new RecoilProperty(44, 16, 55, 8, 14, 17, 8, 14, 19),
            new ReloadProperty(50, 32),
            new ADSProperty(5), new MagProperty(22, 28, 32), 30));
    public static final Item HEMLOK_BURST_AR = register("hemlok_burst_ar", new HemlokBurstAR(
            new BasicProperty(4.4f, 924, 38, 21, 244.8f),
            new DamageProperty(7.8f, 4.4f, 3.4f),
            new RecoilProperty(30, 11, 38, 6, 10, 11, 6, 10, 13),
            new ReloadProperty(52, 40),
            new ADSProperty(5), new MagProperty(27, 30, 36), 30));
    public static final Item HAVOC_RIFLE = register("havoc_rifle", new HavocRifle(
            new BasicProperty(3.6f, 672, 35, 26, 197.6f),
            new DamageProperty(6.4f, 3.6f, 3.0f),
            new RecoilProperty(28, 10, 35, 5, 9, 12),
            new ReloadProperty(58, 58),
            new ADSProperty(5), new MagProperty(30, 34, 38), 30));

    public static final Item EVO_SHIELD_1 = register("evo_shield_1", new GearItem(ArmorMaterial.LEATHER, 10, 30));
    public static final Item EVO_SHIELD_2 = register("evo_shield_2", new GearItem(ArmorMaterial.IRON, 15, 110));
    public static final Item EVO_SHIELD_3 = register("evo_shield_3", new GearItem(ArmorMaterial.DIAMOND, 20, 180));
    public static final Item EVO_SHIELD_4 = register("evo_shield_4", new GearItem(ArmorMaterial.NETHERITE, 25, 0));
    public static final Item SHIELD_CELL = register("shield_cell", new RestoreItem(60, 5, RestoreItem.Type.SHIELD, 4));
    public static final Item SHIELD_BATTERY = register("shield_battery", new RestoreItem(100, -1, RestoreItem.Type.SHIELD, 2));
    public static final Item SYRINGE = register("syringe", new RestoreItem(100, 5, RestoreItem.Type.HEALTH, 4));
    public static final Item MED_KIT = register("med_kit", new RestoreItem(160, -1, RestoreItem.Type.HEALTH, 2));
    public static final Item PHOENIX_KIT = register("phoenix_kit", new RestoreItem(200, -1, RestoreItem.Type.MIXED, 1));

    public static final Item HEAVY_MAG_1 = register("heavy_mag_1", new MagItem(1));
    public static final Item HEAVY_MAG_2 = register("heavy_mag_2", new MagItem(2));
    public static final Item HEAVY_MAG_3 = register("heavy_mag_3", new MagItem(3));
    public static final Item HEAVY_MAG_4 = register("heavy_mag_4", new MagItem(4));
    public static final Item BARREL_STABILIZER_1 = register("barrel_stabilizer_1", new BarrelItem(1));
    public static final Item BARREL_STABILIZER_2 = register("barrel_stabilizer_2", new BarrelItem(2));
    public static final Item BARREL_STABILIZER_3 = register("barrel_stabilizer_3", new BarrelItem(3));
    public static final Item BARREL_STABILIZER_4 = register("barrel_stabilizer_4", new BarrelItem(4));
    public static final Item STANDARD_STOCK_1 = register("standard_stock_1", new StockItem(1));
    public static final Item STANDARD_STOCK_2 = register("standard_stock_2", new StockItem(2));
    public static final Item STANDARD_STOCK_3 = register("standard_stock_3", new StockItem(3));
    public static final Item HCOG_CLASSIC = register("hcog_classic", new OpticItem(1));
    public static final Item HOLO = register("holo", new OpticItem(1));
    public static final Item HCOG_BRUISER = register("hcog_bruiser", new OpticItem(2));
    public static final Item VARIABLE_HOLO = register("variable_holo", new VariableOpticItem(1, 1, 2));
    public static final Item HCOG_RANGER = register("hcog_ranger", new OpticItem(3));
    public static final Item VARIABLE_AOG = register("variable_aog", new VariableOpticItem(2, 2, 4));

    public static final Item SALTPETER_ORE = register("saltpeter_ore", new BlockItem(ModBlocks.SALTPETER_ORE, new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item SULFUR_ORE = register("sulfur_ore", new BlockItem(ModBlocks.SULFUR_ORE, new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item WEAPON_BENCH = register("weapon_bench", new BlockItem(ModBlocks.WEAPON_BENCH, new Item.Properties().tab(ModItemGroup.TAB_UTIL)));

    public static final Item SALTPETER = register("saltpeter", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item SULFUR = register("sulfur", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item COMPRESSED_GUNPOWDER = register("compressed_gunpowder", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item COMPRESSED_REDSTONE = register("compressed_redstone", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item ENERGY_STORAGE = register("energy_storage", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item THREADED_IRON_INGOT = register("threaded_iron_ingot", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item STEEL_INGOT = register("steel_ingot", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item LENS = register("lens", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL)));
    public static final Item HEAVY_MAG = register("heavy_mag", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item BARREL = register("barrel", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item HANDGUARD = register("handguard", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item PISTOL_GRIP = register("pistol_grip", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item RECEIVER = register("receiver", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item REAR_SIGHT = register("rear_sight", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item FLATLINE_RATE_STABILIZER = register("flatline_rate_stabilizer", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item R301_ATTACHMENT_BOOSTER = register("r301_attachment_booster", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item HEMLOK_3_ROUND_BURST_CORE = register("hemlok_3_round_burst_core", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));
    public static final Item HAVOC_DELAYED_ENERGY_CORE = register("havoc_delayed_energy_core", new Item(new Item.Properties().tab(ModItemGroup.TAB_UTIL).stacksTo(1)));

    public static final Item TEST = register("test", new TestItem(new Item.Properties()));

    private static @NotNull Item register(String location, @NotNull Item item) {
        return item.setRegistryName(location);
    }
}
