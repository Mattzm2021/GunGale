package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.message.IModMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class COpenInventoryMessage implements IModMessage {
    public final Action action;

    public enum Action {
        OPEN_FULL
    }

    public COpenInventoryMessage(Action action) {
        this.action = action;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeEnum(this.action);
    }

    public static @NotNull COpenInventoryMessage decode(@NotNull PacketBuffer buffer) {
        return new COpenInventoryMessage(buffer.readEnum(Action.class));
    }

    @Override
    public void process(NetworkEvent.@NotNull Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            if (this.action == Action.OPEN_FULL) {
                player.openMenu(ModPlayerInventory.get(player));
            }
        }
    }
}
