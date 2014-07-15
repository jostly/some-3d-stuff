package net.badgerclaw.cityengine;

import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
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

public class SimpleScene implements GLEventListener, KeyListener, MouseListener, WindowListener, RenderContext {

    private ShaderProgram program = new ShaderProgram();

    private final long startingTime = System.currentTimeMillis();
    private float lastTime = 0;

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

        gl3.glEnable(GL3.GL_CULL_FACE);
        gl3.glCullFace(GL3.GL_BACK);
        gl3.glFrontFace(GL3.GL_CW);

        gl3.glEnable(GL3.GL_DEPTH_TEST);
        gl3.glDepthMask(true);
        gl3.glDepthFunc(GL3.GL_LEQUAL);
        gl3.glDepthRangef(0.0f, 1.0f);

        root.init(gl3);
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
        float delta = lastTime == 0 ? 0 : (time - lastTime);
        lastTime = time;

        float speed = delta * 5f;

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

        float rotationSpeed = delta * 0.5f;

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

        blockContainer.transformationMatrix.setIdentity()
                .mul(Mat4.rotation(new Vec3(1f, 0.0f, 1f).normalize(), time * 0.7f));

        rod.transformationMatrix.setIdentity()
                .mul(Mat4.translation(0f, 0f, 1.2f))
                .mul(Mat4.scaling(0.25f, 0.25f, 1f))
                .mul(Mat4.rotation(new Vec3(0f, 0f, 1f), time * 4.2f));

        program.useProgram(gl3, true);
        {
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
