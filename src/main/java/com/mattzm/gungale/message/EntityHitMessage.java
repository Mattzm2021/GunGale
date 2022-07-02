package com.mattzm.gungale.message;

import net.minecraft.network.PacketBuffer;

public class EntityHitMessage {
    public static class ToClient {
        public static final byte ID = 3;
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

    public static class ToServer {
        public static final byte ID = 4;
        protected final int entityID;
        protected final double distanceSqr;

        public ToServer(int entityID, double distanceSqr) {
            this.entityID = entityID;
            this.distanceSqr = distanceSqr;
        }

        public static ToServer decode(PacketBuffer buffer) {
            return new ToServer(buffer.readInt(), buffer.readDouble());
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(this.entityID);
            buffer.writeDouble(this.distanceSqr);
        }
    }
}
