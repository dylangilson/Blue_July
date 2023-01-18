/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 16, 2021
 */

package Particles;

public class ParticleTexture {

    private int textureID;
    private int numberOfRows; // number of rows in textureAtlas ; 1 -> not an atlas
    private boolean useAdditiveBlending; // useful for magic effects, glows, etc.

    public ParticleTexture(int textureID, int numberOfRows, boolean useAdditiveBlending) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
        this.useAdditiveBlending = useAdditiveBlending;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public boolean usesAdditiveBlending() {
        return useAdditiveBlending;
    }
}
