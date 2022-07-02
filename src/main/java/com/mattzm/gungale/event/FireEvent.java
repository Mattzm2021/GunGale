package com.mattzm.gungale.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

public class FireEvent extends Event {
    protected final LogicalSide side;
    protected final PlayerEntity player;
    protected final LivingEntity entity;
    protected final double distanceSqr;

    public FireEvent(LogicalSide side, PlayerEntity player) {
        this.side = side;
        this.player = player;
        this.entity = null;
        this.distanceSqr = 0.0d;
    }

    public FireEvent(LogicalSide side, PlayerEntity player, LivingEntity entity, double distanceSqr) {
        this.side = side;
        this.player = player;
        this.entity = entity;
        this.distanceSqr = distanceSqr;
    }
}
