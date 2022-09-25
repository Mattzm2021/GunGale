package com.mattzm.gungale.block;

import com.mattzm.gungale.inventory.container.WeaponBenchContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class WeaponBenchBlock extends Block {
    public WeaponBenchBlock() {
        super(Properties.of(Material.STONE).strength(5.0f));
    }

    public SimpleNamedContainerProvider getContainerProvider(World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((counter, inventory, player) ->
                new WeaponBenchContainer(counter, inventory, IWorldPosCallable.create(world, pos)), new TranslationTextComponent("container.weapon_bench"));
    }
}
