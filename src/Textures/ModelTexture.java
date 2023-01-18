/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package Textures;

public class ModelTexture {

    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private boolean isTransparent = false;
    private boolean useFakeLighting = false; // true -> point all normals to (0, 1, 0) for constant lighting
    private int numberOfColumns = 1; // number of columns in texture atlas; 1 -> a regular texture, not an atlas

    public ModelTexture(int textureID) {
        this.textureID = textureID;
        setIsTransparent(true);
    }

    public int getTextureID() {
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity; // 0 -> not reflective ; 1 -> reflective
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setIsTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }
}
