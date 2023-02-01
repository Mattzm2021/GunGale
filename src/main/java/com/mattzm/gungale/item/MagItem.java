package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.item.weapon.IMagProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagItem extends AttachmentItem {
    private final IMagProvider.Type type;
    private final int level;

    public MagItem(IMagProvider.Type type, int level) {
        this.type = type;
        this.level = level;
    }

    public int getMagazineSize(@NotNull ItemStack stack) {
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (this.level == 1) {
            return item.magProperty.level1;
        } else if (this.level == 2) {
            return item.magProperty.level2;
        } else {
            return item.magProperty.level3;
        }
    }

    public IMagProvider.Type getType() {
        return this.type;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World world, @NotNull List<ITextComponent> textComponents, @NotNull ITooltipFlag flag) {
        textComponents.add(new TranslationTextComponent("tooltip.gungale.mag.lv" + this.level));
        if (this.type == IMagProvider.Type.HEAVY) {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.mag.heavy.targets"));
        } else if (this.type == IMagProvider.Type.LIGHT) {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.mag.light.targets"));
        } else if (this.type == IMagProvider.Type.ENERGY) {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.mag.energy.targets"));
        }
    }
}
