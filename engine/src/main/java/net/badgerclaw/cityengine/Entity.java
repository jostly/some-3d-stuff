package net.badgerclaw.cityengine;

import net.badgerclaw.cityengine.component.Component;
import net.badgerclaw.cityengine.math.Mat4;

import javax.media.opengl.GL3;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    public final Mat4 transformationMatrix = Mat4.identity();
    public final List<Entity> children = new ArrayList<Entity>();
    public final List<Component> components = new ArrayList<Component>();

    public Entity(Component ... components) {
        for (Component c : components) {
            this.components.add(c);
        }
    }

    public Entity addChild(Entity child) {
        children.add(child);
        return this;
    }

    public Entity add(Component component) {
        components.add(component);
        return this;
    }

    public void init(GL3 gl3) {
        for (Component c : components) {
            c.init(gl3);
        }
        for (Entity e : children) {
            e.init(gl3);
        }
    }

    public void render(RenderContext renderContext) {
        Mat4 m = renderContext.getCurrentModelTransformation().push();
        renderContext.setCurrentModelTransformation(m.mul(transformationMatrix));

        for (Component c : components) {
            c.render(renderContext);
        }
        for (Entity e : children) {
            e.render(renderContext);
        }
        renderContext.setCurrentModelTransformation(m.pop());
    }
}
