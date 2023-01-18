/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 7, 2021
 */

package Terrains;

import Engine.Main;
import Models.RawModel;
import RenderEngine.Loader;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import Utilities.GlobalConstants;
import Utilities.Mathematics;
import Utilities.ResourceStreamReader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Terrain {

    private static final int VERTEX_COUNT = 128; // number of vertices on each edge of terrain (Flat terrain only)
    private static final float MAX_HEIGHT = 15; // height of terrain is in range [-MAX_HEIGHT , MAX_HEIGHT] (heightmaps only)
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256; // 3 colour channels RGB with 256 possible values each
    private static int SEED;

    private float xCoord;
    private float zCoord;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] pixelHeights;

    public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture blendMap, int seed) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.xCoord = gridX * GlobalConstants.MAP_SIZE;
        this.zCoord = gridZ * GlobalConstants.MAP_SIZE;
        this.SEED = seed;
        this.model = generateTerrain(); // generate height with a generator
    }

    // for use with heightMaps
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.xCoord = gridX * GlobalConstants.MAP_SIZE;
        this.zCoord = gridZ * GlobalConstants.MAP_SIZE;
        // this.model = generateFlatTerrain(); // flat terrain
        this.model = generateTerrain(loader, heightMap); // generate height with heightmap
        // this.model = generateTerrain(); // generate height with a generator
    }

    public float getxCoord() {
        return xCoord;
    }

    public float getzCoord() {
        return zCoord;
    }

    public RawModel getModel() {
        return model;
    }

    private RawModel generateFlatTerrain() {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT)];

        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = -(float)j / ((float)VERTEX_COUNT - 1) * GlobalConstants.MAP_SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = -(float)i / ((float)VERTEX_COUNT - 1) * GlobalConstants.MAP_SIZE;

                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;

                textureCoords[vertexPointer * 2] = (float)j / ((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float)i / ((float)VERTEX_COUNT - 1);

                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int gridZ = 0; gridZ < VERTEX_COUNT - 1; gridZ++) {
            for (int gridX = 0; gridX < VERTEX_COUNT - 1; gridX++) {
                int topLeft = (gridZ * VERTEX_COUNT) + gridX;
                int topRight = topLeft + 1;
                int bottomLeft = ((gridZ + 1) * VERTEX_COUNT) + gridX;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return Main.LOADER.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private RawModel generateTerrain() {
        HeightGenerator generator = new HeightGenerator(SEED);

        int VERTEX_COUNT = 128;
        pixelHeights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT)];

        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float)j / ((float)VERTEX_COUNT - 1) * GlobalConstants.MAP_SIZE; // x-value
                float height = getHeightOfTerrain(j, i, generator);
                pixelHeights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height; // y-value
                vertices[vertexPointer * 3 + 2] = (float)i / ((float)VERTEX_COUNT - 1) * GlobalConstants.MAP_SIZE; // z-value

                Vector3f normal = calculateNormal(j, i, generator);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                textureCoords[vertexPointer * 2] = (float)j / ((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float)i / ((float)VERTEX_COUNT - 1);

                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int gridZ = 0; gridZ < VERTEX_COUNT - 1; gridZ++) {
            for (int gridX = 0; gridX < VERTEX_COUNT - 1; gridX++) {
                int topLeft = (gridZ * VERTEX_COUNT) + gridX;
                int topRight = topLeft + 1;
                int bottomLeft = ((gridZ + 1) * VERTEX_COUNT) + gridX;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return Main.LOADER.loadToVAO(vertices, textureCoords, normals, indices);
    }

    // for use with heightmaps
    private RawModel generateTerrain(Loader loader, String heightMap) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ResourceStreamReader.getResourceStream("res/2D Textures/Terrain Elements/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = image.getHeight();
        pixelHeights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT)];

        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float)j / ((float)VERTEX_COUNT - 1) * GlobalConstants.MAP_SIZE; // x-value
                float height = getPixelHeight(j, i, image);
                pixelHeights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height; // y-value
                vertices[vertexPointer * 3 + 2] = (float)i / ((float)VERTEX_COUNT - 1) * GlobalConstants.MAP_SIZE; // z-value

                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                textureCoords[vertexPointer * 2] = (float)j / ((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float)i / ((float)VERTEX_COUNT - 1);

                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int gridZ = 0; gridZ < VERTEX_COUNT - 1; gridZ++) {
            for (int gridX = 0; gridX < VERTEX_COUNT - 1; gridX++) {
                int topLeft = (gridZ * VERTEX_COUNT) + gridX;
                int topRight = topLeft + 1;
                int bottomLeft = ((gridZ + 1) * VERTEX_COUNT) + gridX;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    // for use with heightmaps
    private float getPixelHeight(int xCoord, int yCoord, BufferedImage image) {
        if (xCoord < 0 || xCoord >= image.getHeight() || yCoord < 0 || yCoord >= image.getHeight()) {
            return 0;
        }

        float height = image.getRGB(xCoord, yCoord); // range of [-MAX_PIXEL_COLOUR , 0]
        height += MAX_PIXEL_COLOUR / 2f; // range of [-MAX_PIXEL_COLOUR / 2 , MAX_PIXEL_COLOUR / 2]
        height /= MAX_PIXEL_COLOUR / 2f; // range of [-1 , 1]
        height *= MAX_HEIGHT; // range of [-MAX_HEIGHT , MAX_HEIGHT]

        return height;
    }

    private Vector3f calculateNormal(int xCoord, int yCoord, HeightGenerator generator) {
        float heightL = getHeightOfTerrain(xCoord - 1, yCoord, generator);
        float heightR = getHeightOfTerrain(xCoord + 1, yCoord, generator);
        float heightD = getHeightOfTerrain(xCoord, yCoord - 1, generator);
        float heightU = getHeightOfTerrain(xCoord, yCoord + 1, generator);

        Vector3f normal = new Vector3f(heightL - heightR, 2, heightD - heightU);
        normal.normalise();

        return normal;
    }

    // for use with heightmaps
    private Vector3f calculateNormal(int xCoord, int yCoord, BufferedImage image) {
        float heightL = getPixelHeight(xCoord - 1, yCoord, image);
        float heightR = getPixelHeight(xCoord + 1, yCoord, image);
        float heightD = getPixelHeight(xCoord, yCoord - 1, image);
        float heightU = getPixelHeight(xCoord, yCoord + 1, image);

        Vector3f normal = new Vector3f(heightL - heightR, 2, heightD - heightU);
        normal.normalise();

        return normal;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getSize() {
        return GlobalConstants.MAP_SIZE;
    }

    public float getHeightOfTerrain(int worldX, int worldZ, HeightGenerator generator) {
        return generator.generateHeight(worldX, worldZ);
    }

    // for use with heightmaps
    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.xCoord;
        float terrainZ = worldZ - this.zCoord;

        float gridSquareSize = GlobalConstants.MAP_SIZE / ((float)pixelHeights.length - 1);
        int gridX = (int)Math.floor(terrainX / gridSquareSize);
        int gridZ = (int)Math.floor(terrainZ / gridSquareSize);

        if (gridX < 0 || gridX >= pixelHeights.length - 1 || gridZ < 0 || gridZ >= pixelHeights.length - 1) {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;

        if (xCoord <= (1 - zCoord)) {
            answer = Mathematics.barycentricInterpolation(new Vector3f(0, pixelHeights[gridX][gridZ], 0),
                    new Vector3f(1, pixelHeights[gridX + 1][gridZ], 0),
                    new Vector3f(0, pixelHeights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = Mathematics.barycentricInterpolation(new Vector3f(1, pixelHeights[gridX + 1][gridZ], 0),
                    new Vector3f(1, pixelHeights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, pixelHeights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;
    }
}
