package net.badgerclaw.cityengine.math;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class Vec2Test {

    private Vec2 a = new Vec2(1, 3),
            b = new Vec2(3, 4);

    @Test
    public void equality() {
        assertThat(a, is(new Vec2(1, 3)));
    }

    @Test
    public void non_equality() {
        assertThat(a, is(not(new Vec2(-1, 3))));
        assertThat(a, is(not(new Vec2(1, -3))));
    }

    @Test
    public void cloning() {
        Vec2 theClone = a.clone();
        assertThat(theClone, is(a));
        assertThat("A clone should not be the same instance", theClone, is(not(sameInstance(a))));
        a.v[0] = 0;
        assertThat("Modifying the original should not modify the clone", theClone, is(not(a)));
    }

    @Test
    public void initialize_to_zero() {
        assertThat(new Vec2(), is(new Vec2(0, 0)));
    }

    @Test
    public void addition() {
        assertThat(a.add(b), is(new Vec2(4, 7)));
    }

    @Test
    public void subtraction() {
        assertThat(b.sub(a), is(new Vec2(2, 1)));
    }

    @Test
    public void dot_product() {
        assertThat(a.dot(b), is(1f*3f + 3f*4f));
    }

    @Test
    public void scalar_multiplication() {
        assertThat(a.scalar(2f), is(new Vec2(2, 6)));
    }

    @Test
    public void length_squared() {
        assertThat(a.lengthSquared(), is(1f*1f + 3f*3f));
    }

    @Test
    public void length() {
        assertThat(a.length(), is((float)Math.sqrt(1f*1f + 3f*3f)));
    }

    @Test
    public void normalize() {
        assertThat(b.normalize(), is(new Vec2(3f/5f, 4f/5f)));
    }

    @Test
    public void negate() {
        assertThat(a.neg(), is(new Vec2(-1, -3)));
    }


}