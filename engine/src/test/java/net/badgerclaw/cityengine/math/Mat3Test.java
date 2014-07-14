package net.badgerclaw.cityengine.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class Mat3Test {

    private Mat3 a = new Mat3(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private Mat3 b = new Mat3(2, 4, 6, 8, 10, 12, 14, 16, 20);

    @Test
    public void equality() {
        assertThat(a, is(new Mat3(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void non_equality() {
        assertThat(a, is(not(new Mat3(-1, 2, 3, 4, 5, 6, 7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, -2, 3, 4, 5, 6, 7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, -3, 4, 5, 6, 7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, 3, -4, 5, 6, 7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, 3, 4, -5, 6, 7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, 3, 4, 5, -6, 7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, 3, 4, 5, 6, -7, 8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, 3, 4, 5, 6, 7, -8, 9))));
        assertThat(a, is(not(new Mat3(1, 2, 3, 4, 5, 6, 7, 8, -9))));
    }

    @Test
    public void cloning() {
        Mat3 theClone = a.clone();
        assertThat(theClone, is(a));
        assertThat("A clone should not be the same instance", theClone, is(not(sameInstance(a))));
        a.setIdentity();
        assertThat("Modifying the original should not modify the clone", theClone, is(not(a)));
    }

    @Test
    public void initialize_to_zero() {
        assertThat(new Mat3(), is(new Mat3(0, 0, 0, 0, 0, 0, 0, 0, 0)));
    }

    @Test
    public void identity() {
        assertThat(a.setIdentity(), is(new Mat3(1, 0, 0, 0, 1, 0, 0, 0, 1)));
    }

    @Test
    public void addition() {
        assertThat(a.add(b), is(new Mat3(3, 6, 9, 12, 15, 18, 21, 24, 29)));
    }

    @Test
    public void subtraction() {
        assertThat(b.sub(a), is(new Mat3(1, 2, 3, 4, 5, 6, 7, 8, 11)));
    }

    @Test
    public void matrix_multiplication() {
        assertThat(a.mul(b), is(new Mat3(
                1 * 2 + 2 * 8 + 3 * 14, 1 * 4 + 2 * 10 + 3 * 16, 1 * 6 + 2 * 12 + 3 * 20,
                4 * 2 + 5 * 8 + 6 * 14, 4 * 4 + 5 * 10 + 6 * 16, 4 * 6 + 5 * 12 + 6 * 20,
                7 * 2 + 8 * 8 + 9 * 14, 7 * 4 + 8 * 10 + 9 * 16, 7 * 6 + 8 * 12 + 9 * 20)));
    }

    @Test
    public void scalar_multiplication() {
        assertThat(a.scalar(2), is(new Mat3(2, 4, 6, 8, 10, 12, 14, 16, 18)));
    }

    @Test
    public void negation() {
        assertThat(a.neg(), is(new Mat3(-1, -2, -3, -4, -5, -6, -7, -8, -9)));
    }

    @Test
    public void transpose() {
        assertThat(a.transpose(), is(new Mat3(1, 4, 7, 2, 5, 8, 3, 6, 9)));
    }

    @Test
    public void determinant() {
        assertThat(b.getDeterminant(), is(-24f)); // From Wolfram Alpha
    }

    @Test
    public void inverse() {
        assertThat(b.invert(), is(new Mat3(-2, -4, 3, -2, 11, -6, 3, -6, 3).scalar(1f / 6f))); // Expected value from Wolfram Alpha, thanks!
    }

    @Test
    public void transform() {
        assertThat(a.transform(new Vec3(10, 20, 30)), is(new Vec3(1*10 + 2*20 + 3*30, 4*10 + 5*20 + 6*30, 7*10 + 8*20 + 9*30)));
    }

    public void timing() {
        final Mat3 matrix = new Mat3();

        System.out.println("scalar() time = " +
                time_in_millis(() -> {
                    for (int i = 0; i < 1000000; i++) {
                        matrix.scalar(0.99f);
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