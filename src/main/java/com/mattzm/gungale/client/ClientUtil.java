package com.mattzm.gungale.client;

import com.mattzm.gungale.client.nbt.tick.ReloadNBT;
import com.mattzm.gungale.client.settings.ModGameSettings;
import com.mattzm.gungale.item.GearItem;
import com.mattzm.gungale.item.RestoreItem;
import com.mattzm.gungale.client.nbt.ADSNBT;
import com.mattzm.gungale.client.nbt.FireNBT;
import com.mattzm.gungale.client.nbt.tick.RestoreNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
    public static void tick() {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) return;
        if (ModGameSettings.KEY_FIRE.isDown()) {
            if (!FireNBT.onFire(player)) {
                FireNBT.start(player);
            }
        } else if (FireNBT.onFire(player)) {
            FireNBT.end(player);
        }

        if (ModGameSettings.KEY_ADS.isDown() && !ReloadNBT.hasStart(player)) {
            if (!ADSNBT.onADS(player)) {
                ADSNBT.start(player, Minecraft.getInstance().options.fov);
            }
        } else if (ADSNBT.onADS(player)) {
            ADSNBT.setFov(player, Minecraft.getInstance().options.fov);
            ADSNBT.end(player);
        }

        if (ModGameSettings.KEY_RESTORE.isDown()) {
            RestoreItem item = (RestoreItem) player.getMainHandItem().getItem();
            if (item.checkUsing(GearItem.get(player), player))
                RestoreNBT.startWith(player, item.timeToUse);
        } else RestoreNBT.reset(player);
    }
}
