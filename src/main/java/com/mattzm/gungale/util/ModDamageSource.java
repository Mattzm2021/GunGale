package com.mattzm.gungale.util;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ModDamageSource extends EntityDamageSource {
    public ModDamageSource(String p_i1567_1_, @Nullable Entity p_i1567_2_) {
        super(p_i1567_1_, p_i1567_2_);
    }

    @Contract("_ -> new")
    public static @NotNull DamageSource gunshot(PlayerEntity player) {
        return new ModDamageSource("gunshot", player);
    }

    @Override
    public @NotNull ITextComponent getLocalizedDeathMessage(@NotNull LivingEntity entity) {
        String s = "death.attack." + this.msgId;
        if (this.entity == null) return new TranslationTextComponent(s, entity.getDisplayName());
        ItemStack stack = this.entity instanceof PlayerEntity ? ModPlayerInventory.get((PlayerEntity) this.entity).getSelected() : ItemStack.EMPTY;
        if (stack.isEmpty()) {
            return new TranslationTextComponent(s + ".player", entity.getDisplayName(), this.entity.getDisplayName());
        } else {
            return new TranslationTextComponent(s + ".item", entity.getDisplayName(), this.entity.getDisplayName(), stack.getDisplayName(), entity.distanceTo(this.entity));
        }
    }
}
