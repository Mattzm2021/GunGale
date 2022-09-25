package com.mattzm.gungale.property;

import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;

public class RecoilProperty {
    public final int verticalRecoil;
    public final int horizontalRecoil;
    public final int hipFireAccuracy;
    public final int barrelLevel1;
    public final int barrelLevel2;
    public final int barrelLevel3;
    public final int stockLevel1;
    public final int stockLevel2;
    public final int stockLevel3;

    public RecoilProperty(int verticalRecoil, int horizontalRecoil, int hipFireAccuracy, int stockLevel1, int stockLevel2, int stockLevel3) {
        this(verticalRecoil, horizontalRecoil, hipFireAccuracy, 0, 0, 0, stockLevel1, stockLevel2, stockLevel3);
    }

    public RecoilProperty(int verticalRecoil, int horizontalRecoil, int hipFireAccuracy, int barrelLevel1, int barrelLevel2, int barrelLevel3, int stockLevel1, int stockLevel2, int stockLevel3) {
        this.verticalRecoil = verticalRecoil;
        this.horizontalRecoil = horizontalRecoil;
        this.hipFireAccuracy = hipFireAccuracy;
        this.barrelLevel1 = barrelLevel1;
        this.barrelLevel2 = barrelLevel2;
        this.barrelLevel3 = barrelLevel3;
        this.stockLevel1 = stockLevel1;
        this.stockLevel2 = stockLevel2;
        this.stockLevel3 = stockLevel3;
    }

    public static double getScreenVRecoil(double recoil, int itemPrecision, int actualPrecision) {
        return recoil * (100.0 - actualPrecision) / (100.0 - itemPrecision) * 0.018;
    }

    public static double getScreenHRecoil(double recoil, int itemPrecision, int actualPrecision) {
        double realRecoil = ModMathHelper.getRangeRandom(recoil, -recoil);
        return getScreenVRecoil(realRecoil, itemPrecision, actualPrecision);
    }

    public static double getRenderVRecoil(int recoil, int itemPrecision, int actualPrecision) {
        return ModMathHelper.twoDigitsDouble(getScreenVRecoil(recoil, itemPrecision, actualPrecision));
    }

    public static double getRenderHRecoil(int recoil, int itemPrecision, int actualPrecision) {
        return ModMathHelper.twoDigitsDouble(getScreenVRecoil(recoil, itemPrecision, actualPrecision));
    }

    public static @NotNull Vector3d getBulletVector(int hipFireAccuracy, @NotNull PlayerEntity player) {
        float distance = (float) Math.random() * (500.0f / hipFireAccuracy - 5);
        float degree = (float) Math.random() * 360;
        float xRot = player.xRot + MathHelper.cos(ModMathHelper.degreeToRadius(degree)) * distance;
        float yRot = player.yRot + MathHelper.sin(ModMathHelper.degreeToRadius(degree)) * distance;

        float f = xRot * (float) (Math.PI / 180.0);
        float f1 = -yRot * (float) (Math.PI / 180.0);
        float f2 = MathHelper.cos(f1);
        float f3 = MathHelper.sin(f1);
        float f4 = MathHelper.cos(f);
        float f5 = MathHelper.sin(f);
        return new Vector3d(f3 * f4, -f5, f2 * f4);
    }
}
