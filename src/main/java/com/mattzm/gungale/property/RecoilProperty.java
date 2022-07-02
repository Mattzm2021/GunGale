package com.mattzm.gungale.property;

import com.mattzm.gungale.client.settings.ModSettings;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.util.math.MathHelper;

public class RecoilProperty {
    private static final float iPhoneWidth = 224.0f / MathHelper.sqrt(337);
    private static final float iPhoneHeight = 126.0f / MathHelper.sqrt(337);
    public final float iPhoneVertical;
    public final float iPhoneHorizontal;
    public final int recoverSpeed;

    public RecoilProperty(float iPhoneVertical, float iPhoneHorizontal, int recoverSpeed) {
        this.iPhoneVertical = iPhoneVertical;
        this.iPhoneHorizontal = iPhoneHorizontal;
        this.recoverSpeed = recoverSpeed;
    }

    public float verticalAngle() {
        double scale = Math.tan(ModMathHelper.getRadius(ModSettings.getVerticalFOV() / 2));
        return (float) ModMathHelper.getDegree(Math.atan(2 * scale * this.iPhoneVertical / iPhoneHeight));
    }

    public float horizontalAngle() {
        double scale = Math.tan(ModMathHelper.getRadius(ModSettings.getHorizontalFOV() / 2));
        return (float) ModMathHelper.getDegree(Math.atan(2 * scale * this.iPhoneHorizontal / iPhoneWidth));
    }

    public float verticalRecover() {
        return verticalAngle() / recoverSpeed;
    }

    public float horizontalRecover() {
        return horizontalAngle() / recoverSpeed;
    }
}
