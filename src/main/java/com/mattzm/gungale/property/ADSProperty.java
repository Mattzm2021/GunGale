package com.mattzm.gungale.property;

import com.mattzm.gungale.util.math.ModMathHelper;
import org.apache.logging.log4j.LogManager;

public class ADSProperty {
    private final float adsSpeed;

    public ADSProperty(float adsSpeed) {
        this.adsSpeed = adsSpeed;
    }

    public float getRenderAdsSpeed() {
        return ModMathHelper.twoDigitsFloat(this.adsSpeed);
    }

    public int getCertainSpeed() {
        int speed = ModMathHelper.getCertainTick(this.adsSpeed);
        LogManager.getLogger().debug(speed);
        return speed;
    }
}
