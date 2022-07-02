package com.mattzm.gungale.message;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.event.EventHandler;
import com.mattzm.gungale.event.FireEvent;
import com.mattzm.gungale.event.ReloadEvent;
import com.mattzm.gungale.item.GunItem;
import java.util.Optional;
import java.util.function.Supplier;

import com.mattzm.gungale.nbt.RecoilNBT;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageHandler {
    public static SimpleChannel channel;
    public static ResourceLocation location = new ResourceLocation(GunGale.MOD_ID, "main");
    public static final String VERSION = "1.0";

    public static class Server {
        public static boolean isCorrectVersion(String version) {
            return VERSION.equals(version);
        }

        public static void onParticleReceived(BlockHitMessage.ToServer message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            PlayerEntity player = context.getSender();
            if (player == null) GunGale.LOGGER.error("BlockHitMessage.ToServer cannot provide a PlayerEntity!");
            else context.enqueueWork(() -> processParticle(message, player));
        }

        private static void processParticle(BlockHitMessage.ToServer message, PlayerEntity player) {
            channel.send(PacketDistributor.DIMENSION.with(player.level::dimension), new BlockHitMessage.ToClient(message.x, message.y, message.z));
        }

        public static void onEntityReceived(EntityHitMessage.ToServer message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            ServerPlayerEntity player = context.getSender();
            if (player == null) GunGale.LOGGER.error("EntityHitMessage.ToServer cannot provide a PlayerEntity!");
            else context.enqueueWork(() -> processEntity(message, player));
        }

        private static void processEntity(EntityHitMessage.ToServer message, ServerPlayerEntity player) {
            LivingEntity entity = (LivingEntity) player.getLevel().getEntity(message.entityID);
            if (entity == null) GunGale.LOGGER.error("EntityHitMessage.ToServer cannot provide a target Entity!");
            else {
                GunGale.FORGE_EB.post(new FireEvent(LogicalSide.SERVER, player, entity, message.distanceSqr));
                channel.send(PacketDistributor.DIMENSION.with(player.level::dimension), new EntityHitMessage.ToClient(message.entityID));
            }
        }

        public static void onFireReceived(FireMessage.ToServer message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            PlayerEntity player = context.getSender();

            if (player == null) GunGale.LOGGER.error("FireMessage.ToServer cannot provide a PlayerEntity!");
            else context.enqueueWork(() -> processFire(player));
        }

        private static void processFire(PlayerEntity player) {
            GunItem.updateGun(player.getMainHandItem());
            RecoilNBT.start(player.getMainHandItem());
        }

        public static void onReloadReceived(ReloadMessage.ToServer message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            PlayerEntity player = context.getSender();
            if (player == null) GunGale.LOGGER.error("ReloadMessage.ToServer cannot provide a PlayerEntity!");
            else context.enqueueWork(() -> processReload(player));
        }

        private static void processReload(PlayerEntity player) {
            EventHandler.onReload(new ReloadEvent(LogicalSide.SERVER, player));
        }

        public static void onStackReceived(StackMessage.ToServer message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            PlayerEntity player = context.getSender();
            if (player == null) GunGale.LOGGER.error("StackMessage.ToServer cannot provide a PlayerEntity!");
            else context.enqueueWork(() -> processStack(message, player));
        }

        private static void processStack(StackMessage.ToServer message, PlayerEntity player) {
            ItemStack stack = player.inventory.getItem(message.stackID);
            if (message.flag) player.inventory.removeItem(stack);
            else stack.shrink(message.value);
        }
    }

    public static class Client {
        public static boolean isCorrectVersion(String version) {
            return VERSION.equals(version);
        }

        public static void onParticleReceived(BlockHitMessage.ToClient message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());
            if (!world.isPresent()) GunGale.LOGGER.error("BlockHitMessage.ToClient cannot provide a World!");
            else context.enqueueWork(() -> processParticle(message, world.get()));
        }

        private static void processParticle(BlockHitMessage.ToClient message, World world) {
            GunItem.addParticle(world, message.x, message.y, message.z);
        }

        public static void onEntityReceived(EntityHitMessage.ToClient message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());

            if (!world.isPresent()) GunGale.LOGGER.error("EntityHitMessage.ToClient cannot provide a World!");
            else context.enqueueWork(() -> processEntity(message, world.get()));
        }

        private static void processEntity(EntityHitMessage.ToClient message, World world) {
            LivingEntity entity = (LivingEntity) world.getEntity(message.entityID);
            if (entity == null) GunGale.LOGGER.error("EntityHitMessage.ToClient cannot provide a target Entity!");
            else entity.animateHurt();
        }

        public static void onFireReceived(FireMessage.ToClient message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.setPacketHandled(true);
            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());

            if (!world.isPresent()) GunGale.LOGGER.error("EntityHitMessage.ToClient cannot provide a World!");
            else context.enqueueWork(() -> processFire(message, world.get()));
        }

        private static void processFire(FireMessage.ToClient message, World world) {
            PlayerEntity player = (PlayerEntity) world.getEntity(message.entityID);
            if (player == null) GunGale.LOGGER.error("FireMessage.ToClient cannot provide a player!");
            else {
                GunItem item = (GunItem) player.getMainHandItem().getItem();
                item.consumeRecover(player);
            }
        }
    }

    public static void setupAll() {
        setSimpleChannel();
        registerMessage();
    }

    private static void setSimpleChannel() {
        channel = NetworkRegistry.newSimpleChannel(location, () -> VERSION,
                MessageHandler.Client::isCorrectVersion, MessageHandler.Server::isCorrectVersion);
    }

    private static void registerMessage() {
        channel.registerMessage(BlockHitMessage.ToClient.ID, BlockHitMessage.ToClient.class,
                BlockHitMessage.ToClient::encode, BlockHitMessage.ToClient::decode,
                Client::onParticleReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(BlockHitMessage.ToServer.ID, BlockHitMessage.ToServer.class,
                BlockHitMessage.ToServer::encode, BlockHitMessage.ToServer::decode,
                Server::onParticleReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(EntityHitMessage.ToClient.ID, EntityHitMessage.ToClient.class,
                EntityHitMessage.ToClient::encode, EntityHitMessage.ToClient::decode,
                Client::onEntityReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(EntityHitMessage.ToServer.ID, EntityHitMessage.ToServer.class,
                EntityHitMessage.ToServer::encode, EntityHitMessage.ToServer::decode,
                Server::onEntityReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(FireMessage.ToServer.ID, FireMessage.ToServer.class,
                FireMessage.ToServer::encode, FireMessage.ToServer::decode,
                Server::onFireReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(FireMessage.ToClient.ID, FireMessage.ToClient.class,
                FireMessage.ToClient::encode, FireMessage.ToClient::decode,
                Client::onFireReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        channel.registerMessage(ReloadMessage.ToServer.ID, ReloadMessage.ToServer.class,
                ReloadMessage.ToServer::encode, ReloadMessage.ToServer::decode,
                Server::onReloadReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        channel.registerMessage(StackMessage.ToServer.ID, StackMessage.ToServer.class,
                StackMessage.ToServer::encode, StackMessage.ToServer::decode,
                Server::onStackReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
