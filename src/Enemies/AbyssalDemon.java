/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 31, 2021
 */

package Enemies;

import Animation.AnimatedModel;
import Engine.Main;
import Entities.AggressiveNPC;
import Models.RawModel;
import Models.TexturedModel;
import OBJLoader.ModelData;
import OBJLoader.OBJFileLoader;
import Textures.ModelTexture;
import Utilities.GlobalConstants;

import Utilities.InternalJarFile;
import org.lwjgl.util.vector.Vector3f;

public class AbyssalDemon extends AggressiveNPC {

    private static final ModelData abyssalDemonData = OBJFileLoader.loadOBJ(new InternalJarFile("res/Objects/Abyssal Demon.obj"));
    private static final RawModel abyssalDemonModel = Main.LOADER.loadToVAO(abyssalDemonData.getVertices(), abyssalDemonData.getTextureCoords(),
            abyssalDemonData.getNormals(), abyssalDemonData.getIndices());
    private static final ModelTexture abyssalDemonTexture = new ModelTexture(Main.LOADER.loadTexture("Abyssal Demon Texture", "Model Textures"));
    public static final TexturedModel TEXTURED_ABYSSAL_DEMON = new TexturedModel(abyssalDemonModel, abyssalDemonTexture);

    public static final float REGULAR_SCALE3D = 0.01f;
    public static final float HEALTH_BAR_OFFSET = 4.5f;

    public AbyssalDemon(String name, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale,
                        int textureIndex, MovementType movementType, boolean isAggressive, AttackType attackType, int dropTableSize) {
        super(name, model, position, velocity, rotation, scale, textureIndex, movementType, isAggressive, attackType, GlobalConstants.NORMAL_DELAY_LENGTH, dropTableSize);
        super.setHealthBarOffset(HEALTH_BAR_OFFSET);

        // TODO update this to the correct rotation for abyssal demon when .dae file is complete
        this.rotateNorth = GlobalConstants.ROTATE_NORTH_COWBOY;
        this.rotateEast = GlobalConstants.ROTATE_EAST_COWBOY;
        this.rotateSouth = GlobalConstants.ROTATE_SOUTH_COWBOY;
        this.rotateWest = GlobalConstants.ROTATE_WEST_COWBOY;

        initializeDropTable();

        this.getStats().getAttack().addXP(1000);
    }

    @Override
    public void initializeDropTable() {

    }
}
