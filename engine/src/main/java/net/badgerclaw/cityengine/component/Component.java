package net.badgerclaw.cityengine.component;

import net.badgerclaw.cityengine.RenderContext;

import javax.media.opengl.GL3;

public interface Component {
    default void init(GL3 gl3) {

    }

    default void render(RenderContext renderContext) {

    }
}
