package com.mattzm.gungale.client.handler;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.settings.ModOptions;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.gui.screen.MouseSettingsScreen;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = GunGale.MOD_ID, value = Dist.CLIENT)
public class ScreenHandler {
    @SubscribeEvent
    public static void onGuiInit$Post(final @NotNull GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof MouseSettingsScreen) {
            OptionsRowList list = (OptionsRowList) event.getGui().children().get(0);
            AbstractOption option = ModOptions.GLOBAL_SENSITIVITY;
            OptionButton button = (OptionButton) list.children().get(0).children().get(1);
            AbstractOption option1 = button.getOption();
            list.children().set(0, OptionsRowList.Row.small(event.getGui().getMinecraft().options, list.getWidth(), option, option1));
        }
    }
}
