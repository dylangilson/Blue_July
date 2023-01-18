/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package RenderEngine;

import Models.RawModel;
import Textures.TextureData;
import Utilities.InternalJarFile;
import Utilities.ResourceStreamReader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private static final float ANISOTROPIC_FILTERING_AMOUNT = 4f;
    private static final float SHARPNESS = -0.4f;

    public static final List<Integer> VAOs = new ArrayList<Integer>();
    public static final List<Integer> VBOs = new ArrayList<Integer>();
    public static final List<Integer> TEXTURES = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, positions); // attributeID 0 -> position
        storeDataInAttributeList(1, 2, textureCoords); // attributeID 1 -> textureCoords
        storeDataInAttributeList(2, 3, normals); // attributeID 2 -> normals

        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadQuadToVAO(float[] positions) {
        int vaoID = createVAO();

        this.storeDataInAttributeList(0, 2, positions);

        unbindVAO();

        return new RawModel(vaoID, positions.length / 2);
    }

    public RawModel loadSkyboxToVAO(float[] positions) {
        int vaoID = createVAO();

        this.storeDataInAttributeList(0, 3, positions);

        unbindVAO();

        return new RawModel(vaoID, positions.length / 3);
    }

    public int loadTextToVAO(float[] positions, float[] textureCoords) {
        int vaoID = createVAO();

        storeDataInAttributeList(0, 2, positions); // attributeID 0 -> position
        storeDataInAttributeList(1, 2, textureCoords); // attributeID 1 -> textureCoords

        unbindVAO();

        return vaoID;
    }

    public int createEmptyVBO(int maxFloatCount) {
        int vboID = GL15.glGenBuffers();
        VBOs.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, maxFloatCount * 4L, GL15.GL_STREAM_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return vboID;
    }

    public void addInstancedAttribute(int vaoID, int vboID, int attribute, int dataSize, int stride, int offset) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL30.glBindVertexArray(vaoID);

        GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, stride * 4, offset * 4);
        GL33.glVertexAttribDivisor(attribute, 1);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind VBO
        GL30.glBindVertexArray(0); // unbind VAO
    }

    public void updateVBO(int vboID, float[] data, FloatBuffer buffer) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4L, GL15.GL_STREAM_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind VBO
    }

    public int loadTexture(String fileName, String folderName) {
        Texture texture = null;

        try {
            // texture must be square, with each dimension having 2^n pixels
            texture = TextureLoader.getTexture("PNG", ResourceStreamReader.getResourceStream("res/2D Textures/" + folderName + "/" + fileName + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            // enable anisotropic filtering
            if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);

                float amount = Math.min(ANISOTROPIC_FILTERING_AMOUNT, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));

                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
            } else {
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, SHARPNESS);

                System.out.println("Anisotropic filtering is not supported by your current graphics card drivers.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID = texture.getTextureID();
        TEXTURES.add(textureID);

        return textureID;
    }

    public int loadFontTexture(String fileName, String folderName) {
        Texture texture = null;

        try {
            // texture must be square, with each dimension having 2^n pixels
            texture = TextureLoader.getTexture("PNG", ResourceStreamReader.getResourceStream("res/2D Textures/" + folderName + "/" + fileName + ".png"));

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID = texture.getTextureID();

        TEXTURES.add(textureID);

        return textureID;
    }

    public void free() {
        for (int i = 0; i < VAOs.size(); i++) {
            GL30.glDeleteVertexArrays(VAOs.get(i));
        }

        for (int i = 0; i < VBOs.size(); i++) {
            GL15.glDeleteBuffers(VBOs.get(i));
        }

        for (int i = 0; i < TEXTURES.size(); i++) {
            GL11.glDeleteTextures(TEXTURES.get(i));
        }
    }

    private TextureData decodeTextureFile(InternalJarFile file) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer;

        try {
            BufferedInputStream bis = ResourceStreamReader.getResourceStream(file.getPath());
            PNGDecoder decoder = new PNGDecoder(bis);

            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(width * height * 4);
            decoder.decode(buffer, width * 4, PNGDecoder.RGBA);
            buffer.flip();

            bis.close();

            return new TextureData(buffer, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + file.getPath() + ", but it did not work");
            System.exit(-1);
            return null;
        }
    }

    public int[] loadCubeMap(String[] textureFilesDay, String[] textureFilesNight, String folderName) {
        int textureID1 = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID1);

        for (int i = 0; i < textureFilesDay.length; i++) {
            TextureData data = decodeTextureFile(new InternalJarFile("res/2D Textures/" + folderName + "/" + textureFilesDay[i] + ".png"));
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(),
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        TEXTURES.add(textureID1);

        int textureID2 = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID2);

        for (int i = 0; i < textureFilesNight.length; i++) {
            TextureData data = decodeTextureFile(new InternalJarFile("res/2D Textures/" + folderName + "/" + textureFilesNight[i] + ".png"));
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(),
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        TEXTURES.add(textureID2);

        return new int[]{textureID1, textureID2};
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays(); // creates empty VAO and returns the ID

        VAOs.add(vaoID);

        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    private void storeDataInAttributeList(int attributeID, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers(); // creates empty VBO and returns the ID

        VBOs.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        int stride = 0; // spacing between each data point i.e. 0 -> only the exactly necessary data is present
        int offset = 0; // 0 -> start at beginning of buffer
        GL20.glVertexAttribPointer(attributeID, coordinateSize, GL11.GL_FLOAT, false, stride, offset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind active buffer
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0); // binding to ID 0 -> unbind currently bound VAO
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers(); // creates an empty VBO and returns the ID

        VBOs.add(vboID);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip(); // change buffer from write to read mode

        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); // change buffer from write to read mode

        return buffer;
    }
}
