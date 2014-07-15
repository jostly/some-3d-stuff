package net.badgerclaw.cityengine;

import com.jogamp.opengl.util.GLBuffers;

import javax.media.opengl.GL3;
import java.nio.IntBuffer;

public class Mesh {
    private float[] GREEN_COLOR = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
    private float[] BLUE_COLOR = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
    private float[] RED_COLOR = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
    //
    private float[] YELLOW_COLOR = new float[]{1.0f, 1.0f, 0.0f, 1.0f};
    private float[] CYAN_COLOR = new float[]{0.0f, 1.0f, 1.0f, 1.0f};
    private float[] MAGENTA_COLOR = new float[]{1.0f, 0.0f, 1.0f, 1.0f};
    private float[] vertexData = new float[]{
            //Front
            +1.0f, +1.0f, +1.0f,
            +1.0f, -1.0f, +1.0f,
            -1.0f, -1.0f, +1.0f,
            -1.0f, +1.0f, +1.0f,
            //Top
            +1.0f, +1.0f, +1.0f,
            -1.0f, +1.0f, +1.0f,
            -1.0f, +1.0f, -1.0f,
            +1.0f, +1.0f, -1.0f,
            //Left
            +1.0f, +1.0f, +1.0f,
            +1.0f, +1.0f, -1.0f,
            +1.0f, -1.0f, -1.0f,
            +1.0f, -1.0f, +1.0f,
            //Back
            +1.0f, +1.0f, -1.0f,
            -1.0f, +1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            +1.0f, -1.0f, -1.0f,
            //Bottom
            +1.0f, -1.0f, +1.0f,
            +1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, +1.0f,
            //Right
            -1.0f, +1.0f, +1.0f,
            -1.0f, -1.0f, +1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, +1.0f, -1.0f,

            // Normals
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,

            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,

            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,

            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,

            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,

            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            //
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            //
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            //
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            //
            YELLOW_COLOR[0], YELLOW_COLOR[1], YELLOW_COLOR[2], YELLOW_COLOR[3],
            YELLOW_COLOR[0], YELLOW_COLOR[1], YELLOW_COLOR[2], YELLOW_COLOR[3],
            YELLOW_COLOR[0], YELLOW_COLOR[1], YELLOW_COLOR[2], YELLOW_COLOR[3],
            YELLOW_COLOR[0], YELLOW_COLOR[1], YELLOW_COLOR[2], YELLOW_COLOR[3],
            //
            CYAN_COLOR[0], CYAN_COLOR[1], CYAN_COLOR[2], CYAN_COLOR[3],
            CYAN_COLOR[0], CYAN_COLOR[1], CYAN_COLOR[2], CYAN_COLOR[3],
            CYAN_COLOR[0], CYAN_COLOR[1], CYAN_COLOR[2], CYAN_COLOR[3],
            CYAN_COLOR[0], CYAN_COLOR[1], CYAN_COLOR[2], CYAN_COLOR[3],
            //
            MAGENTA_COLOR[0], MAGENTA_COLOR[1], MAGENTA_COLOR[2], MAGENTA_COLOR[3],
            MAGENTA_COLOR[0], MAGENTA_COLOR[1], MAGENTA_COLOR[2], MAGENTA_COLOR[3],
            MAGENTA_COLOR[0], MAGENTA_COLOR[1], MAGENTA_COLOR[2], MAGENTA_COLOR[3],
            MAGENTA_COLOR[0], MAGENTA_COLOR[1], MAGENTA_COLOR[2], MAGENTA_COLOR[3]};

    int numberOfVertices = 24;

    private int[] indexData = new int[]{
            0, 1, 2,
            2, 3, 0,
            //
            4, 5, 6,
            6, 7, 4,
            //
            8, 9, 10,
            10, 11, 8,
            //
            12, 13, 14,
            14, 15, 12,
            //
            16, 17, 18,
            18, 19, 16,
            //
            20, 21, 22,
            22, 23, 20};

    private int[] bufferObject = new int[2];
    private int[] vertexArrayObject = new int[1];
    private boolean initialized = false;

    private void initializeVertexArray(GL3 gl3) {

        gl3.glGenVertexArrays(1, IntBuffer.wrap(vertexArrayObject));
        gl3.glBindVertexArray(vertexArrayObject[0]);

        gl3.glGenBuffers(bufferObject.length, IntBuffer.wrap(bufferObject));

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferObject[0]);
        {
            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertexData.length * 4, GLBuffers.newDirectFloatBuffer(vertexData), GL3.GL_STATIC_DRAW);

            gl3.glEnableVertexAttribArray(0);
            gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);

            int normalDataOffset = numberOfVertices * 3 * 4;
            gl3.glEnableVertexAttribArray(2);
            gl3.glVertexAttribPointer(2, 3, GL3.GL_FLOAT, false, 0, normalDataOffset);

            int colorDataOffset = numberOfVertices * 3 * 4 + normalDataOffset;
            gl3.glEnableVertexAttribArray(1);
            gl3.glVertexAttribPointer(1, 4, GL3.GL_FLOAT, false, 0, colorDataOffset);
        }

        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, bufferObject[1]);
        {
            gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, indexData.length * 4, GLBuffers.newDirectIntBuffer(indexData), GL3.GL_STATIC_DRAW);
        }

        gl3.glBindVertexArray(0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void init(GL3 gl3) {
        if (!initialized) {
            initializeVertexArray(gl3);
            initialized = true;
        }
    }

    public void render(GL3 gl3) {
        gl3.glDrawElements(GL3.GL_TRIANGLES, indexData.length, GL3.GL_UNSIGNED_INT, 0);
    }
}
