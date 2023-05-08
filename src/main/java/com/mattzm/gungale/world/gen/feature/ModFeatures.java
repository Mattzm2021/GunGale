package com.mattzm.gungale.world.gen.feature;

import com.mattzm.gungale.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.jetbrains.annotations.NotNull;

public class ModFeatures {
    public static final ConfiguredFeature<?, ?> ORE_SALTPETER = register("ore_saltpeter", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, States.SALTPETER, 9)).range(128).squared().count(30));
    public static final ConfiguredFeature<?, ?> ORE_SULFUR = register("ore_sulfur", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, States.SULFUR, 3)).range(32).squared().count(60));

    private static final class States {
        private static final BlockState SALTPETER = ModBlocks.SALTPETER_ORE.defaultBlockState();
        private static final BlockState SULFUR = ModBlocks.SULFUR_ORE.defaultBlockState();
    }

    private static <FC extends IFeatureConfig> @NotNull ConfiguredFeature<FC, ?> register(String location, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, location, configuredFeature);
    }
}
