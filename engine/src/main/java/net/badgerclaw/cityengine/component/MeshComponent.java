package net.badgerclaw.cityengine.component;

import net.badgerclaw.cityengine.Mesh;
import net.badgerclaw.cityengine.RenderContext;

import javax.media.opengl.GL3;

public class MeshComponent implements Component {

    private Mesh mesh;

    public MeshComponent(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void init(GL3 gl3) {
        mesh.init(gl3);
    }

    @Override
    public void render(RenderContext renderContext) {
        renderContext.uploadMatrices();
        renderContext.renderMesh(mesh);
    }
}
