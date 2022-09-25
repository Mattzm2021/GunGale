package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.IModMessage;
import com.mattzm.gungale.nbt.stack.OpticNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class COpticMessage implements IModMessage {
    public final int stackID;
    public final int value;

    public COpticMessage(int stackID, int value) {
        this.stackID = stackID;
        this.value = value;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(this.stackID);
        buffer.writeInt(this.value);
    }

    public static @NotNull COpticMessage decode(@NotNull PacketBuffer buffer) {
        return new COpticMessage(buffer.readInt(), buffer.readInt());
    }

    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            OpticNBT.set(ModPlayerInventory.get(player).getItem(this.stackID), this.value);
        }
    }
}
