package net.badgerclaw.cityengine.math;

import java.util.Arrays;

public abstract class AbstractVector<SelfType extends AbstractVector, D extends Dimensionality> implements Dimensionality {

    final public float v[] = new float[dimensions()];

    /**
     * Copies the elements of this vector into another vector of the same dimensions
     *
     * @param dest the vector to copy elements to
     * @return dest
     */
    public SelfType copyTo(SelfType dest) {
        System.arraycopy(v, 0, dest.v, 0, dimensions());
        return dest;
    }

    /**
     * Add the elements of that vector to the elments of this vector. The operation can be expressed as
     * <code>this = this + that</code>
     *
     * @param that the vector to add to this
     * @return this vector object - the operation modifies the original object
     */
    public SelfType add(SelfType that) {
        float[] a = this.v;
        float[] b = that.v;
        float n = a.length;
        for (int i = 0; i < n; i++) {
            a[i] += b[i];
        }
        return (SelfType) this;

    }

    public SelfType sub(SelfType that) {
        float[] a = this.v;
        float[] b = that.v;
        float n = a.length;
        for (int i = 0; i < n; i++) {
            a[i] -= b[i];
        }
        return (SelfType) this;
    }

    public float dot(SelfType that) {
        float[] a = this.v;
        float[] b = that.v;
        float n = a.length;
        float dot = 0;
        for (int i = 0; i < n; i++) {
            dot += a[i] * b[i];
        }
        return dot;
    }

    public SelfType scalar(float s) {
        float[] v = this.v;
        float n = v.length;
        for (int i = 0; i < n; i++) {
            v[i] *= s;
        }
        return (SelfType)this;
    }

    public float lengthSquared() {
        float[] v = this.v;
        float n = v.length;
        float dot = 0;
        for (int i = 0; i < n; i++) {
            dot += v[i] * v[i];
        }
        return dot;
    }

    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    public SelfType normalize() {
        float length_inv = 1f / length();
        return scalar(length_inv);
    }

    public SelfType neg() {
        float[] v = this.v;
        float n = v.length;
        for (int i = 0; i < n; i++) {
            v[i] = -v[i];
        }
        return (SelfType)this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractVector that = (AbstractVector) o;

        if (!Arrays.equals(v, that.v)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(v);
    }

    @Override
    public String toString() { return Arrays.toString(v); }
}
