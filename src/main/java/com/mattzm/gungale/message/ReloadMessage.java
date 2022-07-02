package com.mattzm.gungale.message;

import net.minecraft.network.PacketBuffer;

public class ReloadMessage {
    public static class ToServer {
        public static final byte ID = 7;

        public static ToServer decode(PacketBuffer buffer) {
            return new ToServer();
        }

        public void encode(PacketBuffer buffer) {}
    }
}
