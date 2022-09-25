package com.mattzm.gungale.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.jetbrains.annotations.NotNull;

public class ModTileEntityType {

    private static <T extends TileEntity> @NotNull TileEntityType<T> registerTileEntityType(String location, @NotNull TileEntityType.Builder<T> builder) {
        TileEntityType<T> tileEntityType = builder.build(null);
        tileEntityType.setRegistryName(location);
        return tileEntityType;
    }
}
