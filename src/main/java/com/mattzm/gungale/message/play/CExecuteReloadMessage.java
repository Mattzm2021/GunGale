package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.IModMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CExecuteReloadMessage implements IModMessage {
    public final int bullet;

    public CExecuteReloadMessage(int bullet) {
        this.bullet = bullet;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(this.bullet);
    }

    public static @NotNull CExecuteReloadMessage decode(@NotNull PacketBuffer buffer) {
        return new CExecuteReloadMessage(buffer.readInt());
    }

    @Override
    public void process(NetworkEvent.@NotNull Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            ItemStack stack = ModPlayerInventory.get(player).getSelected();
            AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
            item.executeReload(player, stack);
        }
    }
}
