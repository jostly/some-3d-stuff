package net.badgerclaw.cityengine.math;

public class Vec3 extends AbstractVector<Vec3, Dimensionality3> implements Dimensionality3, Cloneable {

    public final static int
            X = 0,
            Y = 1,
            Z = 2;

    public Vec3() {
    }

    public Vec3(float x, float y, float z) {
        v[X] = x;
        v[Y] = y;
        v[Z] = z;
    }

    /**
     * Calculate the cross product of this vector and that vector. The operation can be written as
     * <pre>
     *     this = this x that
     * </pre>
     * @param that the vector to use as the right hand side of the calculation
     * @return this vector with its elements updated to be the cross product
     */
    public Vec3 cross(Vec3 that) {
        float[] a = this.v;
        float[] b = that.v;

        float x = a[Y] * b[Z] - a[Z] * b[Y];
        float y = a[Z] * b[X] - a[X] * b[Z];
        float z = a[X] * b[Y] - a[Y] * b[X];

        v[X] = x;
        v[Y] = y;
        v[Z] = z;
        return this;
    }

    @Override
    public Vec3 clone() {
        return copyTo(new Vec3());
    }

}
