package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.item.weapon.IStockProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StockItem extends AttachmentItem {
    private final IStockProvider.Type type;
    private final int level;

    public StockItem(IStockProvider.Type type, int level) {
        this.type = type;
        this.level = level;
    }

    public int getPrecisionIncrease(@NotNull ItemStack stack) {
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        if (this.level == 1) {
            return item.recoilProperty.stockLevel1;
        } else if (this.level == 2) {
            return item.recoilProperty.stockLevel2;
        } else {
            return item.recoilProperty.stockLevel3;
        }
    }

    public IStockProvider.Type getType() {
        return this.type;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World world, @NotNull List<ITextComponent> textComponents, @NotNull ITooltipFlag flag) {
        textComponents.add(new TranslationTextComponent("tooltip.gungale.stock.lv" + this.level));
        if (this.type == IStockProvider.Type.HEAVY) {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.stock.heavy.targets"));
        } else {
            textComponents.add(new TranslationTextComponent("tooltip.gungale.stock.light.targets"));
        }
    }
}
