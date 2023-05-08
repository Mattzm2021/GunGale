package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.item.weapon.IOpticProvider;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OpticItem extends AttachmentItem {
    public static final double[] MAGNIFICATION_FOV = {60.0, ModMathHelper.getFovWithMagnification(70, 1), ModMathHelper.getFovWithMagnification(70, 2), ModMathHelper.getFovWithMagnification(70, 3), ModMathHelper.getFovWithMagnification(70, 4)};
    private final IOpticProvider.Type[] types;
    private final int magnification;

    public OpticItem(IOpticProvider.Type[] types, int magnification) {
        this.types = types;
        this.magnification = magnification;
    }

    public boolean canFit(AbstractWeaponItem item) {
        for (IOpticProvider.Type type : this.types) {
            if (item.getOptic() == type) {
                return true;
            }
        }

        return false;
    }

    public static double getFov(int magnification) {
        return MAGNIFICATION_FOV[magnification];
    }

    public int getMagnification() {
        return this.magnification;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World world, @NotNull List<ITextComponent> textComponents, @NotNull ITooltipFlag flag) {
        String key = "tooltip.gungale.optic.";
        textComponents.add(new TranslationTextComponent(key + Objects.requireNonNull(this.getRegistryName()).getPath()));
        key += "targets.";
        if (Arrays.equals(this.types, new IOpticProvider.Type[]{IOpticProvider.Type.SHORT})) {
            textComponents.add(new TranslationTextComponent(key + "short"));
        } else if (Arrays.equals(this.types, new IOpticProvider.Type[]{IOpticProvider.Type.MIDDLE})) {
            textComponents.add(new TranslationTextComponent(key + "middle"));
        } else if (Arrays.equals(this.types, new IOpticProvider.Type[]{IOpticProvider.Type.LONG})) {
            textComponents.add(new TranslationTextComponent(key + "long"));
        } else if (Arrays.equals(this.types, new IOpticProvider.Type[]{IOpticProvider.Type.SHORT, IOpticProvider.Type.MIDDLE})) {
            textComponents.add(new TranslationTextComponent(key + "short.middle"));
        } else if (Arrays.equals(this.types, new IOpticProvider.Type[]{IOpticProvider.Type.SHORT, IOpticProvider.Type.LONG})) {
            textComponents.add(new TranslationTextComponent(key + "short.long"));
        } else if (Arrays.equals(this.types, new IOpticProvider.Type[]{IOpticProvider.Type.MIDDLE, IOpticProvider.Type.LONG})) {
            textComponents.add(new TranslationTextComponent(key + "middle.long"));
        } else {
            textComponents.add(new TranslationTextComponent(key + "short.middle.long"));
        }
    }
}
