package com.mattzm.gungale.message.play;

import com.mattzm.gungale.message.IModMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CBlockHitMessage implements IModMessage {
    protected final double x;
    protected final double y;
    protected final double z;

    public CBlockHitMessage(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CBlockHitMessage(@NotNull Vector3d position) {
        this.x = position.x();
        this.y = position.y();
        this.z = position.z();
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
    }

    @Contract("_ -> new")
    public static @NotNull CBlockHitMessage decode(@NotNull PacketBuffer buffer) {
        return new CBlockHitMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player == null) MessageHandler.LOGGER.error("BlockHitMessage cannot provide a PlayerEntity!");
        else MessageHandler.sendToWorld(new SBlockHitMessage(this), player.level);
    }
}
