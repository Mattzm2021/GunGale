package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.IModMessage;
import com.mattzm.gungale.nbt.stack.FireModeNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CFireModeMessage implements IModMessage {
    private final int stackId;
    private final int value;

    public CFireModeMessage(int stackId, int value) {
        this.stackId = stackId;
        this.value = value;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(this.stackId);
        buffer.writeInt(this.value);
    }

    public static @NotNull CFireModeMessage decode(@NotNull PacketBuffer buffer) {
        return new CFireModeMessage(buffer.readInt(), buffer.readInt());
    }

    @Override
    public void process(NetworkEvent.@NotNull Context context) {
        PlayerEntity player = context.getSender();
        if (player == null) return;
        FireModeNBT.set(ModPlayerInventory.get(player).getItem(this.stackId), this.value);
    }
}
