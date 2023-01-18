/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package ColladaLoader;

import DataStructures.AnimatedModelData;
import DataStructures.AnimationData;
import DataStructures.MeshData;
import DataStructures.SkeletonData;
import DataStructures.SkinningData;
import Utilities.InternalJarFile;
import XMLParser.XMLNode;
import XMLParser.XMLParser;

public class ColladaLoader {

	public static AnimatedModelData loadColladaModel(InternalJarFile colladaFile, int maxWeights) {
		XMLNode node = XMLParser.loadXmlFile(colladaFile);

		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();

		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		SkeletonData jointsData = jointsLoader.extractBoneData();

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();

		return new AnimatedModelData(meshData, jointsData);
	}

	public static AnimationData loadColladaAnimation(InternalJarFile colladaFile) {
		XMLNode node = XMLParser.loadXmlFile(colladaFile);
		XMLNode animNode = node.getChild("library_animations");
		XMLNode jointsNode = node.getChild("library_visual_scenes");
		ColladaAnimationLoader loader = new ColladaAnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();

		return animData;
	}
}
