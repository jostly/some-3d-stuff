package net.badgerclaw.cityengine.math;

import java.nio.FloatBuffer;
import java.util.Arrays;

public abstract class AbstractMatrix<SelfType extends AbstractMatrix, D extends Dimensionality> implements Dimensionality, Cloneable {

    final public float[] m = new float[dimensions() * dimensions()];
    SelfType next;

    /**
     * Copies the elements of this matrix into another matrix of the same dimensions
     *
     * @param dest the matrix to copy elements to
     * @return dest
     */
    public SelfType copyTo(SelfType dest) {
        System.arraycopy(m, 0, dest.m, 0, m.length);
        return dest;
    }

    /**
     * Set this matrix to the setIdentity matrix
     *
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType setIdentity() {
        float[] m = this.m;
        int n = m.length;
        int s = dimensions() + 1;
        for (int i = 0; i < n; i++) {
            if (i % s == 0) {
                m[i] = 1f;
            } else {
                m[i] = 0f;
            }
        }
        return (SelfType) this;
    }

    /**
     * Add the elements of that matrix to the elments of this matrix. The operation can be expressed as
     * <code>this = this + that</code>
     *
     * @param that the matrix to add to this
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType add(SelfType that) {
        float[] a = this.m;
        float[] b = that.m;
        float n = a.length;
        for (int i = 0; i < n; i++) {
            a[i] += b[i];
        }
        return (SelfType) this;
    }

    /**
     * Subtract the elements of that matrix from the elements of this matrix. The operation can be expressed as
     * <code>this = this - that</code>
     *
     * @param that the matrix to subtract from this
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType sub(SelfType that) {
        float[] a = this.m;
        float[] b = that.m;
        float n = a.length;
        for (int i = 0; i < n; i++) {
            a[i] -= b[i];
        }
        return (SelfType) this;
    }

    /**
     * Multiply this matrix by another 2x2 matrix. The operation can be expressed as
     * <code>this = this * that</code>
     *
     * @param that matrix to multiply this by
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType mul(SelfType that) {
        float[] a = this.m;
        float[] b = that.m;
        float[] temp = new float[a.length];
        int s = dimensions();

        for (int row = 0; row < s; row++) {
            for (int col = 0; col < s; col++) {
                int tempIndex = col * s + row;
                for (int k = 0; k < s; k++) {
                    temp[tempIndex] += a[k * s + row] * b[col * s + k];
                }
            }
        }
        System.arraycopy(temp, 0, a, 0, a.length);

        return (SelfType) this;
    }

    public AbstractVector<?, D> transform(AbstractVector<?, D> vector) {
        float[] m = this.m;
        float[] v = vector.v;
        int n = v.length;
        float[] temp = new float[v.length];

        for (int row = 0; row < n; row++) {
            for (int k = 0; k < n; k++) {
                temp[row] += m[k*n + row] * v[k];
            }
        }
        System.arraycopy(temp, 0, v, 0, v.length);

        return vector;
    }

    /**
     * Multiply each element of this matrix by a scalar value
     *
     * @param scalar
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType scalar(float scalar) {
        float[] m = this.m;
        int n = m.length;
        for (int i = 0; i < n; i++) {
            m[i] *= scalar;
        }
        return (SelfType) this;
    }

    /**
     * Negate each element of the matrix
     *
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType neg() {
        float[] m = this.m;
        int n = m.length;
        for (int i = 0; i < n; i++) {
            m[i] = -m[i];
        }
        return (SelfType) this;
    }

    /**
     * Transpose the elements of the matrix
     *
     * @return this matrix object - the operation modifies the original object
     */
    public SelfType transpose() {
        float[] m = this.m;
        int s = dimensions();
        for (int row = 0; row < s; row++) {
            for (int col = row + 1; col < s; col++) {
                float t = m[col * s + row];
                m[col * s + row] = m[row * s + col];
                m[row * s + col] = t;
            }
        }
        return (SelfType) this;
    }

    /**
     * Calculate the determinant of the matrix
     *
     * @return the calculated determinant
     */
    abstract public float getDeterminant();

    /**
     * Calculate the inverse of this matrix
     *
     * @return this matrix object - the operation modifies the original object
     */
    abstract public SelfType invert();

    /**
     * Clone this matrix elements, but NOT any push():ed state
     *
     * @return a matrix with the same elements as this matrix
     */
    abstract public SelfType clone();

    /**
     * Store the current matrix state on a stack, which can later be retrieved by calling pop()
     *
     * @return a clone of this matrix
     */
    public SelfType push() {
        SelfType m1 = this.clone();
        m1.next = this;
        return m1;
    }

    /**
     * Retrieve the last stored matrix state
     *
     * @return the last matrix state stored with push()
     * @throws IllegalStateException if there are no stored matrix states
     */
    public SelfType pop() {
        if (next == null) {
            throw new IllegalStateException("Called pop() on an empty stack");
        }
        return next;
    }

    /**
     * Returns a FloatBuffer wrapping the matrix array
     *
     * @return FloatBuffer with the matrix elements
     */
    public FloatBuffer toBuffer() {
        return FloatBuffer.wrap(m);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMatrix matrix = (AbstractMatrix) o;

        if (!Arrays.equals(m, matrix.m)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(m);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        int s = dimensions();
        for (int row = 0; row < s; row++) {
            sb.append("| ");
            for (int col = 0; col < s; col++) {
                sb.append(String.format("%6.2f ", m[col * s + row]));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
