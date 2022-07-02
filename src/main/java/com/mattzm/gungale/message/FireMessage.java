package com.mattzm.gungale.message;

import net.minecraft.network.PacketBuffer;

public class FireMessage {
    public static class ToServer {
        public static final byte ID = 5;

        public static ToServer decode(PacketBuffer buffer) {
            return new ToServer();
        }

        public void encode(PacketBuffer buffer) {}
    }

    public static class ToClient {
        public static final byte ID = 6;
        protected final int entityID;

        public ToClient(int entityID) {
            this.entityID = entityID;
        }

        public static ToClient decode(PacketBuffer buffer) {
            return new ToClient(buffer.readInt());
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(this.entityID);
        }
    }
}
