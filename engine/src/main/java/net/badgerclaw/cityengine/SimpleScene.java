package net.badgerclaw.cityengine;

import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import net.badgerclaw.cityengine.component.MeshComponent;
import net.badgerclaw.cityengine.math.AbstractMatrix;
import net.badgerclaw.cityengine.math.Mat4;
import net.badgerclaw.cityengine.math.Vec2;
import net.badgerclaw.cityengine.math.Vec3;

import javax.media.nativewindow.util.Point;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.awt.*;
import java.nio.IntBuffer;

public class SimpleScene implements GLEventListener, KeyListener, MouseListener, WindowListener, RenderContext {

    private ShaderProgram program = new ShaderProgram();

    private final long startingTime = System.currentTimeMillis();

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
    private int[] uniformBufferObject = new int[1];

    private float aspectRatio = 1f;
    private final float fieldOfView = 80f;

    private final int globalMatricesBindingIndex = 0;

    private boolean moveForward, moveBackward, strafeLeft, strafeRight;
    private Vec3 cameraPosition = new Vec3(0, 0, 8);

    private Vec3 cameraForward = new Vec3(0, 0, 1);
    private Vec3 cameraRight = new Vec3(1, 0, 0);
    private Vec3 cameraUp = new Vec3(0, 1, 0);
    private Vec2 lastMousePosition = new Vec2();
    private float mouseDeltaX, mouseDeltaY;
    private Object mouseSyncLock = new Object();

    private Mat4 worldToCamera = new Mat4();
    private Mat4 transformationStack;

    private int centerX, centerY;
    private int top, left;

    private Robot robot;

    private GL3 currentFrameGL3;

    private Entity root;
    private Entity ground;
    private Entity blockContainer;
    private Entity rod;

    public SimpleScene(GLWindow window) {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        recalcCenter(window);

        Mesh mesh = new Mesh();

        root = new Entity();

        ground = new Entity(new MeshComponent(mesh));
        ground.transformationMatrix
                .mul(Mat4.translation(0, -4f, 0f))
                .mul(Mat4.scaling(100f, 0.1f, 100f));

        blockContainer = new Entity();

        Entity block = new Entity(new MeshComponent(mesh));
        block.transformationMatrix
                .mul(Mat4.scaling(1f, 1f, 0.2f));

        blockContainer.addChild(block);

        rod = new Entity(new MeshComponent(mesh));

        blockContainer.addChild(rod);

        root.addChild(blockContainer);
        root.addChild(ground);
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL3 gl3 = drawable.getGL().getGL3();

        gl3.glGenBuffers(1, uniformBufferObject, 0);
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, uniformBufferObject[0]);
        gl3.glBufferData(GL3.GL_UNIFORM_BUFFER, 16 * 4, null, GL3.GL_STREAM_DRAW);
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, 0);

        buildShaders(gl3);

        initializeVertexArray(gl3);

        gl3.glEnable(GL3.GL_CULL_FACE);
        gl3.glCullFace(GL3.GL_BACK);
        gl3.glFrontFace(GL3.GL_CW);

        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glDepthMask(true);
        gl3.glDepthFunc(GL3.GL_LEQUAL);
        gl3.glDepthRangef(0.0f, 1.0f);

        root.init(gl3);
    }

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

    private void buildShaders(GL3 gl3) {
        ShaderCode vertex = ShaderCode.create(gl3, GL2ES2.GL_VERTEX_SHADER, this.getClass(), "shaders", "shaders/bin", "basic", false);
        ShaderCode fragment = ShaderCode.create(gl3, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(), "shaders", "shaders/bin", "basic", false);

        program.init(gl3);

        program.add(gl3, vertex, System.out);
        program.add(gl3, fragment, System.out);

        program.link(gl3, System.out);

        int programId = program.id();

        int globalUniformBlockIndex = gl3.glGetUniformBlockIndex(programId, "GlobalMatrices");
        gl3.glUniformBlockBinding(programId, globalUniformBlockIndex, globalMatricesBindingIndex);

        gl3.glBindBufferRange(GL3.GL_UNIFORM_BUFFER, globalMatricesBindingIndex, uniformBufferObject[0], 0, 16 * 4);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        program.release(drawable.getGL().getGL3());
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        float time = (System.currentTimeMillis() - startingTime) / 1000f;

        float speed = time * 0.03f;

        if (moveForward) {
            cameraPosition.add(cameraForward.clone().scalar(-speed));
        }
        if (moveBackward) {
            cameraPosition.sub(cameraForward.clone().scalar(-speed));
        }
        if (strafeLeft) {
            cameraPosition.sub(cameraRight.clone().scalar(speed));
        }
        if (strafeRight) {
            cameraPosition.add(cameraRight.clone().scalar(speed));
        }

        float rotationSpeed = time * 0.001f;

        synchronized (mouseSyncLock) {
            Mat4 rotation = Mat4.rotation(0, 1, 0, -mouseDeltaX * rotationSpeed);
            //Mat4.rotation(1, 0, 0, mouseDeltaY * rotationSpeed);
            rotation.transform(cameraForward);
            rotation.transform(cameraRight);
            rotation.transform(cameraUp);
            cameraForward.normalize();
            cameraRight.normalize();
            cameraUp.normalize();

            rotation = Mat4.rotation(cameraRight, -mouseDeltaY * rotationSpeed);

            rotation.transform(cameraForward);
            rotation.transform(cameraUp);
            cameraForward.normalize();
            cameraUp.normalize();

            mouseDeltaY = 0;
            mouseDeltaX = 0;
        }

        GL3 gl3 = drawable.getGL().getGL3();
        currentFrameGL3 = gl3;

        gl3.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        Mat4 cameraToClipMatrix = Mat4.perspectiveProjection(fieldOfView, aspectRatio);
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, uniformBufferObject[0]);
        gl3.glBufferSubData(GL3.GL_UNIFORM_BUFFER, 0, 16 * 4, cameraToClipMatrix.toBuffer());
        gl3.glBindBuffer(GL3.GL_UNIFORM_BUFFER, 0);

        worldToCamera.setIdentity();
        worldToCamera.setColumn(0, cameraRight);
        worldToCamera.setColumn(1, cameraUp);
        worldToCamera.setColumn(2, cameraForward);
        worldToCamera.setColumn(3, cameraPosition);

        Mat4 worldToCameraInverse = worldToCamera.clone().invert();

        transformationStack = worldToCameraInverse;

        program.useProgram(gl3, true);
        {

            gl3.glBindVertexArray(vertexArrayObject[0]);

            /*
            Mat4 ground = worldToCameraInverse.clone()
                    .mul(Mat4.translation(0, -4f, 0f))
                    .mul(Mat4.scaling(100f, 0.1f, 100f));
            setUniform(gl3, "modelToCameraMatrix", ground);
            setUniform(gl3, "normalModelToCameraMatrix", worldToCamera.clone().mul(ground).toMat3());
            gl3.glDrawElements(GL3.GL_TRIANGLES, indexData.length, GL3.GL_UNSIGNED_INT, 0);
            */

            blockContainer.transformationMatrix.setIdentity()
                    .mul(Mat4.rotation(new Vec3(1f, 0.0f, 1f).normalize(), time * 0.7f));


            /*
            Mat4 m1 = worldToCameraInverse.clone()
                    .mul(Mat4.rotation(new Vec3(1f, 0.0f, 1f).normalize(), time * 0.7f))
                    .push()
                    .mul(Mat4.scaling(1f, 1f, 0.2f));
            setUniform(gl3, "modelToCameraMatrix", m1);
            setUniform(gl3, "normalModelToCameraMatrix", worldToCamera.clone().mul(m1).toMat3());
            gl3.glDrawElements(GL3.GL_TRIANGLES, indexData.length, GL3.GL_UNSIGNED_INT, 0);
            */

            rod.transformationMatrix.setIdentity()
                    .mul(Mat4.translation(0f, 0f, 1.2f))
                    .mul(Mat4.scaling(0.25f, 0.25f, 1f))
                    .mul(Mat4.rotation(new Vec3(0f, 0f, 1f), time * 4.2f));

            /*
            setUniform(gl3, "modelToCameraMatrix", m1);
            setUniform(gl3, "normalModelToCameraMatrix", worldToCamera.clone().mul(m1).toMat3());
            gl3.glDrawElements(GL3.GL_TRIANGLES, indexData.length, GL3.GL_UNSIGNED_INT, 0);
            */
            root.render(this);
        }
        program.useProgram(gl3, false);
    }

    private void setUniform(GL3 gl3, String name, AbstractMatrix matrix) {
        int id = gl3.glGetUniformLocation(program.id(), name);
        if (id == -1) {
            throw new IllegalArgumentException("Invalid uniform parameter " + name);
        }
        switch (matrix.dimensions()) {
            case 2:
                gl3.glUniformMatrix2fv(id, 1, false, matrix.m, 0);
                break;
            case 3:
                gl3.glUniformMatrix3fv(id, 1, false, matrix.m, 0);
                break;
            case 4:
                gl3.glUniformMatrix4fv(id, 1, false, matrix.m, 0);
                break;
            default:
                throw new IllegalArgumentException("Cannot set a uniform with a matrix of dimensionality " + matrix.dimensions());
        }
    }

    private void setUniform(GL3 gl3, String name, float... val) {
        int id = gl3.glGetUniformLocation(program.id(), name);
        if (id == -1) {
            System.err.println("Warning: Invalid uniform parameter " + name);
            return;
        }
        switch (val.length) {
            case 1:
                gl3.glUniform1fv(id, 1, val, 0);
                break;
            case 2:
                gl3.glUniform2fv(id, 1, val, 0);
                break;
            case 3:
                gl3.glUniform3fv(id, 1, val, 0);
                break;
            case 4:
                gl3.glUniform4fv(id, 1, val, 0);
                break;
            default:
                throw new IllegalArgumentException("Cannot set a uniform of " + val.length + " values");
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        aspectRatio = (float) width / (float) height;
        GL3 gl3 = drawable.getGL().getGL3();
        gl3.glViewport(x, y, width, height);
    }

    @Override
    public Mat4 getCurrentModelTransformation() {
        return transformationStack;
    }

    @Override
    public void setCurrentModelTransformation(Mat4 modelTransformation) {
        transformationStack = modelTransformation;
    }

    @Override
    public void uploadMatrices() {
        setUniform(currentFrameGL3, "modelToCameraMatrix", transformationStack);
        setUniform(currentFrameGL3, "normalModelToCameraMatrix", worldToCamera.clone().mul(transformationStack).toMat3());
    }

    @Override
    public void renderMesh(Mesh mesh) {
        mesh.render(currentFrameGL3);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                moveForward = true;
                break;
            case 's':
                moveBackward = true;
                break;
            case 'a':
                strafeLeft = true;
                break;
            case 'd':
                strafeRight = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                moveForward = false;
                break;
            case 's':
                moveBackward = false;
                break;
            case 'a':
                strafeLeft = false;
                break;
            case 'd':
                strafeRight = false;
                break;
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        robot.mouseMove(centerX, centerY);
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int dx = e.getX() - centerX;
        int dy = e.getY() - centerY;
        if (dx != 0 || dy != 0) {
            synchronized (mouseSyncLock) {
                mouseDeltaX += dx;
                mouseDeltaY += dy;
            }
            robot.mouseMove(centerX + left, centerY + top);
        }
    }

    private void recalcCenter(GLWindow window) {
        Point p = new Point();
        window.getLocationOnScreen(p);

        left = window.getX();//p.getX();
        centerX = left + window.getWidth() / 2;
        top = window.getY();//p.getY();
        centerY = top + window.getHeight() / 2;

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {

    }

    @Override
    public void windowResized(WindowEvent e) {
        recalcCenter((GLWindow) e.getSource());
    }

    @Override
    public void windowMoved(WindowEvent e) {
        recalcCenter((GLWindow) e.getSource());
    }

    @Override
    public void windowDestroyNotify(WindowEvent e) {

    }

    @Override
    public void windowDestroyed(WindowEvent e) {

    }

    @Override
    public void windowGainedFocus(WindowEvent e) {

    }

    @Override
    public void windowLostFocus(WindowEvent e) {

    }

    @Override
    public void windowRepaint(WindowUpdateEvent e) {

    }
}
