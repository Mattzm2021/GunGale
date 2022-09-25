package com.mattzm.gungale.message.play;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.message.IModMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

public class CEntityHitMessage implements IModMessage {
    protected final int entityID;
    protected final float damage;

    public CEntityHitMessage(int entityID, float damage) {
        this.entityID = entityID;
        this.damage = damage;
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeFloat(this.damage);
    }

    public static @NotNull CEntityHitMessage decode(@NotNull PacketBuffer buffer) {
        return new CEntityHitMessage(buffer.readInt(), buffer.readFloat());
    }

    @Override
    public void process(@NotNull NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player == null) MessageHandler.LOGGER.error("EntityHitMessage cannot provide a PlayerEntity!");
        else {
            Entity entity = player.level.getEntity(this.entityID);
            if (entity != null && !entity.isSpectator()) {
                if (entity instanceof PlayerEntity) if (((PlayerEntity) entity).isCreative()) return;
                AbstractWeaponItem item = (AbstractWeaponItem) ModPlayerInventory.get(player).getSelected().getItem();
                item.hurt((LivingEntity) entity, player, this.damage);
            }
        }
    }
}
