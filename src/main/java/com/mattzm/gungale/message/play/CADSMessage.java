package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.IModMessage;
import com.mattzm.gungale.message.NBTAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CADSMessage extends NBTMessage implements IModMessage {
    public CADSMessage(NBTAction action) {
        super(action);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeEnum(this.action);
    }

    public static @NotNull CADSMessage decode(@NotNull PacketBuffer buffer) {
        return new CADSMessage(buffer.readEnum(NBTAction.class));
    }

    @Override
    public void process(NetworkEvent.@NotNull Context context) {
        PlayerEntity player = context.getSender();
        if (player == null) return;
        if (!(ModPlayerInventory.get(player).getSelected().getItem() instanceof AbstractWeaponItem)) return;
        AbstractWeaponItem item = (AbstractWeaponItem) ModPlayerInventory.get(player).getSelected().getItem();
        if (this.action == NBTAction.START) {
            item.executeSlowDown(player);
        } else if (this.action == NBTAction.END) {
            item.removeSlowDown(player);
        }
    }
}
