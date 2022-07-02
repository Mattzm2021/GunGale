package com.mattzm.gungale.message;

import net.minecraft.network.PacketBuffer;

public class BlockHitMessage {
    public static class ToClient {
        public static final byte ID = 1;
        protected final double x;
        protected final double y;
        protected final double z;

        public ToClient(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static ToClient decode(PacketBuffer buffer) {
            return new ToClient(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeDouble(this.x);
            buffer.writeDouble(this.y);
            buffer.writeDouble(this.z);
        }
    }

    public static class ToServer {
        public static final byte ID = 2;
        protected final double x;
        protected final double y;
        protected final double z;

        public ToServer(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static ToServer decode(PacketBuffer buffer) {
            return new ToServer(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeDouble(this.x);
            buffer.writeDouble(this.y);
            buffer.writeDouble(this.z);
        }
    }
}
