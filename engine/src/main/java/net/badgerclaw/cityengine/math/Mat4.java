package net.badgerclaw.cityengine.math;

/**
 * 4x4 matrix of floats
 */
public class Mat4 extends AbstractMatrix<Mat4, Dimensionality4> implements Dimensionality4 {
    final public static int
            M00 = 0,
            M10 = 1,
            M20 = 2,
            M30 = 3,
            M01 = 4,
            M11 = 5,
            M21 = 6,
            M31 = 7,
            M02 = 8,
            M12 = 9,
            M22 = 10,
            M32 = 11,
            M03 = 12,
            M13 = 13,
            M23 = 14,
            M33 = 15;

    public Mat4() {
    }

    public Mat4(float m00, float m01, float m02, float m03,
                float m10, float m11, float m12, float m13,
                float m20, float m21, float m22, float m23,
                float m30, float m31, float m32, float m33) {
        m[M00] = m00;
        m[M01] = m01;
        m[M02] = m02;
        m[M03] = m03;
        m[M10] = m10;
        m[M11] = m11;
        m[M12] = m12;
        m[M13] = m13;
        m[M20] = m20;
        m[M21] = m21;
        m[M22] = m22;
        m[M23] = m23;
        m[M30] = m30;
        m[M31] = m31;
        m[M32] = m32;
        m[M33] = m33;
    }

    @Override
    public float getDeterminant() {
        float f = m[M00]
                * ((m[M11] * m[M22] * m[M33] + m[M12] * m[M23] * m[M31] + m[M13] * m[M21] * m[M32])
                - m[M13] * m[M22] * m[M31]
                - m[M11] * m[M23] * m[M32]
                - m[M12] * m[M21] * m[M33]);
        f -= m[M01]
                * ((m[M10] * m[M22] * m[M33] + m[M12] * m[M23] * m[M30] + m[M13] * m[M20] * m[M32])
                - m[M13] * m[M22] * m[M30]
                - m[M10] * m[M23] * m[M32]
                - m[M12] * m[M20] * m[M33]);
        f += m[M02]
                * ((m[M10] * m[M21] * m[M33] + m[M11] * m[M23] * m[M30] + m[M13] * m[M20] * m[M31])
                - m[M13] * m[M21] * m[M30]
                - m[M10] * m[M23] * m[M31]
                - m[M11] * m[M20] * m[M33]);
        f -= m[M03]
                * ((m[M10] * m[M21] * m[M32] + m[M11] * m[M22] * m[M30] + m[M12] * m[M20] * m[M31])
                - m[M12] * m[M21] * m[M30]
                - m[M10] * m[M22] * m[M31]
                - m[M11] * m[M20] * m[M32]);
        return f;
    }

    /**
     * Calculate the determinant of a 3x3 matrix
     *
     * @return result
     */

    private float determinant3x3(float t00, float t01, float t02,
                                 float t10, float t11, float t12,
                                 float t20, float t21, float t22) {
        return t00 * (t11 * t22 - t12 * t21)
                + t01 * (t12 * t20 - t10 * t22)
                + t02 * (t10 * t21 - t11 * t20);
    }

    @Override
    public Mat4 invert() {
        float[] m = this.m;
        float determinant = getDeterminant();
        float determinant_inv = 1f / determinant;

        // first row
        float t00 = determinant3x3(m[M11], m[M12], m[M13], m[M21], m[M22], m[M23], m[M31], m[M32], m[M33]);
        float t01 = -determinant3x3(m[M10], m[M12], m[M13], m[M20], m[M22], m[M23], m[M30], m[M32], m[M33]);
        float t02 = determinant3x3(m[M10], m[M11], m[M13], m[M20], m[M21], m[M23], m[M30], m[M31], m[M33]);
        float t03 = -determinant3x3(m[M10], m[M11], m[M12], m[M20], m[M21], m[M22], m[M30], m[M31], m[M32]);
        // second row
        float t10 = -determinant3x3(m[M01], m[M02], m[M03], m[M21], m[M22], m[M23], m[M31], m[M32], m[M33]);
        float t11 = determinant3x3(m[M00], m[M02], m[M03], m[M20], m[M22], m[M23], m[M30], m[M32], m[M33]);
        float t12 = -determinant3x3(m[M00], m[M01], m[M03], m[M20], m[M21], m[M23], m[M30], m[M31], m[M33]);
        float t13 = determinant3x3(m[M00], m[M01], m[M02], m[M20], m[M21], m[M22], m[M30], m[M31], m[M32]);
        // third row
        float t20 = determinant3x3(m[M01], m[M02], m[M03], m[M11], m[M12], m[M13], m[M31], m[M32], m[M33]);
        float t21 = -determinant3x3(m[M00], m[M02], m[M03], m[M10], m[M12], m[M13], m[M30], m[M32], m[M33]);
        float t22 = determinant3x3(m[M00], m[M01], m[M03], m[M10], m[M11], m[M13], m[M30], m[M31], m[M33]);
        float t23 = -determinant3x3(m[M00], m[M01], m[M02], m[M10], m[M11], m[M12], m[M30], m[M31], m[M32]);
        // fourth row
        float t30 = -determinant3x3(m[M01], m[M02], m[M03], m[M11], m[M12], m[M13], m[M21], m[M22], m[M23]);
        float t31 = determinant3x3(m[M00], m[M02], m[M03], m[M10], m[M12], m[M13], m[M20], m[M22], m[M23]);
        float t32 = -determinant3x3(m[M00], m[M01], m[M03], m[M10], m[M11], m[M13], m[M20], m[M21], m[M23]);
        float t33 = determinant3x3(m[M00], m[M01], m[M02], m[M10], m[M11], m[M12], m[M20], m[M21], m[M22]);

        // transpose and divide by the determinant
        m[M00] = t00 * determinant_inv;
        m[M11] = t11 * determinant_inv;
        m[M22] = t22 * determinant_inv;
        m[M33] = t33 * determinant_inv;
        m[M01] = t10 * determinant_inv;
        m[M10] = t01 * determinant_inv;
        m[M20] = t02 * determinant_inv;
        m[M02] = t20 * determinant_inv;
        m[M12] = t21 * determinant_inv;
        m[M21] = t12 * determinant_inv;
        m[M03] = t30 * determinant_inv;
        m[M30] = t03 * determinant_inv;
        m[M13] = t31 * determinant_inv;
        m[M31] = t13 * determinant_inv;
        m[M32] = t23 * determinant_inv;
        m[M23] = t32 * determinant_inv;
        return this;
    }

    @Override
    public Mat4 clone() {
        return copyTo(new Mat4());
    }

    public Mat3 toMat3() {
        return new Mat3(
                m[M00], m[M01], m[M02],
                m[M10], m[M11], m[M12],
                m[M20], m[M21], m[M22]
        );
    }

    public Vec3 transform(Vec3 vector) {
        float[] m = this.m;
        float[] v = vector.v;

        float x = m[M00] * v[Vec3.X] + m[M01] * v[Vec3.Y] + m[M02] * v[Vec3.Z];
        float y = m[M10] * v[Vec3.X] + m[M11] * v[Vec3.Y] + m[M12] * v[Vec3.Z];
        float z = m[M20] * v[Vec3.X] + m[M21] * v[Vec3.Y] + m[M22] * v[Vec3.Z];

        v[Vec3.X] = x;
        v[Vec3.Y] = y;
        v[Vec3.Z] = z;
        return vector;
    }

    public Mat4 setColumn(int col, Vec3 vector) {
        float[] m = this.m;
        float[] v = vector.v;

        for (int row = 0; row < 3; row++) {
            m[col * 4 + row] = v[row];
        }
        return this;
    }

    public static Mat4 identity() {
        return new Mat4().setIdentity();
    }

    /**
     * Create a matrix describing a rotation around an arbitrary axis
     *
     * @param x     the x component of the rotation axis
     * @param y     the y component of the rotation axis
     * @param z     the z component of the rotation axis
     * @param angle the angle of rotation around the axis
     * @return a matrix describing the rotation
     */
    public static Mat4 rotation(final float x, final float y, final float z, final float angle) {
        Mat4 matrix = new Mat4();
        final float s = (float) Math.sin(angle);
        final float c = (float) Math.cos(angle);
        final float ic = 1f - c;
        final float xSquared = x * x;
        final float ySquared = y * y;
        final float zSquared = z * z;
        final float[] m = matrix.m;

        m[M00] = xSquared + (1 - xSquared) * c;
        m[M01] = ic * x * y - z * s;
        m[M02] = ic * x * z + y * s;

        m[M10] = ic * x * y + z * s;
        m[M11] = ySquared + (1 - ySquared) * c;
        m[M12] = ic * y * z - x * s;

        m[M20] = ic * x * z - y * s;
        m[M21] = ic * y * z + x * s;
        m[M22] = zSquared + (1 - zSquared) * c;

        m[M33] = 1;

        return matrix;
    }

    /**
     * Create a matrix describing a rotation around an arbitrary axis
     *
     * @param axis  the rotation axis
     * @param angle the angle of rotation around the axis
     * @return a matrix describing the rotation
     */
    public static Mat4 rotation(Vec3 axis, float angle) {
        return rotation(axis.v[Vec3.X], axis.v[Vec3.Y], axis.v[Vec3.Z], angle);
    }

    /**
     * Create a matrix describing a translation
     *
     * @param x the x component of the translation
     * @param y the y component of the translation
     * @param z the z component of the translation
     * @return a matrix describing the translation
     */
    public static Mat4 translation(float x, float y, float z) {
        Mat4 matrix = identity();

        float[] m = matrix.m;
        m[M03] = x;
        m[M13] = y;
        m[M23] = z;

        return matrix;
    }

    /**
     * Create a matrix describing a translation
     *
     * @param t the translation vector
     * @return a matrix describing the translation
     */
    public static Mat4 translation(Vec3 t) {
        return translation(t.v[Vec3.X], t.v[Vec3.Y], t.v[Vec3.Z]);
    }

    public static Mat4 scaling(float xScale, float yScale, float zScale) {
        Mat4 matrix = new Mat4();

        float[] m = matrix.m;
        m[M00] = xScale;
        m[M11] = yScale;
        m[M22] = zScale;
        m[M33] = 1f;

        return matrix;
    }

    public static Mat4 scaling(Vec3 s) {
        return scaling(s.v[Vec3.X], s.v[Vec3.Y], s.v[Vec3.Z]);
    }

    public static Mat4 perspectiveProjection(float fieldOfView, float aspectRatio) {
        float fFrustumScale = (float) (1.0 / Math.tan(0.5 * fieldOfView * Math.PI / 180.0));
        float fzNear = 0.5f;
        float fzFar = 100.0f;

        Mat4 perspectiveMatrix = new Mat4();

        perspectiveMatrix.m[0] = fFrustumScale / aspectRatio;
        perspectiveMatrix.m[5] = fFrustumScale;
        perspectiveMatrix.m[10] = (fzFar + fzNear) / (fzNear - fzFar);
        perspectiveMatrix.m[14] = (2 * fzFar * fzNear) / (fzNear - fzFar);
        perspectiveMatrix.m[11] = -1.0f;

        return perspectiveMatrix;
    }
}
