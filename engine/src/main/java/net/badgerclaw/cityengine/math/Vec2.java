package net.badgerclaw.cityengine.math;

public class Vec2 extends AbstractVector<Vec2, Dimensionality2> implements Dimensionality2, Cloneable {

    public final static int
            X = 0,
            Y = 1;

    public Vec2() {
    }

    public Vec2(float x, float y) {
        v[X] = x;
        v[Y] = y;
    }

    @Override
    public Vec2 clone() {
        return copyTo(new Vec2());
    }

}
