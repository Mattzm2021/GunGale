package com.mattzm.gungale.item;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.property.BasicProperty;
import com.mattzm.gungale.property.DamageProperty;
import com.mattzm.gungale.property.RecoilProperty;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GunGale.MOD_ID)
public class ModItems {
    public static final Item P9_PISTOL = registerItem("p9_pistol", new PistolItem(
            new BasicProperty(9.6f, 30, 4, 7, 50),
            DamageProperty.create2To3(3, 27,  1.0f / 6.0f, 1.0f / 3.0f, 8.0f / 15.0f),
            new RecoilProperty(0.1271f, 0.0246f, 5)));
    public static final Item SMG_AMMO = registerItem("smg_ammo", new SMGAmmoItem());
    public static final Item BODY_ARMOR_3 = registerItem("body_armor_3", new BodyArmorItem(0.65f));

    private static Item registerItem(String location, Item item) {
        return item.setRegistryName(location);
    }
}
