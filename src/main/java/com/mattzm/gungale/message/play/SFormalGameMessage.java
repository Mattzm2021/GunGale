package com.mattzm.gungale.message.play;

import com.mattzm.gungale.client.object.ClientObjectHolder;
import com.mattzm.gungale.message.IModMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class SFormalGameMessage implements IModMessage {
    private final boolean flag;

    public SFormalGameMessage(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeBoolean(this.flag);
    }

    public static @NotNull SFormalGameMessage decode(@NotNull PacketBuffer buffer) {
        return new SFormalGameMessage(buffer.readBoolean());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void process(NetworkEvent.Context context) {
        ClientObjectHolder.getInstance().getMIngameGui().showBorderText = this.flag;
    }
}
