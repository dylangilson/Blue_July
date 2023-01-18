/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 2, 2021
 */

package Engine;

import RenderEngine.DisplayManager;

public class Delay {

    private int length; // length of delay
    private long endTime;
    private boolean isStarted;

    // length is in milliseconds
    public Delay(int length) {
        this.length = length;
        this.isStarted = false;
    }

    public void start() {
        isStarted = true;
        endTime = length + DisplayManager.getCurrentTimeInMilliseconds();
    }

    // end the delay
    public void end() {
        isStarted = true;
        endTime = 0;
    }

    public boolean isOver() {
        if (!isStarted) {
            return false;
        }

        return DisplayManager.getCurrentTimeInMilliseconds() >= endTime;
    }
}
