package net.badgerclaw.cityengine.math;

public class Mat3 extends AbstractMatrix<Mat3, Dimensionality3> implements Dimensionality3, Cloneable {
    final public static int
            M00 = 0,
            M10 = 1,
            M20 = 2,
            M01 = 3,
            M11 = 4,
            M21 = 5,
            M02 = 6,
            M12 = 7,
            M22 = 8;

    public Mat3() {
    }

    /**
     * Construct a matrix with the values supplied in row-major order. IE, the following matrix
     * will be built:
     * <p>
     * <pre>
     * | m00 m01 m02 |
     * | m10 m11 m12 |
     * | m20 m21 m22 |
     * </pre>
     */
    public Mat3(float m00, float m01, float m02,
                float m10, float m11, float m12,
                float m20, float m21, float m22) {
        m[M00] = m00;
        m[M01] = m01;
        m[M02] = m02;
        m[M10] = m10;
        m[M11] = m11;
        m[M12] = m12;
        m[M20] = m20;
        m[M21] = m21;
        m[M22] = m22;
    }

    @Override
    public Mat3 setIdentity() {
        float[] m = this.m;
        m[M00] = 1f;
        m[M01] = 0f;
        m[M02] = 0f;
        m[M10] = 0f;
        m[M11] = 1f;
        m[M12] = 0f;
        m[M20] = 0f;
        m[M21] = 0f;
        m[M22] = 1f;
        return this;
    }

    @Override
    public Mat3 mul(Mat3 that) {
        float[] a = this.m;
        float[] b = that.m;
        float m00 = a[M00] * b[M00] + a[M01] * b[M10] + a[M02] * b[M20];
        float m01 = a[M00] * b[M01] + a[M01] * b[M11] + a[M02] * b[M21];
        float m02 = a[M00] * b[M02] + a[M01] * b[M12] + a[M02] * b[M22];
        float m10 = a[M10] * b[M00] + a[M11] * b[M10] + a[M12] * b[M20];
        float m11 = a[M10] * b[M01] + a[M11] * b[M11] + a[M12] * b[M21];
        float m12 = a[M10] * b[M02] + a[M11] * b[M12] + a[M12] * b[M22];
        float m20 = a[M20] * b[M00] + a[M21] * b[M10] + a[M22] * b[M20];
        float m21 = a[M20] * b[M01] + a[M21] * b[M11] + a[M22] * b[M21];
        float m22 = a[M20] * b[M02] + a[M21] * b[M12] + a[M22] * b[M22];
        a[M00] = m00;
        a[M01] = m01;
        a[M02] = m02;
        a[M10] = m10;
        a[M11] = m11;
        a[M12] = m12;
        a[M20] = m20;
        a[M21] = m21;
        a[M22] = m22;
        return this;
    }

    @Override
    public Mat3 scalar(float scalar) {
        float[] m = this.m;
        m[0] *= scalar;
        m[1] *= scalar;
        m[2] *= scalar;
        m[3] *= scalar;
        m[4] *= scalar;
        m[5] *= scalar;
        m[6] *= scalar;
        m[7] *= scalar;
        m[8] *= scalar;
        return this;
    }

    @Override
    public Mat3 neg() {
        float[] m = this.m;
        m[0] = -m[0];
        m[1] = -m[1];
        m[2] = -m[2];
        m[3] = -m[3];
        m[4] = -m[4];
        m[5] = -m[5];
        m[6] = -m[6];
        m[7] = -m[7];
        m[8] = -m[8];
        return this;
    }

    @Override
    public float getDeterminant() {
        float[] m = this.m;
        return m[M00] * (m[M11] * m[M22] - m[M12] * m[M21])
                + m[M01] * (m[M12] * m[M20] - m[M10] * m[M22])
                + m[M02] * (m[M10] * m[M21] - m[M11] * m[M20]);
    }

    @Override
    public Mat3 invert() {
        float[] m = this.m;
        float determinant = getDeterminant();
        float determinant_inv = 1f / determinant;

        float t00 = m[M11] * m[M22] - m[M12] * m[M21];
        float t01 = - m[M10] * m[M22] + m[M12] * m[M20];
        float t02 = m[M10] * m[M21] - m[M11] * m[M20];
        float t10 = - m[M01] * m[M22] + m[M02] * m[M21];
        float t11 = m[M00] * m[M22] - m[M02] * m[M20];
        float t12 = - m[M00] * m[M21] + m[M01] * m[M20];
        float t20 = m[M01] * m[M12] - m[M02] * m[M11];
        float t21 = -m[M00] * m[M12] + m[M02] * m[M10];
        float t22 = m[M00] * m[M11] - m[M01] * m[M10];

        m[M00] = t00*determinant_inv;
        m[M11] = t11*determinant_inv;
        m[M22] = t22*determinant_inv;
        m[M01] = t10*determinant_inv;
        m[M10] = t01*determinant_inv;
        m[M20] = t02*determinant_inv;
        m[M02] = t20*determinant_inv;
        m[M12] = t21*determinant_inv;
        m[M21] = t12*determinant_inv;
        return this;
    }

    @Override
    public Mat3 clone() {
        return copyTo(new Mat3());
    }
}
