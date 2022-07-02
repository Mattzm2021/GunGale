package com.mattzm.gungale.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

public class ReloadEvent extends Event {
    protected final LogicalSide side;
    protected final PlayerEntity player;

    public ReloadEvent(LogicalSide side, PlayerEntity player) {
        this.side = side;
        this.player = player;
    }
}
