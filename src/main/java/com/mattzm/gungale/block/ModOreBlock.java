package com.mattzm.gungale.block;

import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ModOreBlock extends OreBlock {
    private final int minXpOnDrop, maxXpOnDrop;

    public ModOreBlock(float strength, int harvestLevel, int minXpOnDrop, int maxXpOnDrop) {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(strength).harvestTool(ToolType.PICKAXE).harvestLevel(harvestLevel));
        this.minXpOnDrop = minXpOnDrop;
        this.maxXpOnDrop = maxXpOnDrop;
    }

    @Override
    protected int xpOnDrop(@NotNull Random random) {
        return MathHelper.nextInt(random, this.minXpOnDrop, this.maxXpOnDrop);
    }
}
