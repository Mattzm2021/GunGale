package com.mattzm.gungale.block;

import com.mattzm.gungale.GunGale;
import net.minecraft.block.Block;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;

@ObjectHolder(GunGale.MOD_ID)
public class ModBlocks {
    public static final Block SALTPETER_ORE = register("saltpeter_ore", new ModOreBlock(3.0f, 1, 0, 2));
    public static final Block SULFUR_ORE = register("sulfur_ore", new ModOreBlock(3.0f, 1, 2, 5));
    public static final Block WEAPON_BENCH = register("weapon_bench", new WeaponBenchBlock());

    private static @NotNull Block register(String location, @NotNull Block block) {
        return block.setRegistryName(location);
    }
}
