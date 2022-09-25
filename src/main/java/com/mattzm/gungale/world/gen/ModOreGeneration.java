package com.mattzm.gungale.world.gen;

import com.mattzm.gungale.world.gen.feature.ModFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import org.jetbrains.annotations.NotNull;

public class ModOreGeneration {
    public static void generateOres(final @NotNull BiomeGenerationSettingsBuilder builder) {
        builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.ORE_SALTPETER);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ModFeatures.ORE_SULFUR);
    }
}
