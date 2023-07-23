/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package Entities;

import RenderEngine.MasterRenderer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    public static final float MINIMAP_DISTANCE = 150;
    public static final float MINIMAP_PITCH = 90;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix = new Matrix4f();

    public static float distanceFromPlayer = 25;
    private float angleAroundPlayer = 180;
    private Vector3f position = new Vector3f(0, 0, 0);
    public float pitch = 20; // rotation around x, y, z axes
    private float yaw = 0; // camera aim in sideways orientation
    private float roll; // angle of tilt of camera
    private Player player;

    public Camera(Player player) {
        this.player = player;
        this.projectionMatrix = createProjectionMatrix();
    }

    public void move(boolean isGameView) {
        if (isGameView) {
            calculateZoom();
            calculatePitch();
        }

        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        // this.yaw = 180 - (player.getRotation().y + angleAroundPlayer);
        this.yaw = 180 - angleAroundPlayer; // using the above line will result in the camera rotating when the player does

        updateViewMatrix();
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public void setDistanceFromPlayer(float distanceFromPlayer) {
        this.distanceFromPlayer = distanceFromPlayer;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        // float theta = player.getRotation().y + angleAroundPlayer;
        float theta = angleAroundPlayer; // using the above line will result in the camera rotating when the player does

        float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.y = player.getPosition().y + verticalDistance + 1;
        position.z = player.getPosition().z - offsetZ;
    }

    private float calculateHorizontalDistance() {
        return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.05f;
        distanceFromPlayer -= zoomLevel;

        if (distanceFromPlayer > 50) {
            distanceFromPlayer = 50; // ensure player can't zoom out too far
        }

        if (distanceFromPlayer < 1) {
            distanceFromPlayer = 1; // ensure player can't zoom in too close
        }
    }

    private void calculatePitch() {
        float pitchChange = 0.5f;

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            pitch += pitchChange;

            if (pitch > 60) {
                pitch = 60;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            pitch -= pitchChange;

            if (pitch < 5) {
                pitch = 5;
            }
        }

        // Mouse scroll wheel clicked
        if (Mouse.isButtonDown(2)) {
            int sign = Integer.signum(Mouse.getDY());
            pitch += pitchChange * -sign * 3;

            if (pitch < 5) {
                pitch = 5;
            }

            if (pitch > 60) {
                pitch = 60;
            }
        }
    }

    private void calculateAngleAroundPlayer() {
        float angleChange = 1f;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            angleAroundPlayer -= angleChange;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            angleAroundPlayer += angleChange;
        }

        // Mouse scroll wheel clicked
        if (Mouse.isButtonDown(2)) {
            int sign = Integer.signum(Mouse.getDX());
            angleAroundPlayer += angleChange * -sign * 3;
        }
    }

    private static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
        float y_scale = (float)((1f / Math.tan(Math.toRadians(MasterRenderer.FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = MasterRenderer.FAR_PLANE - MasterRenderer.NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((MasterRenderer.FAR_PLANE + MasterRenderer.NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * MasterRenderer.NEAR_PLANE * MasterRenderer.FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;

        return projectionMatrix;
    }

    private void updateViewMatrix() {
        viewMatrix.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float)Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);

        Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);

        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
