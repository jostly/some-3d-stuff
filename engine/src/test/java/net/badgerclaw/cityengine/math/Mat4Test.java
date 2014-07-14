package net.badgerclaw.cityengine.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class Mat4Test {

    // Wolfram Alpha matrix format: {{1,2,3,4},{4,5,6,7},{7,8,9,10},{10,11,12,13}}
    private Mat4 a = new Mat4(
            1, 2, 3, 4,
            4, 5, 6, 7,
            7, 8, 9, 10,
            10, 11, 12, 13);
    // Wolfram Alpha matrix format: {{7,1,10,-2},{3,5,9,11},{-4,6,0,2},{-5,-3,-7,-1}}
    private Mat4 b = new Mat4(
            7, 1, 10, -2,
            3, 5, 9, 11,
            -4, 6, 0, 2,
            -5, -3, -7, -1);

    @Test
    public void equality() {
        assertThat(a, is(new Mat4(1,2,3,4,4,5,6,7,7,8,9,10,10,11,12,13)));
    }

    @Test
    public void non_equality() {
        assertThat(a, is(not(new Mat4(-1,2,3,4,4,5,6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,-2,3,4,4,5,6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,-3,4,4,5,6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,-4,4,5,6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,-4,5,6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,-5,6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,-6,7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,-7,7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,-7,8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,-8,9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,8,-9,10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,8,9,-10,10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,8,9,10,-10,11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,8,9,10,10,-11,12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,8,9,10,10,11,-12,13))));
        assertThat(a, is(not(new Mat4(1,2,3,4,4,5,6,7,7,8,9,10,10,11,12,-13))));
    }

    @Test
    public void cloning() {
        Mat4 theClone = a.clone();
        assertThat(theClone, is(a));
        assertThat("A clone should not be the same instance", theClone, is(not(sameInstance(a))));
        a.setIdentity();
        assertThat("Modifying the original should not modify the clone", theClone, is(not(a)));
    }

    @Test
    public void initialize_to_zero() {
        assertThat(new Mat4(), is(new Mat4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
    }

    @Test
    public void identity() {
        assertThat(a.setIdentity(), is(new Mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)));
    }

    @Test
    public void addition() {
        assertThat(a.add(b), is(new Mat4(8,3,13,2,7,10,15,18,3,14,9,12,5,8,5,12)));
    }

    @Test
    public void subtraction() {
        assertThat(b.sub(a), is(new Mat4(6,-1,7,-6,-1,0,3,4,-11,-2,-9,-8,-15,-14,-19,-14)));
    }

    @Test
    public void matrix_multiplication() {
        assertThat(a.mul(b), is(new Mat4(-19,17,0,22,-16,44,36,52,-13,71,72,82,-10,98,108,112))); // From Wolfram Alpha
    }

    @Test
    public void scalar_multiplication() {
        assertThat(a.scalar(2), is(new Mat4(2,4,6,8,8,10,12,14,14,16,18,20,20,22,24,26)));
    }

    @Test
    public void negation() {
        assertThat(a.neg(), is(new Mat4(-1,-2,-3,-4,-4,-5,-6,-7,-7,-8,-9,-10,-10,-11,-12,-13)));
    }

    @Test
    public void transpose() {
        assertThat(a.transpose(), is(new Mat4(1,4,7,10,2,5,8,11,3,6,9,12,4,7,10,13)));
    }

    @Test
    public void determinant() {
        assertThat(b.getDeterminant(), is(1176f)); // From Wolfram Alpha
    }

    @Test
    public void inverse() {
        assertThat(b.invert(), is(new Mat4(-196, -49, -98, -343,
                -112, -49, 46, -223,
                196, 49, 56, 259,
                -56, 49, -40, -17).scalar(1f / 588f))); // Expected value from Wolfram Alpha, thanks!
    }

    @Test
    public void transform() {
        assertThat(a.transform(new Vec4(10, 20, 30, 40)), is(new Vec4(300, 600, 900, 1200)));
    }

    //@Test
    public void timing() {
        final Mat2 matrix = new Mat2();

        System.out.println("setIdentity() time = " +
                time_in_millis(() -> {
                    for (int i = 0; i < 1000000; i++) {
                        matrix.setIdentity();
                    }
                }) +
                " ms");
    }

    float time_in_millis(Runnable runnable) {
        long t = System.nanoTime();
        runnable.run();
        t = System.nanoTime() - t;
        return (t / 1000000f);
    }
}