package com.mattzm.gungale.block;

import com.mattzm.gungale.util.ErrorUtil;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Custom ore block type that extends the vanilla ore block mechanics.
 *
 * @see net.minecraft.block.OreBlock
 */
public class ModOreBlock extends OreBlock {
    public ModOreBlock(Properties properties) {
        super(properties);
    }

    /**
     * Determines the amount of xp released when a player properly mined an OreBlock
     *
     * @param random the random object used to provide random xp value.
     * @return the amount of xp released to the world.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected int xpOnDrop(@NotNull Random random) {
        ErrorUtil.checkNotNull(ModBlocks.SALTPETER_ORE);
        ErrorUtil.checkNotNull(ModBlocks.SULFUR_ORE);
        if (this == ModBlocks.SALTPETER_ORE) {
            return MathHelper.nextInt(random, 0, 2);
        } else {
            return this == ModBlocks.SULFUR_ORE ? MathHelper.nextInt(random, 2, 5) : 0;
        }
    }
}
