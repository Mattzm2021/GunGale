package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.client.nbt.FireNBT;
import com.mattzm.gungale.client.nbt.ShotCountNBT;
import com.mattzm.gungale.client.nbt.tick.CoolDownNBT;
import com.mattzm.gungale.client.nbt.tick.RecoilNBT;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.nbt.stack.BulletNBT;
import com.mattzm.gungale.nbt.stack.PrecisionNBT;
import com.mattzm.gungale.property.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractThreeRounded extends AbstractAutoWeaponItem implements IThreeRounded {
    protected AbstractThreeRounded(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Override
    protected void reactRecoil(@NotNull PlayerEntity player, ItemStack stack) {
        if (this.isFiring(player, stack)) {
            if (ShotCountNBT.getCount(player) == 1) {
                player.xRot -= RecoilProperty.getScreenVRecoil(this.verticalRecoil * 3 * this.get1stModifier(), this.precision, PrecisionNBT.get(stack));
                player.yRot += RecoilProperty.getScreenHRecoil(this.horizontalRecoil * 3 * this.get1stModifier(), this.precision, PrecisionNBT.get(stack));
            } else if (ShotCountNBT.getCount(player) == 2) {
                player.xRot -= RecoilProperty.getScreenVRecoil(this.verticalRecoil * 3 * this.get2ndModifier(), this.precision, PrecisionNBT.get(stack));
                player.yRot += RecoilProperty.getScreenHRecoil(this.horizontalRecoil * 3 * this.get2ndModifier(), this.precision, PrecisionNBT.get(stack));
            } else {
                player.xRot -= RecoilProperty.getScreenVRecoil(this.verticalRecoil * 3 * this.get3rdModifier(), this.precision, PrecisionNBT.get(stack));
                player.yRot += RecoilProperty.getScreenHRecoil(this.horizontalRecoil * 3 * this.get3rdModifier(), this.precision, PrecisionNBT.get(stack));
            }
        }
    }

    @Override
    public boolean canFire(PlayerEntity player, ItemStack stack) {
        if (haveFireAbility(player, stack)) {
            if (this.isFiring(player, stack)) {
                return true;
            } else if (FireNBT.onFire(player) && !CoolDownNBT.hasStart(player)) {
                ShotCountNBT.start(player);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void updateGun(@NotNull PlayerEntity player, ItemStack stack) {
        if (ShotCountNBT.hasChangeStack(player)) {
            CoolDownNBT.startWith(player, 10);
            RecoilNBT.startWith(player, 11, RecoilProperty.getScreenVRecoil(this.verticalRecoil, this.precision, PrecisionNBT.get(stack)) * 3);
            ShotCountNBT.reset(player);
        } else if (ShotCountNBT.next(player)) {
            CoolDownNBT.startWith(player, 10);
            RecoilNBT.startWith(player, 11, RecoilProperty.getScreenVRecoil(this.verticalRecoil, this.precision, PrecisionNBT.get(stack)) * 3);
            if (player.isCreative()) return;
            BulletNBT.addFromClient(stack, ModPlayerInventory.get(player).selected, -1);
        } else {
            super.updateGun(player, stack);
            RecoilNBT.reset(player);
        }
    }

    @Override
    public boolean isFiring(@NotNull PlayerEntity player, @NotNull ItemStack stack) {
        if (ShotCountNBT.hasChangeStack(player)) {
            this.updateGun(player, stack);
            return false;
        } else {
            return ShotCountNBT.hasStart(player);
        }
    }
}
