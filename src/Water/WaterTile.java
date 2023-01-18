/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 22, 2021
 */

package Water;

public class WaterTile {
	
	public static final float TILE_SIZE = 30f;
	
	private float height;
	private float x;
	private float z;
	
	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
}
