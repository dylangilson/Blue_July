/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 10, 2021
 */

package Utilities;

import Entities.Camera;
import Entities.Entity;
import RenderEngine.DisplayManager;
import Terrains.Terrain;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {

    private static final int RECURSION_COUNT = 200;
    private static final int RAY_RANGE = 600;

    private static final int GAMEVIEW_WIDTH = 912; // x in [0, 912] -> in gameview
    private static final int GAMEVIEW_HEIGHT = 169; // y in [169, Display.getHeight()] -> in gameview
    private static final int INVENTORY_HEIGHT = 432; // y in [0, 432] -> in inventory
    public static final int TOP_BAR_HEIGHT = 380;
    public static final int BOTTOM_BAR_HEIGHT = 45;
    private static final int COMBAT_WIDTH = 965;
    private static final int LEVELS_WIDTH = 1012;
    private static final int QUESTS_WIDTH = 1060;
    private static final int INVENTORY_WIDTH = 1135;
    private static final int EQUIPMENT_WIDTH = 1185;
    private static final int PRAYER_WIDTH = 1234;
    private static final int MAGIC_WIDTH = 1280;
    private static final int BLANK_WIDTH = 963;
    private static final int FRIENDS_WIDTH = 1012;
    private static final int IGNORES_WIDTH = 1060;
    private static final int LOGOUT_WIDTH = 1135;
    private static final int SETTINGS_WIDTH = 1185;
    private static final int EMOTES_WIDTH = 1234;
    private static final int MUSIC_WIDTH = 1280;

    private Vector3f currentRay = new Vector3f();
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;
    private Terrain terrain;
    private Vector3f currentTerrainPoint;

    // gui/gameview that the mouse is hovering over
    public enum GameElement {
        GAMEVIEW, CHATLOG, INVENTORY, MINIMAP
    }

    public GameElement elementHoveredOver;

    // gui/gameview that the mouse is hovering over
    public enum InventoryPanel {
        COMBAT, LEVELS, QUESTS, INVENTORY, EQUIPMENT, PRAYER, MAGIC, FRIENDS, IGNORES, LOGOUT, SETTINGS, EMOTES, MUSIC
    }

    public InventoryPanel currentPanel;

    public MousePicker(Camera camera, Matrix4f projectionMatrix, Terrain terrain) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = Mathematics.createViewMatrix(camera);
        this.terrain = terrain;
        this.currentPanel = InventoryPanel.INVENTORY;
    }

    public void update() {
        viewMatrix = Mathematics.createViewMatrix(camera);
        currentRay = calculateMouseRay();

        calculateElementHoveredOver();

        currentTerrainPoint = terrainIntersectionInRange(0, RAY_RANGE, currentRay) ? terrainBinarySearch(0, 0, RAY_RANGE, currentRay) : null;
    }

    public Vector2f getMouseScreenPoint() {
        return new Vector2f(Mouse.getX(), Mouse.getY());
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public void calculateElementHoveredOver() {
        if (Mouse.getX() < GAMEVIEW_WIDTH && Mouse.getY() > GAMEVIEW_HEIGHT) {
            elementHoveredOver = GameElement.GAMEVIEW;
        } else if (Mouse.getX() < GAMEVIEW_WIDTH && Mouse.getY() < GAMEVIEW_HEIGHT) {
            elementHoveredOver = GameElement.CHATLOG;
        } else if (Mouse.getX() > GAMEVIEW_WIDTH && Mouse.getY() < INVENTORY_HEIGHT) {
            elementHoveredOver = GameElement.INVENTORY;
        } else if (Mouse.getX() > GAMEVIEW_WIDTH && Mouse.getY() > INVENTORY_HEIGHT) {
            elementHoveredOver = GameElement.MINIMAP;
        }
    }

    public void selectInventoryPanel(Vector2f mouseRay) {
        if (mouseRay.getX() < COMBAT_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.COMBAT;
        } else if (mouseRay.getX() < LEVELS_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.LEVELS;
        } else if (mouseRay.getX() < QUESTS_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.QUESTS;
        } else if (mouseRay.getX() < INVENTORY_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.INVENTORY;
        } else if (mouseRay.getX() < EQUIPMENT_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.EQUIPMENT;
        } else if (mouseRay.getX() < PRAYER_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.PRAYER;
        } else if (mouseRay.getX() < MAGIC_WIDTH && mouseRay.getY() > TOP_BAR_HEIGHT) {
            currentPanel = InventoryPanel.MAGIC;
        } else if (mouseRay.getX() < BLANK_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            // blank tile
        } else if (mouseRay.getX() < FRIENDS_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            currentPanel = InventoryPanel.FRIENDS;
        } else if (mouseRay.getX() < IGNORES_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            currentPanel = InventoryPanel.IGNORES;
        } else if (mouseRay.getX() < LOGOUT_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            currentPanel = InventoryPanel.LOGOUT;
        } else if (mouseRay.getX() < SETTINGS_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            currentPanel = InventoryPanel.SETTINGS;
        } else if (mouseRay.getX() < EMOTES_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            currentPanel = InventoryPanel.EMOTES;
        } else if (mouseRay.getX() < MUSIC_WIDTH && mouseRay.getY() < BOTTOM_BAR_HEIGHT) {
            currentPanel = InventoryPanel.MUSIC;
        }
    }

    // convert from 2D screen position to 3D ray
    private Vector3f calculateMouseRay() {
        // location of mouse in 2D (viewport) space
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();

        // location of mouse in normalized device space
        Vector2f normalizedDeviceCoords = getNormalizedDeviceCoords(mouseX, mouseY);

        // location of mouse in homogeneous clip space (z = -1)
        Vector4f clipCoords = new Vector4f(normalizedDeviceCoords.x, normalizedDeviceCoords.y, -1f, 1f);

        // location of mouse in eye space (inverse projection matrix)
        Vector4f eyeCoords = toEyeCoords(clipCoords);

        // location of mouse in world space (inverse view matrix)
        Vector3f worldRay = toWorldCoords(eyeCoords);

        return worldRay;
    }

    // may need a second, nearly identical function for HUD because it doesn't need the DisplayManager calls added
    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
        float x = (2f * mouseX) / (Display.getWidth() - DisplayManager.getGameviewWidth()) - 1f;
        float y = (2f * (mouseY - DisplayManager.getGameviewHeight())) / (Display.getHeight() - DisplayManager.getGameviewHeight()) - 1f;

        return new Vector2f(x, y);
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjectedMatrix = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjectedMatrix, clipCoords, null);

        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedViewMatrix = Matrix4f.invert(viewMatrix, null);
        Vector4f rayCoordsInWorldSpace = Matrix4f.transform(invertedViewMatrix, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayCoordsInWorldSpace.x, rayCoordsInWorldSpace.y, rayCoordsInWorldSpace.z);
        mouseRay.normalise();

        return mouseRay;
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f cameraPosition = camera.getPosition();
        Vector3f start = new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);

        return Vector3f.add(start, scaledRay, null);
    }

    private Vector3f terrainBinarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);

        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());

            return terrain != null ? endPoint : null;
        }

        return terrainIntersectionInRange(start, half, ray) ? terrainBinarySearch(count + 1, start, half, ray)
                : terrainBinarySearch(count + 1, half, finish, ray);
    }

    private boolean terrainIntersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);

        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
        float height = 0;

        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
        }

        return testPoint.y < height;
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }
}
