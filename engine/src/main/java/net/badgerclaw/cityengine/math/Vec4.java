package net.badgerclaw.cityengine.math;

public class Vec4 extends AbstractVector<Vec4, Dimensionality4> implements Dimensionality4, Cloneable {

    public final static int
            X = 0,
            Y = 1,
            Z = 2,
            W = 3;

    public Vec4() {
    }

    public Vec4(float x, float y, float z, float w) {
        v[X] = x;
        v[Y] = y;
        v[Z] = z;
        v[W] = w;
    }

    @Override
    public Vec4 clone() {
        return copyTo(new Vec4());
    }

}
