package net.badgerclaw.cityengine.math;

/**
 * 2x2 matrix of floats
 * <p>
 * Implementation stores values in a float array; this has been timed to be twice as fast
 * when accessing elements as having them as individual fields.
 * <p>
 * The backing array storage is in column-major order (OpenGL default), so that shaders
 * can use transforms with vector on the right ( v' = M * v )
 */
public class Mat2 extends AbstractMatrix<Mat2, Dimensionality2> implements Dimensionality2, Cloneable {
    final public static int
            M00 = 0,
            M10 = 1,
            M01 = 2,
            M11 = 3;

    public Mat2() {
    }

    /**
     * Construct a 2x2 matrix with the values supplied in row-major order. IE, the following matrix
     * will be built:
     * <p>
     * <pre>
     * | m00  m01 |
     * | m10  m11 |
     * </pre>
     */
    public Mat2(float m00, float m01,
                float m10, float m11) {
        m[M00] = m00;
        m[M01] = m01;
        m[M10] = m10;
        m[M11] = m11;
    }

    @Override
    public Mat2 setIdentity() {
        float[] m = this.m;
        m[M00] = 1f;
        m[M01] = 0f;
        m[M10] = 0f;
        m[M11] = 1f;
        return this;
    }

    @Override
    public Mat2 mul(Mat2 that) {
        float[] a = this.m;
        float[] b = that.m;
        float m00 = a[M00] * b[M00] + a[M01] * b[M10];
        float m01 = a[M00] * b[M01] + a[M01] * b[M11];
        float m10 = a[M10] * b[M00] + a[M11] * b[M10];
        float m11 = a[M10] * b[M01] + a[M11] * b[M11];
        a[M00] = m00;
        a[M10] = m10;
        a[M01] = m01;
        a[M11] = m11;
        return this;
    }

    @Override
    public Mat2 scalar(float scalar) {
        float[] m = this.m;
        m[0] *= scalar;
        m[1] *= scalar;
        m[2] *= scalar;
        m[3] *= scalar;
        return this;
    }

    @Override
    public Mat2 neg() {
        float[] m = this.m;
        m[0] = -m[0];
        m[1] = -m[1];
        m[2] = -m[2];
        m[3] = -m[3];
        return this;
    }

    @Override
    public Mat2 transpose() {
        float m01 = m[M01];
        m[M01] = m[M10];
        m[M10] = m01;
        return this;
    }

    @Override
    public Mat2 invert() {
        final float det = getDeterminant();
        final float m00 = m[M00];
        m[M00] = m[M11] / det;
        m[M01] = -m[M01] / det;
        m[M10] = -m[M10] / det;
        m[M11] = m00 / det;
        return this;
    }

    @Override
    public float getDeterminant() {
        return m[M00] * m[M11] - m[M01] * m[M10];
    }

    @Override
    public Mat2 clone() {
        return copyTo(new Mat2());
    }
}
