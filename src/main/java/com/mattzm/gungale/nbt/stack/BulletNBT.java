package com.mattzm.gungale.nbt.stack;

import com.mattzm.gungale.message.play.CBulletMessage;
import com.mattzm.gungale.message.play.MessageHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class BulletNBT {
    public static final String TAG_ID = "bullet";

    public static void add(ItemStack stack, int value) {
        set(stack, get(stack) + value);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addFromClient(ItemStack stack, int stackID, int value) {
        setFromClient(stackID, get(stack) + value);
    }

    public static boolean isEmpty(ItemStack stack) {
        return get(stack) == 0;
    }

    public static boolean hasSpace(ItemStack stack) {
        return getSpace(stack) > 0;
    }

    public static int getSpace(@NotNull ItemStack stack) {
        return MagSizeNBT.get(stack) - get(stack);
    }

    public static int get(@NotNull ItemStack stack) {
        if (!stack.getOrCreateTag().contains(TAG_ID)) {
            stack.getOrCreateTag().putInt(TAG_ID, MagSizeNBT.get(stack));
        }

        return stack.getOrCreateTag().getInt(TAG_ID);
    }

    public static void set(@NotNull ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(TAG_ID, value);
    }

    @OnlyIn(Dist.CLIENT)
    private static void setFromClient(int stackID, int value) {
        MessageHandler.sendToServer(new CBulletMessage(stackID, value));
    }
}
