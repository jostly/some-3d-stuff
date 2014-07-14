package net.badgerclaw.cityengine.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class Mat2Test {

    private Mat2 a = new Mat2(1, 3, 5, 7);
    private Mat2 b = new Mat2(2, 4, 6, 8);

    @Test
    public void equality() {
        assertThat(a, is(new Mat2(1, 3, 5, 7)));
    }

    @Test
    public void non_equality() {
        assertThat(a, is(not(new Mat2(1, 3, 5, 8))));
        assertThat(a, is(not(new Mat2(1, 3, 4, 7))));
        assertThat(a, is(not(new Mat2(1, 2, 5, 7))));
        assertThat(a, is(not(new Mat2(2, 3, 5, 7))));
    }

    @Test
    public void cloning() {
        Mat2 theClone = a.clone();
        assertThat(theClone, is(a));
        assertThat("A clone should not be the same instance", theClone, is(not(sameInstance(a))));
        a.setIdentity();
        assertThat("Modifying the original should not modify the clone", theClone, is(not(a)));
    }

    @Test
    public void initialize_to_zero() {
        assertThat(new Mat2(), is(new Mat2(0, 0, 0, 0)));
    }

    @Test
    public void identity() {
        assertThat(a.setIdentity(), is(new Mat2(1, 0, 0, 1)));
    }

    @Test
    public void addition() {
        assertThat(a.add(b), is(new Mat2(3, 7, 11, 15)));
    }

    @Test
    public void subtraction() {
        assertThat(b.sub(a), is(new Mat2(1, 1, 1, 1)));
    }

    @Test
    public void matrix_multiplication() {
        assertThat(a.mul(b), is(new Mat2(1*2+3*6, 1*4+3*8, 5*2+7*6, 5*4+7*8)));
    }
    
    @Test
    public void scalar_multiplication() {
        assertThat(a.scalar(2), is(new Mat2(2, 6, 10, 14)));
    }

    @Test
    public void negation() {
        assertThat(a.neg(), is(new Mat2(-1, -3, -5, -7)));
    }

    @Test
    public void transpose() {
        assertThat(a.transpose(), is(new Mat2(1, 5, 3, 7)));
    }

    @Test
    public void determinant() {
        assertThat(a.getDeterminant(), is(1f*7f - 3f*5f));
        assertThat(b.getDeterminant(), is(2f*8f - 4f*6f));
    }

    @Test
    public void inverse() {
        assertThat(a.invert(), is(new Mat2(-7f, 3f, 5f, -1f).scalar(1f/8f))); // Expected value from Wolfram Alpha, thanks!
    }

    @Test
    public void transform() {
        assertThat(a.transform(new Vec2(10, 20)), is(new Vec2(1*10 + 3*20, 5*10 + 7*20)));
    }

    //@Test
    public void timing() {
        final Mat2 matrix = new Mat2();

        System.out.println("setIdentity() time = " +
                time_in_millis(() -> { for(int i = 0; i < 1000000; i++) { matrix.setIdentity(); }}) +
                " ms");
    }

    float time_in_millis(Runnable runnable) {
        long t = System.nanoTime();
        runnable.run();
        t = System.nanoTime() - t;
        return (t / 1000000f);
    }
}