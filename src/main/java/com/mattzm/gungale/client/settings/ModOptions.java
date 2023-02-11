package com.mattzm.gungale.client.settings;

import com.mattzm.gungale.client.object.ClientObjectHolder;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModOptions {
    public static final SliderPercentageOption GLOBAL_SENSITIVITY = new SliderPercentageOption("moptions.globalSensitivity", 0.0, 1.0, 0.0f, settings ->
            ClientObjectHolder.getInstance().getMOptions().globalSensitivity, (settings, value) -> {
        ClientObjectHolder.getInstance().getMOptions().globalSensitivity = value;
        ClientObjectHolder.getInstance().getMOptions().save();
    }, (settings, option) -> {
        double pct = option.toPct(option.get(settings));
        if (pct == 0.0D) {
            return genericValueLabel("moptions.globalSensitivity", new TranslationTextComponent("options.sensitivity.min"));
        } else {
            return pct == 1.0D ? genericValueLabel("moptions.globalSensitivity", new TranslationTextComponent("options.sensitivity.max")) : percentValueLabel("moptions.globalSensitivity", 2.0 * pct);
        }
    });

    private static @NotNull ITextComponent genericValueLabel(String key, ITextComponent value) {
        return new TranslationTextComponent("options.generic_value", new TranslationTextComponent(key), value);
    }

    private static @NotNull ITextComponent percentValueLabel(String key, double value) {
        return new TranslationTextComponent("options.percent_value", new TranslationTextComponent(key), (int) (value * 100.0));
    }
}
