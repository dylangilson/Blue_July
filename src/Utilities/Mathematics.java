/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package Utilities;

import Entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Mathematics {

    public static float barycentricInterpolation(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f position) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);

        float l1 = ((p2.z - p3.z) * (position.x - p3.x) + (p3.x - p2.x) * (position.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (position.x - p3.x) + (p1.x - p3.x) * (position.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;

        return l1 * p1.y + l2 * p2.y + l3 * p3.y; // estimated height of triangle at given position
    }

    // 3D transformation matrix
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix.setIdentity();

        Matrix4f.translate(translation, transformationMatrix, transformationMatrix);

        Matrix4f.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0), transformationMatrix, transformationMatrix);
        Matrix4f.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0), transformationMatrix, transformationMatrix);
        Matrix4f.rotate((float)Math.toRadians(rotation.z), new Vector3f(0, 0, 1), transformationMatrix, transformationMatrix);

        Matrix4f.scale(new Vector3f(scale, scale, scale), transformationMatrix, transformationMatrix);

        return transformationMatrix;
    }

    // 2D transformation matrix
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale, float rotation) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);

        Vector3f cameraPosition = camera.getPosition();
        Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        Matrix4f.translate(negativeCameraPosition, viewMatrix, viewMatrix);

        return viewMatrix;
    }
}
