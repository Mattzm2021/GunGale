package com.mattzm.gungale.message;

import net.minecraft.network.PacketBuffer;

public class StackMessage {
    public static class ToServer {
        public static final byte ID = 8;
        protected final int stackID;
        protected final int value;
        protected final boolean flag;

        public ToServer(int stackID, int value, boolean flag) {
            this.stackID = stackID;
            this.value = value;
            this.flag = flag;
        }

        public static ToServer decode(PacketBuffer buffer) {
            return new ToServer(buffer.readInt(), buffer.readInt(), buffer.readBoolean());
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(this.stackID);
            buffer.writeInt(this.value);
            buffer.writeBoolean(this.flag);
        }
    }
}
