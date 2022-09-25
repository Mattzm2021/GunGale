package com.mattzm.gungale.client.settings;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.mattzm.gungale.client.nbt.ADSNBT;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.nbt.stack.OpticNBT;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.ClientModLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class ModSettings extends GameSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Splitter OPTION_SPLITTER = Splitter.on(':').limit(2);
    public static final KeyBinding KEY_FIRE = new KeyBinding("key.fire", ModKeyConflictContext.ON_SHOOTABLE_0, Type.MOUSE, 0, "key.categories.gungale");
    public static final KeyBinding KEY_ADS = new KeyBinding("key.ads", ModKeyConflictContext.ON_SHOOTABLE_0, Type.MOUSE, 1, "key.categories.gungale");
    public static final KeyBinding KEY_RELOAD = new KeyBinding("key.reload", ModKeyConflictContext.ON_SHOOTABLE_0, Type.KEYSYM, 82, "key.categories.gungale");
    public static final KeyBinding KEY_INVENTORY = new KeyBinding("key.mod_inventory", KeyConflictContext.UNIVERSAL, Type.KEYSYM, 86, "key.categories.gungale");
    public static final KeyBinding KEY_RESTORE = new KeyBinding("key.restore", ModKeyConflictContext.ON_RESTORABLE_2, Type.MOUSE, 1, "key.categories.gungale");
    public static final KeyBinding KEY_SWAP = new KeyBinding("key.swap", KeyConflictContext.IN_GAME, Type.KEYSYM, 258, "key.categories.gungale");
    public static final KeyBinding KEY_CHANGE_WEAPON = new KeyBinding("key.change_weapon", ModKeyConflictContext.ON_WEAPON_SLOT_6, Type.KEYSYM, 70, "key.categories.gungale");
    public static final KeyBinding KEY_SWAP_OPTIC = new KeyBinding("key.swap_optic", ModKeyConflictContext.ON_WEAPON_SLOT_6, Type.KEYSYM, 67, "key.categories.gungale");
    public static final KeyBinding KEY_SELECT_WEAPON_A = new KeyBinding("key.weapon_a", ModKeyConflictContext.ON_WEAPON_SLOT_6, Type.KEYSYM, 49, "key.categories.gungale");
    public static final KeyBinding KEY_SELECT_WEAPON_B = new KeyBinding("key.weapon_b", ModKeyConflictContext.ON_WEAPON_SLOT_6, Type.KEYSYM, 50, "key.categories.gungale");
    public static final KeyBinding KEY_DROP = new ModKeyBinding("key.copy_drop", ModKeyConflictContext.ON_WEAPON_SLOT_6, Type.KEYSYM.getOrCreate(81));
    private static final double[] SENSITIVITIES = {0.88, 0.88, 0.6, 0.43, 0.34};
    public double globalSensitivity = 0.5;
    private final Minecraft minecraft;
    private final File optionsFile;

    public ModSettings(@NotNull Minecraft minecraft, File file) {
        super(minecraft, file);
        this.minecraft = minecraft;
        this.optionsFile = new File(file, "moptions.txt");
        this.load();
        this.setupAll();
    }

    @Override
    public void load() {
        try {
            if (this.optionsFile == null || !this.optionsFile.exists()) {
                return;
            }

            CompoundNBT data = new CompoundNBT();

            // noinspection UnstableApiUsage
            try (BufferedReader reader = Files.newReader(this.optionsFile, Charsets.UTF_8)) {
                reader.lines().forEach(s -> {
                    try {
                        Iterator<String> iterator = OPTION_SPLITTER.split(s).iterator();
                        data.putString(iterator.next(), iterator.next());
                    } catch (Exception exception) {
                        LOGGER.warn("Skipping bad moption: {}", s);
                    }
                });
            }

            CompoundNBT fixedData = this.dataFix(data);

            for (String key : fixedData.getAllKeys()) {
                String value = fixedData.getString(key);

                try {
                    if ("globalSensitivity".equals(key)) {
                        ModOptions.GLOBAL_SENSITIVITY.set(this, readFloat(value));
                    }
                } catch (Exception exception) {
                    LOGGER.warn("Skipping bad moption: {}:{}", key, value);
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to load moptions", exception);
        }
    }

    private @NotNull CompoundNBT dataFix(CompoundNBT data) {
        int version = 0;

        try {
            version = Integer.parseInt(data.getString("version"));
        } catch (RuntimeException ignored) {}

        return NBTUtil.update(this.minecraft.getFixerUpper(), DefaultTypeReferences.OPTIONS, data, version);
    }

    @Override
    public void save() {
        if (ClientModLoader.isLoading()) return;
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(java.nio.file.Files.newOutputStream(this.optionsFile.toPath()), StandardCharsets.UTF_8))) {
            writer.println("version:" + SharedConstants.getCurrentVersion().getWorldVersion());
            writer.println("globalSensitivity:" + this.globalSensitivity);
        } catch (Exception exception) {
            LOGGER.error("Failed to save options", exception);
        }
    }

    private static float readFloat(String value) {
        if ("true".equals(value)) {
            return 1.0f;
        } else {
            return "false".equals(value) ? 0.0f : Float.parseFloat(value);
        }
    }

    public void setupAll() {
        this.getSettings().sensitivity = this.globalSensitivity;
        this.addAllConflictContext();
        registerKeyBindings();
    }

    public void onClientTick() {
        if (AbstractOption.RENDER_DISTANCE.get(this.getSettings()) > 25) {
            AbstractOption.RENDER_DISTANCE.set(this.getSettings(), 25);
        }

        if (!KEY_DROP.getKey().equals(this.getSettings().keyDrop.getKey())) {
            KEY_DROP.setKey(this.getSettings().keyDrop.getKey());
        }
    }

    public void setSensitivity(PlayerEntity player) {
        if (this.minecraft.player == player) {
            if (!ADSNBT.onADS(player)) {
                this.getSettings().sensitivity = this.globalSensitivity;
            } else {
                this.getSettings().sensitivity = this.globalSensitivity * SENSITIVITIES[OpticNBT.get(ModPlayerInventory.get(player).getSelected())];
            }
        }
    }

    private GameSettings getSettings() {
        return this.minecraft.options;
    }

    private void addAllConflictContext() {
        this.getSettings().keyAttack.setKeyConflictContext(ModKeyConflictContext.MIXED_1_7);
        this.getSettings().keyUse.setKeyConflictContext(ModKeyConflictContext.MIXED_1_3_7);
        this.getSettings().keySprint.setKeyConflictContext(ModKeyConflictContext.OFF_RESTORING_5);
        this.getSettings().keyDrop.setKeyConflictContext(ModKeyConflictContext.OFF_WEAPON_SLOT_7);
        this.getSettings().keyPlayerList.setKeyConflictContext(ModKeyConflictContext.NONE_8);
        this.getSettings().keyPickItem.setKeyConflictContext(ModKeyConflictContext.OFF_WEAPON_SLOT_7);
        this.getSettings().keySwapOffhand.setKeyConflictContext(ModKeyConflictContext.OFF_WEAPON_SLOT_7);
        this.getSettings().keySaveHotbarActivator.setKeyConflictContext(ModKeyConflictContext.OFF_WEAPON_SLOT_7);
        this.getSettings().keyLoadHotbarActivator.setKeyConflictContext(ModKeyConflictContext.OFF_WEAPON_SLOT_7);

        for (KeyBinding keyBinding : this.getSettings().keyHotbarSlots) {
            keyBinding.setKeyConflictContext(ModKeyConflictContext.OFF_WEAPON_SLOT_7);
        }
    }

    private static void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(KEY_FIRE);
        ClientRegistry.registerKeyBinding(KEY_ADS);
        ClientRegistry.registerKeyBinding(KEY_RELOAD);
        ClientRegistry.registerKeyBinding(KEY_INVENTORY);
        ClientRegistry.registerKeyBinding(KEY_RESTORE);
        ClientRegistry.registerKeyBinding(KEY_SWAP);
        ClientRegistry.registerKeyBinding(KEY_CHANGE_WEAPON);
        ClientRegistry.registerKeyBinding(KEY_SWAP_OPTIC);
        ClientRegistry.registerKeyBinding(KEY_SELECT_WEAPON_A);
        ClientRegistry.registerKeyBinding(KEY_SELECT_WEAPON_B);
    }
}
