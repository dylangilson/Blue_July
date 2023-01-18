/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Animation;

import ColladaLoader.ColladaLoader;
import DataStructures.AnimatedModelData;
import DataStructures.JointData;
import DataStructures.MeshData;
import DataStructures.SkeletonData;
import Textures.Texture;
import Utilities.GlobalConstants;
import Utilities.InternalJarFile;

public class AnimatedModelLoader {

	/**
	 * Creates an AnimatedEntity from the data in an entity file. It loads up
	 * the collada model data, stores the extracted data in a VAO, sets up the
	 * joint hierarchy, and loads up the entity's texture.
	 * 
	 * @param modelFile
	 *            - the file containing the data for the entity.
	 * @param textureFile
	 *            - the file containing the data for the entity.
	 * @return The animated entity (no animation applied though)
	 */
	public static AnimatedModel loadEntity(InternalJarFile modelFile, InternalJarFile textureFile) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, GlobalConstants.MAX_WEIGHTS);
		AnimationVAO model = createVao(entityData.getMeshData());
		Texture texture = loadTexture(textureFile);
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);

		return new AnimatedModel(model, texture, headJoint, skeletonData.jointCount);
	}

	/**
	 * Loads up the diffuse texture for the model.
	 * 
	 * @param textureFile
	 *            - the texture file.
	 * @return The diffuse texture.
	 */
	private static Texture loadTexture(InternalJarFile textureFile) {
		Texture diffuseTexture = Texture.newTexture(textureFile).anisotropic().create();

		return diffuseTexture;
	}

	/**
	 * Constructs the joint-hierarchy skeleton from the data extracted from the
	 * collada file.
	 * 
	 * @param data
	 *            - the joint's data from the collada file for the head joint.
	 * @return The created joint, with all its descendants added.
	 */
	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameID, data.bindLocalTransform);

		for (int i = 0; i < data.children.size(); i++) {
			joint.addChild(createJoints(data.children.get(i)));
		}

		return joint;
	}

	/**
	 * Stores the mesh data in a VAO.
	 * 
	 * @param data
	 *            - all the data about the mesh that needs to be stored in the
	 *            VAO.
	 * @return The VAO containing all the mesh data for the model.
	 */
	private static AnimationVAO createVao(MeshData data) {
		AnimationVAO vao = AnimationVAO.create();

		vao.bind(null);

		vao.createIndexBuffer(data.getIndices());
		vao.createAttribute(0, data.getVertices(), 3);
		vao.createAttribute(1, data.getTextureCoords(), 2);
		vao.createAttribute(2, data.getNormals(), 3);
		vao.createIntAttribute(3, data.getJointIds(), 3);
		vao.createAttribute(4, data.getVertexWeights(), 3);

		vao.unbind(null);

		return vao;
	}
}
