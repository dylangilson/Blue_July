/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 23, 2021
 */

package Utilities;

import Entities.Entity;

public class Physics {

    // returns the 3D distance between two entities
    public static float calculateDistanceBetweenEntities(Entity a, Entity b) {
        float dX = (a.getPosition().x - b.getPosition().x) * (a.getPosition().x - b.getPosition().x);
        float dY = (a.getPosition().y - b.getPosition().y) * (a.getPosition().y - b.getPosition().y);
        float dZ = (a.getPosition().z - b.getPosition().z) * (a.getPosition().z - b.getPosition().z);

        return (float)Math.sqrt(dX + dY + dZ);
    }
}
