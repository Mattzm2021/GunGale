package com.mattzm.gungale.server.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.util.game.FormalGameHandler;
import com.mattzm.gungale.util.game.QuickGameHandler;
import com.mattzm.gungale.util.nbt.ModDataManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID)
public class ServerHandler {
    @SubscribeEvent
    public static void onServerStopping(@NotNull final FMLServerStoppingEvent event) throws IOException {
        MinecraftServer server = event.getServer();
        File directory = server.getServerDirectory();
        CompoundNBT compoundNBT = new CompoundNBT(), data = new CompoundNBT();
        ModDataManager.saveServerData(data, server);
        compoundNBT.put("data", data);
        File file = new File(directory, "level.mdat");
        CompressedStreamTools.writeCompressed(compoundNBT, file);

        if (FormalGameHandler.getInstance() != null) {
            FormalGameHandler.getInstance().stopFormalGame();
        }

        if (QuickGameHandler.getInstance() != null) {
            QuickGameHandler.getInstance().stopGame();
        }
    }

    @SubscribeEvent
    public static void onServerStarting(@NotNull FMLServerStartingEvent event) throws IOException {
        MinecraftServer server = event.getServer();
        File directory = server.getServerDirectory();
        File file = new File(directory, "level.mdat");
        CompoundNBT data = new CompoundNBT();
        if (file.exists() && file.isFile()) {
            data = CompressedStreamTools.readCompressed(file);
        }

        ModDataManager.loadServerData(data.getCompound("data"), server);
    }
}
