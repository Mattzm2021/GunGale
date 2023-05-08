package com.mattzm.gungale.util;

import net.minecraft.block.Block;

/**
 * This class contains inspection methods that might be needed when dealing with risky actions,
 * in order to provide detailed information when an exception is about to be thrown,
 * thus improving debugging efficiency.
 */
public final class ErrorUtil {
    private static final String NULL_BLOCK_INFO = "Illegal uses of ModBlocks occurred before the instances are injected!";

    /**
     * Check if a block instance of GunGale is null before using it.
     *
     * @param block the block instance that is about to be inspected
     * @throws NullPointerException when the instance is null
     * @see com.mattzm.gungale.block.ModBlocks
     */
    public static void checkNotNull(Block block) {
        if (block == null) {
            throw new NullPointerException(NULL_BLOCK_INFO);
        }
    }

    /**
     * Check if an object is null.
     * @param object the object that is about to be inspected
     * @param name the display name of the object
     * @throws NullPointerException when the object is null
     */
    public static void checkNotNull(Object object, String name) {
        if (object == null) {
            throw new NullPointerException(name + " must not be null.");
        }
    }
}
