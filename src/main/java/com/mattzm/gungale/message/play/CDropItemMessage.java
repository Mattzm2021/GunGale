package com.mattzm.gungale.message.play;

import com.mattzm.gungale.message.IModMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CDropItemMessage implements IModMessage {
    public final ItemStack stack;

    public CDropItemMessage(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeItemStack(this.stack, false);
    }

    public static @NotNull CDropItemMessage decode(@NotNull PacketBuffer buffer) {
        return new CDropItemMessage(buffer.readItem());
    }

    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            player.drop(this.stack, false, true);
        }
    }
}
