/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 25, 2020 (Start date of project)
 */

// NOTE: there is a memory leak due to the amount of statically-referenced objects, because they can't be destroyed by the garbage collector

// TODO force each animated model to only render its own animation
// TODO allow entities to be selected by the user being within a radius of the given entity (similarly to targeting AggressiveNPCs)
// TODO add inventory panel system
// TODO order the skill icons into the skills panel (They are loaded to the GUIS in Engine/Skill)
// TODO make .obj and .dae files for entities
// TODO add drop table to each enemy
// TODO change the privacy of every class variable and method to being as private as possible
// TODO convert all the author tags to Eliseo
// TODO edit all the comments
// TODO add annotations to describe each function's purpose
// TODO remove any unused methods and imports
// TODO edit import list orders (order: package, local .java imports, local jar imports, java lang imports) + sort each list alphabetically
// TODO remove any println statements

package Engine;

import Animation.AnimatedModel;
import Animation.AnimatedModelLoader;
import Animation.Animation;
import Animation.AnimationLoader;
import Audio.AudioMaster;
import Audio.Source;
import Enemies.GreaterDemon;
import Entities.AnimatedEntity;
import Entities.Camera;
import Entities.AggressiveNPC;
import Entities.Entity;
import Entities.FriendlyNPC;
import Entities.InanimateEntity;
import Entities.Item;
import Entities.Light;
import Entities.NPC;
import Entities.Player;
import Font.KeyboardHandler;
import Font.TextMaster;
import RenderEngine.GUIRenderer;
import GUIs.GUITexture;
import GUIs.MiniMapFrameBuffer;
import Models.RawModel;
import Models.TexturedModel;
import Particles.ParticleMaster;
import Particles.ParticleSystem;
import Particles.ParticleTexture;
import PostProcessing.PostProcessing;
import PostProcessing.ProcessingFBO;
import OBJLoader.ModelData;
import OBJLoader.OBJFileLoader;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.WaterRenderer;
import Shaders.WaterShader;
import Terrains.Terrain;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import Utilities.DisableIllegalAccessWarning;
import Utilities.GlobalConstants;
import Utilities.InternalJarFile;
import Utilities.MousePicker;
import Utilities.RandomNumberGenerator;
import Water.WaterFrameBuffers;
import Water.WaterTile;

import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static final Loader LOADER = new Loader();
    public static final RandomNumberGenerator CUSTOM_RANDOM_NUMBER_GENERATOR = new RandomNumberGenerator(6764521); // original seed: 6764521
    public static final Random RANDOM_NUMBER_GENERATOR = new Random();

    // use these lists to access all game objects
    public static final List<GUITexture> GUIS = new ArrayList<GUITexture>();
    public static final List<Entity> ENTITIES = new ArrayList<Entity>();
    public static final List<AnimatedEntity> ANIMATED_ENTITIES = new ArrayList<AnimatedEntity>();
    public static final List<InanimateEntity> INANIMATE_ENTITIES = new ArrayList<InanimateEntity>();
    public static final List<NPC> NPCS = new ArrayList<NPC>();
    public static final List<FriendlyNPC> FRIENDLY_NPCS = new ArrayList<FriendlyNPC>();
    public static final List<AggressiveNPC> ENEMIES = new ArrayList<AggressiveNPC>();
    public static final List<Item> ITEMS_ON_GROUND = new ArrayList<Item>();

    public static void main(String[] args) {

        DisableIllegalAccessWarning.disableAccessWarnings();

        DisplayManager.createDisplay();

        AudioMaster.init();
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

        // TODO change this to being the music track
        int buffer = AudioMaster.loadSound(new InternalJarFile("res/Sounds/1.wav"));
        Source source = new Source(AudioMaster.MAX_VOLUME, new Vector3f(10, 0, 0), new Vector3f(0, 0, 0));
        source.setLooping(true);
        source.playSound(buffer);

        // ********** TERRAIN TEXTURE STUFF **********
        TerrainTexture backgroundTexture = new TerrainTexture(LOADER.loadTexture("Grass", "Terrain Elements"));
        TerrainTexture redTexture = new TerrainTexture(LOADER.loadTexture("Lava", "Terrain Elements"));
        TerrainTexture greenTexture = new TerrainTexture(LOADER.loadTexture("Stone", "Terrain Elements"));
        TerrainTexture blueTexture = new TerrainTexture(LOADER.loadTexture("Dirt", "Terrain Elements"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, redTexture, greenTexture, blueTexture);
        TerrainTexture blendMap = new TerrainTexture(LOADER.loadTexture("BlendMap2", "Terrain Elements"));

        Terrain terrain = new Terrain(0, -1, texturePack, blendMap, CUSTOM_RANDOM_NUMBER_GENERATOR.nextInt(1000000));
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);

        ModelData data = OBJFileLoader.loadOBJ(new InternalJarFile("res/Objects/Fern.obj"));
        RawModel model = LOADER.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture fernTextureAtlas = new ModelTexture(LOADER.loadTexture("Fern", "Terrain Elements"));
        fernTextureAtlas.setNumberOfColumns(2);
        fernTextureAtlas.setShineDamper(10);
        fernTextureAtlas.setReflectivity(0);

        TexturedModel texturedModel = new TexturedModel(model, fernTextureAtlas);
        for (int i = 0; i < 1000; i++) {
            // TODO only spawn ferns on grass
            float x = CUSTOM_RANDOM_NUMBER_GENERATOR.nextFloat() * terrain.getSize();
            float z = CUSTOM_RANDOM_NUMBER_GENERATOR.nextFloat() * -terrain.getSize();
            float y = terrain.getHeightOfTerrain(x, z);

            InanimateEntity fern = new InanimateEntity("Fern", texturedModel, new Vector3f(x, y, z), new Vector3f(0, 0, 0),
                    new Vector3f(0, CUSTOM_RANDOM_NUMBER_GENERATOR.nextFloat() * 360, 0), 0.25f, CUSTOM_RANDOM_NUMBER_GENERATOR.nextInt(4),
                    Entity.EntityID.ENVIRONMENT);

            INANIMATE_ENTITIES.add(fern);
        }

        List<Light> lights = new ArrayList<Light>();
        Vector3f sunColour = new Vector3f(1.3f, 1.3f, 1.3f);
        Light sun = new Light(GlobalConstants.LIGHT_DIRECTION, sunColour);
        lights.add(sun);

        // this will eventually become the new player
        InternalJarFile animatedModelFile = new InternalJarFile("res/Animated Models/Character Running.dae");
        InternalJarFile animatedModelTexture = new InternalJarFile("res/2D Textures/Model Textures/Character Running Texture.png");
        AnimatedModel animatedModel = AnimatedModelLoader.loadEntity(animatedModelFile, animatedModelTexture);
        Animation animation = AnimationLoader.loadAnimation(animatedModelFile);
        animatedModel.doAnimation(animation);

        // demon test
        InternalJarFile demonModelFile = new InternalJarFile("res/Animated Models/Animated Greater Demon.dae");
        InternalJarFile demonModelTexture = new InternalJarFile("res/2D Textures/Model Textures/Greater Demon Texture.png");
        AnimatedModel animatedModelDemon = AnimatedModelLoader.loadEntity(demonModelFile, demonModelTexture);
        Animation demonAnimation = AnimationLoader.loadAnimation(demonModelFile);
        animatedModelDemon.doAnimation(demonAnimation);

        // skeleton test
        InternalJarFile skeletonModelFile = new InternalJarFile("res/Animated Models/Skeleton.dae");
        InternalJarFile skeletonModelTexture = new InternalJarFile("res/2D Textures/Model Textures/Skeleton Texture.png");
        AnimatedModel animatedSkeleton = AnimatedModelLoader.loadEntity(skeletonModelFile, skeletonModelTexture);
        Animation skeletonAnimation = AnimationLoader.loadAnimation(skeletonModelFile);
        animatedSkeleton.doAnimation(skeletonAnimation);

        GreaterDemon greaterDemon = new GreaterDemon("Greater Demon", animatedModelDemon, new Vector3f(25, terrain.getHeightOfTerrain(25, -25), -25),
                new Vector3f(0, 0, 0), GlobalConstants.ROTATE_SOUTH_GREATER_DEMON, GreaterDemon.REGULAR_SCALE3D, 0, NPC.MovementType.RANDOM,
                true, AggressiveNPC.AttackType.MELEE, 1);

        GreaterDemon skeleton = new GreaterDemon("Skeleton", animatedSkeleton, new Vector3f(15, terrain.getHeightOfTerrain(15, -15), -15),
                new Vector3f(0, 0, 0), GlobalConstants.ROTATE_SOUTH_COWBOY, 3f, 0, NPC.MovementType.RANDOM,
                true, AggressiveNPC.AttackType.MELEE, 1);

        ModelData abyssalDemonData = OBJFileLoader.loadOBJ(new InternalJarFile("res/Objects/Abyssal Demon.obj"));
        RawModel abyssalDemonModel = LOADER.loadToVAO(abyssalDemonData.getVertices(), abyssalDemonData.getTextureCoords(), abyssalDemonData.getNormals(), abyssalDemonData.getIndices());
        ModelTexture abyssalDemonTexture = new ModelTexture(LOADER.loadTexture("Abyssal Demon Texture", "Model Textures"));
        abyssalDemonTexture.setShineDamper(1);
        abyssalDemonTexture.setReflectivity(0);
        // abyssalDemonTexture.setIsTransparent(true);
        TexturedModel texturedAbyssalDemon = new TexturedModel(abyssalDemonModel, abyssalDemonTexture);

        Player player = new Player("Eliseo", animatedModel, new Vector3f(5, terrain.getHeightOfTerrain(5, -5), -5),
                new Vector3f(0, 0, 0), new Vector3f(0, 180, 0), 0.0001f, 0);

        Camera camera = new Camera(player);

        MasterRenderer masterRenderer = new MasterRenderer(camera);

        TextMaster.init(player);

        MiniMapFrameBuffer miniMapFBO = new MiniMapFrameBuffer();
        GUITexture miniMap = new GUITexture(miniMapFBO.getTextureID(), new Vector2f(0.71f, 0.6f), new Vector2f(0.25f, 0.38f), 180f,
                true);
        GUITexture miniMapGUI = new GUITexture(LOADER.loadTexture("minimap", "GUI Elements"), new Vector2f(0.7135f, 0.598f),
                new Vector2f(0.287f, 0.532f), 0f, false);
        GUITexture inventory = new GUITexture(LOADER.loadTexture("inventory", "GUI Elements"), new Vector2f(0.715f, -0.4f),
                new Vector2f(0.447f, 0.6f), 0f, false);
        GUITexture chatlog = new GUITexture(LOADER.loadTexture("chatlog", "GUI Elements"), new Vector2f(-0.285f, -0.765f),
                new Vector2f(1.408f,1.7f), 0f, false);
        GUITexture compass = new GUITexture(LOADER.loadTexture("compass", "GUI Elements"), new Vector2f(0.502f, 0.91f),
                new Vector2f(0.088f, 0.158f), 0f, false);
        GUIRenderer guiRenderer = new GUIRenderer();

        WaterFrameBuffers waterFBOs = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(waterShader, masterRenderer.getProjectionMatrix(), waterFBOs);
        List<WaterTile> waterTiles = new ArrayList<WaterTile>();
        WaterTile river = new WaterTile(75, -75, -1f);
        WaterTile waterA = new WaterTile(290, -370, 0f);
        waterTiles.add(river);
        waterTiles.add(waterA);

        Vector4f reflectionClipPlane = new Vector4f(0, 1, 0, -waterA.getHeight() + 1);
        Vector4f refractionClipPlane = new Vector4f(0, -1, 0, waterA.getHeight());
        Vector4f gameViewClipPlane = new Vector4f(0, -1, 0, 10000);

        ParticleMaster.init(masterRenderer.getProjectionMatrix());
        ParticleTexture particleTexture = new ParticleTexture(LOADER.loadTexture("Alchemy Atlas", "Particle Effects"), 4,
                false);
        ParticleSystem particleSystem = new ParticleSystem(particleTexture, 60, 10, 1f, 0.4f, 1.1f, 1.1f);
        particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
        // the higher the error values, the more random the particle becomes
        particleSystem.setLifeError(0.1f);
        particleSystem.setSpeedError(0.25f);
        particleSystem.setScaleError(0.5f, 0.5f);
        particleSystem.randomizeRotation();

        ParticleTexture prayerTexture = new ParticleTexture(LOADER.loadTexture("Protect From Melee", "Particle Effects"), 1,
                false);
        ParticleSystem prayerParticleSystem = new ParticleSystem(prayerTexture, 600, 1, 0, 0.01f, 1.1f, 1.1f);

        /*
        ParticleTexture greenHealthBarTexture = new ParticleTexture(LOADER.loadTexture("Green Health Bar", "Particle Effects"), 1, false);
        ParticleSystem greenHealthBarParticleSystem = new ParticleSystem(greenHealthBarTexture, 6000, 1, 0, 0.01f, 1f, 1f);

        ParticleTexture redHealthBarTexture = new ParticleTexture(LOADER.loadTexture("Red Health Bar", "Particle Effects"), 1, false);
        ParticleSystem redHealthBarParticleSystem = new ParticleSystem(redHealthBarTexture, 6000, 1, 0, 0.01f, 2f, 1f);
        */

        ProcessingFBO postProcessingFBO = new ProcessingFBO(Display.getWidth(), Display.getHeight());
        ProcessingFBO outputFBO = new ProcessingFBO(Display.getWidth(), Display.getHeight(), ProcessingFBO.DEPTH_TEXTURE);
        PostProcessing.init();

        MousePicker mouse = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrain);
        boolean leftClickHeld = false;
        Vector3f gameViewMouseRay = new Vector3f(player.getPosition());
        Vector2f HUDMouseRay;

        /*
        FileManager.createDirectory("", "temp");
        FileManager.createFile("temp", "temp", ".txt");
        FileManager.writeObjectToFile("temp", "temp", ".txt", sunColour);
        System.out.println(FileManager.read3DVector(FileManager.readFromFile("temp", "temp", ".txt")));
        System.out.println(FileManager.read3DVector(FileManager.readFromFile("temp", "temp", ".txt")).x);
        FileManager.deleteFile("temp", "temp", ".txt");
        FileManager.deleteDirectory("", "temp");
        */

        while (!Display.isCloseRequested()) {
            masterRenderer.renderShadowMap(sun);

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            AudioMaster.setListenerData(player.getPosition(), player.getVelocity());

            // render reflection to water FBO
            waterFBOs.bindReflectionFrameBuffer();
            float reflectionDistance = 2 * (camera.getPosition().y - waterA.getHeight());
            camera.getPosition().y -= reflectionDistance;
            camera.invertPitch();
            masterRenderer.renderScene(terrain, lights, camera, reflectionClipPlane);
            camera.getPosition().y += reflectionDistance;
            camera.invertPitch();

            // render refraction to water FBO
            waterFBOs.bindRefractionFrameBuffer();
            masterRenderer.renderScene(terrain, lights, camera, refractionClipPlane);
            waterFBOs.unbindCurrentFrameBuffer();

            // render minimap to FBO
            miniMapFBO.bindFrameBuffer();
            float actualPitch = camera.getPitch();
            float actualDistanceFromPlayer = camera.getDistanceFromPlayer();
            camera.setPitch(Camera.MINIMAP_PITCH);
            camera.setDistanceFromPlayer(Camera.MINIMAP_DISTANCE);
            camera.move(false);
            player.setScale(Player.MINIMAP_SCALE);
            masterRenderer.renderScene(terrain, lights, camera, gameViewClipPlane);
            waterRenderer.render(waterTiles, camera, sun);
            miniMapFBO.unbindCurrentFrameBuffer();
            camera.setPitch(actualPitch);
            camera.setDistanceFromPlayer(actualDistanceFromPlayer);
            player.setScale(Player.REGULAR_SCALE);

            // render scene to Gameview
            DisplayManager.changeToGameView();

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

            camera.move(true);
            mouse.update();
            Item.updateItems(player);

            particleSystem.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y + 6f, player.getPosition().z - 2f));
            prayerParticleSystem.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y + 7.2f, player.getPosition().z));

            ParticleMaster.update(camera);

            // TODO clean this up and make into a function
            // ensures each left click is consumed only once
            if (Mouse.isButtonDown(0) && !leftClickHeld) {
                if (mouse.elementHoveredOver == MousePicker.GameElement.GAMEVIEW && mouse.getCurrentTerrainPoint() != null) {
                    gameViewMouseRay = mouse.getCurrentTerrainPoint();
                    player.faceTarget(gameViewMouseRay); // handle character rotation
                } else if (mouse.elementHoveredOver == MousePicker.GameElement.INVENTORY) {
                    HUDMouseRay = mouse.getMouseScreenPoint();
                    mouse.selectInventoryPanel(HUDMouseRay);

                    // TODO change inventory panel view based on mouse.currentPanel's value
                    InventoryPanelSystem.displayPanel(mouse.currentPanel);
                    player.getInventory().interactWithInventorySlot(HUDMouseRay);
                }
            }
            player.moveTo(gameViewMouseRay, terrain);
            leftClickHeld = Mouse.isButtonDown(0);

            // System.out.println(player.getPosition());

            updateGameObjects(player, terrain);

            source.setPosition(player.getPosition());

            KeyboardHandler.getInput(player);

            // true -> only one stage i.e. Contrast ; false -> multi-stage post-processing
            postProcessingFBO.bindFrameBuffer(true);

            masterRenderer.renderScene(terrain, lights, camera, gameViewClipPlane);

            waterRenderer.render(waterTiles, camera, sun);

            for (int i = 0; i < ANIMATED_ENTITIES.size(); i++) {
                AnimatedEntity entity = ANIMATED_ENTITIES.get(i);
                Vector3f position = entity.getPosition();
                entity.getHealthBarParticleSystem().generateParticles(new Vector3f(position.x, position.y + entity.getHealthBarOffset(), position.z));
            }

            ParticleMaster.renderParticles(camera);

            postProcessingFBO.unbindFrameBuffer();
            postProcessingFBO.resolveToFBO(outputFBO);

            PostProcessing.doPostProcessing(outputFBO.getColourTexture());

            // render HUD
            DisplayManager.changeToHUDView();

            compass.calculateRotation();
            guiRenderer.render(GUIS);

            TextMaster.render();

            DisplayManager.updateDisplay();
        }

        source.free();
        AudioMaster.free();
        PostProcessing.free();
        outputFBO.free();
        postProcessingFBO.free();
        ParticleMaster.free();
        TextMaster.free();
        miniMapFBO.free();
        waterFBOs.free();
        waterShader.free();
        guiRenderer.free();
        masterRenderer.free();
        LOADER.free();
        freeList(GUIS);
        freeList(ENTITIES);
        freeList(ANIMATED_ENTITIES);
        freeList(INANIMATE_ENTITIES);
        freeList(NPCS);
        freeList(FRIENDLY_NPCS);
        freeList(ENEMIES);
        freeList(ITEMS_ON_GROUND);
        DisplayManager.closeDisplay();
    }

    // this method gets called every frame
    private static void updateGameObjects(Player player, Terrain terrain) {
        player.update();

        for (int i = 0; i < NPCS.size(); i++) {
            NPCS.get(i).update();
        }

        for (int i = 0; i < ENEMIES.size(); i++) {
            ENEMIES.get(i).update(player, terrain);
        }

        // TODO determine why calling update on only one model causes the all animations of all entities to update
        // for (int i = 0; i < ANIMATED_ENTITIES.size(); i++) {
            // ANIMATED_ENTITIES.get(i).getModel().update();
            ANIMATED_ENTITIES.get(1).getModel().update();
        // }
    }

    private static void freeList(List list) {
        for (int i = 0; i < list.size(); i++) {
            list.remove(i);
        }
    }
}
