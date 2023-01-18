/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 16, 2021
 */

package Particles;

import RenderEngine.DisplayManager;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.Random;

public class ParticleSystem {

	private ParticleTexture texture;
	private float pps; // particles per second
	private float averageSpeed; // average speed that the particles are emitted at
	private float gravityCompliment; // effect of gravity on each particle
	private float averageLifeLength; // average length of each particle in seconds
	private float averageScaleX;
	private float averageScaleY;

	private float speedError = 0;
	private float lifeError = 0;
	private float scaleErrorX = 0;
	private float scaleErrorY = 0;
	private boolean randomRotation = false;
	private Vector3f direction;
	private float directionDeviation = 0;

	private Random random = new Random();

	public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityCompliment, float lifeLength, float scaleX, float scaleY) {
		this.texture = texture;
		this.pps = pps;
		this.averageSpeed = speed;
		this.gravityCompliment = gravityCompliment;
		this.averageLifeLength = lifeLength;
		this.averageScaleX = scaleX;
		this.averageScaleY = scaleY;
	}

	/**
	 * @param direction - The average direction in which particles are emitted.
	 * @param deviation - A value between 0 and 1 indicating how far from the chosen direction particles can deviate.
	 */
	public void setDirection(Vector3f direction, float deviation) {
		this.direction = new Vector3f(direction);
		this.directionDeviation = (float)(deviation * Math.PI);
	}

	public void randomizeRotation() {
		randomRotation = true;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setSpeedError(float error) {
		this.speedError = error * averageSpeed;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setLifeError(float error) {
		this.lifeError = error * averageLifeLength;
	}

	/**
	 * @param errorX
	 *            - A number between 0 and 1, where 0 means no error margin.
	 * @param errorY
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setScaleError(float errorX, float errorY) {
		this.scaleErrorX = errorX * averageScaleX;
		this.scaleErrorY = errorY * averageScaleX;
	}

	public void generateParticles(Vector3f systemCenter) {
		float delta = DisplayManager.getFrameTimeInSeconds();
		float particlesToCreate = pps * delta;
		int count = (int)Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;

		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter);
		}

		if (Math.random() < partialParticle) {
			emitParticle(systemCenter);
		}
	}

	private void emitParticle(Vector3f center) {
		Vector3f velocity = null;

		if (direction != null) {
			velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
		} else {
			velocity = generateRandomUnitVector();
		}

		velocity.normalise();
		velocity.scale(generateValue(averageSpeed, speedError));
		float scaleX = generateValue(averageScaleX, scaleErrorX);
		float scaleY = generateValue(averageScaleY, scaleErrorY);
		float lifeLength = generateValue(averageLifeLength, lifeError);

		new Particle(texture, new Vector3f(center), velocity, gravityCompliment, lifeLength, generateRotation(), scaleX, scaleY);
	}

	private float generateValue(float average, float errorMargin) {
		float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
		return average + offset;
	}

	private float generateRotation() {
		return randomRotation ? random.nextFloat() * 360f : 0;
	}

	private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		Random random = new Random();
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));

		Vector4f direction = new Vector4f(x, y, z, 1);
		if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
			Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
			rotateAxis.normalise();
			float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(-rotateAngle, rotateAxis);
			Matrix4f.transform(rotationMatrix, direction, direction);
		} else if (coneDirection.z == -1) {
			direction.z *= -1;
		}

		return new Vector3f(direction);
	}
	
	private Vector3f generateRandomUnitVector() {
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = (random.nextFloat() * 2) - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));

		return new Vector3f(x, y, z);
	}
}
