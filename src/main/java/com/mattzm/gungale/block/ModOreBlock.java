package com.mattzm.gungale.block;

import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ModOreBlock extends OreBlock {
    public ModOreBlock(float strength, int harvestLevel) {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(strength).harvestTool(ToolType.PICKAXE).harvestLevel(harvestLevel));
    }

    @Override
    protected int xpOnDrop(@NotNull Random random) {
        if (this == ModBlocks.SALTPETER_ORE) {
            return MathHelper.nextInt(random, 0, 2);
        } else {
            return this == ModBlocks.SULFUR_ORE ? MathHelper.nextInt(random, 2, 5) : 0;
        }
    }
}
