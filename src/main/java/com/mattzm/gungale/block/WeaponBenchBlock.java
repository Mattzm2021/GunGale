package com.mattzm.gungale.block;

import com.mattzm.gungale.inventory.container.WeaponBenchContainer;
import com.mattzm.gungale.util.VanillaCode;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

@VanillaCode("CraftingTableBlock")
public class WeaponBenchBlock extends Block {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.weapon_bench");

    public WeaponBenchBlock(Properties properties) {
        super(properties);
    }

    public SimpleNamedContainerProvider getContainerProvider(World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((counter, inventory, player) -> {
            return new WeaponBenchContainer(counter, inventory, IWorldPosCallable.create(world, pos));
        }, CONTAINER_TITLE);
    }
}
