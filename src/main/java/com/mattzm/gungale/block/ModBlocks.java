package com.mattzm.gungale.block;

import com.mattzm.gungale.GunGale;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

/**
 * This class holds all the custom block types of GunGale.
 * After the registration event fires
 * {@link com.mattzm.gungale.handler.RegistryHandler#onRegisterBlock(RegistryEvent.Register)},
 * fields will be injected by
 * {@link net.minecraftforge.registries.ObjectHolderRegistry}
 * according to their lowercase names and the registry names passed by the
 * {@link Block#setRegistryName(String)} method.
 * Notice that these instances do not represent real blocks in a game world.
 * Instead, they are used to distinguish block types of different BlockStates,
 * which also means that you should not create additional instances in the rest of the code.
 * Make sure that usages should only occur after the instances are injected,
 * otherwise a {@link NullPointerException} might be thrown.
 *
 * @see net.minecraft.block.Blocks
 * @see com.mattzm.gungale.util.ErrorUtil#checkNotNull(Block)
 */
@ObjectHolder(GunGale.MOD_ID)
public final class ModBlocks {
    public static final Block SALTPETER_ORE = null;
    public static final Block SULFUR_ORE = null;
    public static final Block WEAPON_BENCH = null;
}
