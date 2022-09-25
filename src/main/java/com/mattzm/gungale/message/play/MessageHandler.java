package com.mattzm.gungale.message.play;

import com.mattzm.gungale.GunGale;
import java.util.Optional;
import java.util.function.Supplier;
import com.mattzm.gungale.message.IModMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MessageHandler {
    public static SimpleChannel channel;
    public static ResourceLocation location = new ResourceLocation(GunGale.MOD_ID, "main");
    public static final Logger LOGGER = LogManager.getLogger();

    public static void onMessageReceived(IModMessage message, @NotNull Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> message.process(context));
        context.setPacketHandled(true);
    }

    public static void sendToServer(IModMessage message) {
        channel.sendToServer(message);
    }

    public static void sendToAll(IModMessage message) {
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToWorld(IModMessage message, @NotNull World world) {
        channel.send(PacketDistributor.DIMENSION.with(world::dimension), message);
    }

    public static void sendToPlayer(IModMessage message, PlayerEntity player) {
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    public static void setupAll() {
        setSimpleChannel();
        registerMessage();
    }

    private static void setSimpleChannel() {
        channel = NetworkRegistry.newSimpleChannel(location, () -> "1.0", (ver) -> true, (ver) -> true);
    }

    private static void registerMessage() {
        channel.registerMessage(0, CBlockHitMessage.class,
                CBlockHitMessage::encode, CBlockHitMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(1, CBulletMessage.class,
                CBulletMessage::encode, CBulletMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(2, CDropItemMessage.class,
                CDropItemMessage::encode, CDropItemMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(3, CEntityHitMessage.class,
                CEntityHitMessage::encode, CEntityHitMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(4, CExecuteReloadMessage.class,
                CExecuteReloadMessage::encode, CExecuteReloadMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(5, COpticMessage.class,
                COpticMessage::encode, COpticMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(6, CRestoreMessage.class,
                CRestoreMessage::encode, CRestoreMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(7, CChangeInventoryMessage.class,
                CChangeInventoryMessage::encode, CChangeInventoryMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(8, COpenInventoryMessage.class,
                COpenInventoryMessage::encode, COpenInventoryMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        channel.registerMessage(9, SUpdateInventoryMessage.class,
                SUpdateInventoryMessage::encode, SUpdateInventoryMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(10, SBlockHitMessage.class,
                SBlockHitMessage::encode, SBlockHitMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(11, SEntityHitMessage.class,
                SEntityHitMessage::encode, SEntityHitMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(12, SFormalGameMessage.class,
                SFormalGameMessage::encode, SFormalGameMessage::decode,
                MessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

    }
}
