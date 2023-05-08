package com.mattzm.gungale.nbt.stack;

import com.mattzm.gungale.message.play.CFireModeMessage;
import com.mattzm.gungale.message.play.MessageHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class FireModeNBT {
    public static final String TAG_ID = "fireMode";

    public static int get(@NotNull ItemStack stack) {
        return stack.getOrCreateTag().getInt(TAG_ID);
    }

    public static void set(@NotNull ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(TAG_ID, value);
    }

    @OnlyIn(Dist.CLIENT)
    public static void setFromClient(ItemStack stack, int stackId, int value) {
        set(stack, value);
        MessageHandler.sendToServer(new CFireModeMessage(stackId, value));
    }
}
