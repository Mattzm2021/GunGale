package com.mattzm.gungale.message;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IModMessage {
    void encode(PacketBuffer buffer);

    void process(NetworkEvent.Context context);
}
