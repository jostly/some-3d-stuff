package net.badgerclaw.cityengine;

import net.badgerclaw.cityengine.math.Mat4;

public interface RenderContext {

    Mat4 getCurrentModelTransformation();

    void uploadMatrices();

    void renderMesh(Mesh mesh);

    void setCurrentModelTransformation(Mat4 modelTransformation);
}
