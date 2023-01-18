/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package Models;

public class RawModel {

    private int vaoID; // index of vao
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
