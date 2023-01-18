/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 24, 2021
 */

package Utilities;

import Engine.Main;
import Models.RawModel;
import Models.TexturedModel;
import OBJLoader.ModelData;
import OBJLoader.OBJFileLoader;
import Textures.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

// global constants used by multiple classes
public class GlobalConstants {

    public static final float MAP_SIZE = 800f; // x and z length of entire map

    // skeleton constants
    public static final int MAX_JOINTS = 50; // max number of joints in a skeleton
    public static final int MAX_WEIGHTS = 3; // maximum number of joints that can be affected by a single root joint

    // physics constants
    public static final float PLAYER_WALK_SPEED = 40f; // units per second
    public static final float NPC_WALK_SPEED = 20f; // units per second
    public static final float TURN_SPEED = 160f; // degrees per second
    public static final float GRAVITY = -50f;
    public static final float JUMP_HEIGHT = 25f;

    // delay length constants (in milliseconds)
    public static final int GAME_TICK = 4; // number of ticks that must pass before an action is consumed
    public static final int SHORT_DELAY_LENGTH = 200 * GAME_TICK;
    public static final int NORMAL_DELAY_LENGTH = 400 * GAME_TICK;
    public static final int LONG_DELAY_LENGTH = 600 * GAME_TICK;
    public static final int CONVERT_MILLISECONDS_TO_SECONDS = 1000;

    // attack distance constants
    public static final float MELEE_ATTACK_RANGE = 3f;
    public static final float RANGED_ATTACK_RANGE = 50f;

    // colour constants
    public static final Vector3f NORMAL_COLOUR = new Vector3f(0.5f, 0.5f, 0.5f);
    public static final Vector3f BLACK = new Vector3f(0f, 0f, 0f);
    public static final Vector3f GREEN = new Vector3f(0f, 1f, 0f);
    public static final Vector3f DARK_GREEN = new Vector3f(0f, 0.5f, 0f);
    public static final Vector3f FOREST_GREEN = new Vector3f(0.13f, 0.543f, 0.13f);
    public static final Vector3f DARK_BLUE = new Vector3f(0f, 0f, 0.5f);
    public static final Vector3f BLUE = new Vector3f(0f, 0f, 1f);
    public static final Vector3f SKY_BLUE = new Vector3f(0.53f, 0.8f, 0.92f);
    public static final Vector3f DEEP_BLUE = new Vector3f(0.1f, 0.4f, 0.8f);
    public static final Vector3f ULTRAMARINE = new Vector3f(0.015f, 0.02f, 0.945f);
    public static final Vector3f MIDNIGHT_BLUE = new Vector3f(0.098f, 0.098f, 0.4375f);
    public static final Vector3f PURPLE = new Vector3f(0.5f, 0f, 0.5f);
    public static final Vector3f GOLD = new Vector3f(1f, 0.88f, 0f);
    public static final Vector3f GOLDENROD = new Vector3f(0.852f, 0.645f, 0.125f);
    public static final Vector3f DARK_GOLDENROD = new Vector3f(0.5859f, 0.4258f, 0.035f);
    public static final Vector3f RED = new Vector3f(1f, 0f, 0f);
    public static final Vector3f DIM_GREY = new Vector3f(0.41f, 0.41f, 0.41f);

    // environment constants
    public static final Vector3f LIGHT_DIRECTION = new Vector3f(3000000, 2000000, 200000);
    public static final int MAX_LIGHTS = 4; // maximum number of lights that can affect any single entity

    // animated entity's rotations
    // greater demon
    public static final Vector3f ROTATE_NORTH_GREATER_DEMON = new Vector3f(270, 180, 0);;
    public static final Vector3f ROTATE_EAST_GREATER_DEMON = new Vector3f(90, 0, 270);;
    public static final Vector3f ROTATE_SOUTH_GREATER_DEMON = new Vector3f(90, 0, 0);
    public static final Vector3f ROTATE_WEST_GREATER_DEMON = new Vector3f(90, 0, 90);
    // cowboy
    public static final Vector3f ROTATE_NORTH_COWBOY = new Vector3f(0, 180, 0);;
    public static final Vector3f ROTATE_EAST_COWBOY = new Vector3f(0, 90, 0);;
    public static final Vector3f ROTATE_SOUTH_COWBOY = new Vector3f(0, 0, 0);
    public static final Vector3f ROTATE_WEST_COWBOY = new Vector3f(0, -90, 0);
}
