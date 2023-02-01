package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BarrelItem extends AttachmentItem {
    public final int level;

    public BarrelItem(int level) {
        this.level = level;
    }

    public int getPrecisionIncrease(@NotNull ItemStack stack) {
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (this.level == 1) {
            return item.recoilProperty.barrelLevel1;
        } else if (this.level == 2) {
            return item.recoilProperty.barrelLevel2;
        } else {
            return item.recoilProperty.barrelLevel3;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World world, @NotNull List<ITextComponent> textComponents, @NotNull ITooltipFlag flag) {
        textComponents.add(new TranslationTextComponent("tooltip.gungale.barrel.lv" + this.level));
        textComponents.add(new TranslationTextComponent("tooltip.gungale.barrel.targets"));
    }
}
