package com.mattzm.gungale.property;

public class MagProperty {
    public final int level1;
    public final int level2;
    public final int level3;

    public MagProperty(int level1, int level2, int level3) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
    }

    public int getMagazineIncrement(int level) {
        if (level == 1) {
            return this.level1;
        } else if (level == 2) {
            return this.level2;
        } else if (level == 3) {
            return this.level3;
        } else {
            return 0;
        }
    }
}
