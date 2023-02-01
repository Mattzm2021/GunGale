package com.mattzm.gungale.item;

import com.mattzm.gungale.item.weapon.IOpticProvider;

public class VariableOpticItem extends OpticItem implements IVariable2I {
    private final int smaller;
    private final int larger;

    public VariableOpticItem(IOpticProvider.Type[] types, int magnification, int smaller, int larger) {
        super(types, magnification);
        this.smaller = smaller;
        this.larger = larger;
    }

    @Override
    public int getSmallerInt() {
        return this.smaller;
    }

    @Override
    public int getLargerInt() {
        return this.larger;
    }
}
