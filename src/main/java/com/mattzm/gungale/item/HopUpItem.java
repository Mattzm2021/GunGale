package com.mattzm.gungale.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class HopUpItem extends AttachmentItem {
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World world, @NotNull List<ITextComponent> textComponents, @NotNull ITooltipFlag flag) {
        textComponents.add(new TranslationTextComponent("tooltip.gungale.hop_up." + Objects.requireNonNull(this.getRegistryName()).getPath()));
        textComponents.add(new TranslationTextComponent("tooltip.gungale.hop_up.targets." + Objects.requireNonNull(this.getRegistryName()).getPath()));
    }
}
