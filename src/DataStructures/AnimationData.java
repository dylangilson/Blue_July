/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package DataStructures;

/**
 * Contains the extracted data for an animation, which includes the length of
 * the entire animation and the data for all the keyframes of the animation.
 * 
 * @author Eliseo
 *
 */
public class AnimationData {

	public final float lengthSeconds;
	public final KeyFrameData[] keyFrames;

	public AnimationData(float lengthSeconds, KeyFrameData[] keyFrames) {
		this.lengthSeconds = lengthSeconds;
		this.keyFrames = keyFrames;
	}
}
