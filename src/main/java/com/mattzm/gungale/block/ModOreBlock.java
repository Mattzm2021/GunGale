package com.mattzm.gungale.block;

import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@VanillaCode("OreBlock")
public class ModOreBlock extends OreBlock {
    public ModOreBlock(Properties properties) {
        super(properties);
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
