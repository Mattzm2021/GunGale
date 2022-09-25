package com.mattzm.gungale.message.play;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.IModMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SBlockHitMessage implements IModMessage {
    protected final double x;
    protected final double y;
    protected final double z;

    public SBlockHitMessage(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Contract(pure = true)
    public SBlockHitMessage(@NotNull CBlockHitMessage message) {
        this.x = message.x;
        this.y = message.y;
        this.z = message.z;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
    }

    @Contract("_ -> new")
    public static @NotNull SBlockHitMessage decode(@NotNull PacketBuffer buffer) {
        return new SBlockHitMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        World world = Minecraft.getInstance().level;
        if (world == null) MessageHandler.LOGGER.error("SBlockHitMessage cannot provide a World!");
        else AbstractWeaponItem.addParticle(world, this.x, this.y, this.z);
    }
}
