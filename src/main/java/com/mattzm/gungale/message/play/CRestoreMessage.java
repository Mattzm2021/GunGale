package com.mattzm.gungale.message.play;

import com.mattzm.gungale.item.RestoreItem;
import com.mattzm.gungale.message.IModMessage;
import com.mattzm.gungale.message.NBTAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CRestoreMessage extends NBTMessage implements IModMessage {
    public CRestoreMessage(NBTAction action) {
        super(action);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeEnum(this.action);
    }

    public static @NotNull CRestoreMessage decode(@NotNull PacketBuffer buffer) {
        return new CRestoreMessage(buffer.readEnum(NBTAction.class));
    }

    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            if (this.action == NBTAction.START) {
                RestoreItem.executeSlowDown(player);
            } else if (this.action == NBTAction.END) {
                RestoreItem.removeSlowDown(player);
            } else if (player.getMainHandItem().getItem() instanceof RestoreItem){
                RestoreItem item = (RestoreItem) player.getMainHandItem().getItem();
                item.executeRestore(player, player.getMainHandItem());
            }
        }
    }
}
