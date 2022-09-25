package com.mattzm.gungale.message.play;

import com.mattzm.gungale.message.IModMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SEntityHitMessage implements IModMessage {
    protected final int entityID;

    public SEntityHitMessage(int entityID) {
        this.entityID = entityID;
    }

    @Contract(pure = true)
    public SEntityHitMessage(@NotNull CEntityHitMessage message) {
        this.entityID = message.entityID;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
    }

    @Contract("_ -> new")
    public static @NotNull SEntityHitMessage decode(@NotNull PacketBuffer buffer) {
        return new SEntityHitMessage(buffer.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void process(NetworkEvent.Context context) {
        World world = Minecraft.getInstance().level;
        if (world == null) MessageHandler.LOGGER.error("SEntityHitMessage cannot provide a World!");
        else {
            Entity entity = world.getEntity(this.entityID);
            if (entity != null) entity.animateHurt();
        }
    }
}
