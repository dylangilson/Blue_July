/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package ColladaLoader;

import DataStructures.SkinningData;
import DataStructures.VertexSkinData;
import XMLParser.XMLNode;

import java.util.ArrayList;
import java.util.List;

public class SkinLoader {

	private final XMLNode skinningData;
	private final int maxWeights;

	public SkinLoader(XMLNode controllersNode, int maxWeights) {
		this.skinningData = controllersNode.getChild("controller").getChild("skin");
		this.maxWeights = maxWeights;
	}

	public SkinningData extractSkinData() {
		List<String> jointsList = loadJointsList();
		float[] weights = loadWeights();
		XMLNode weightsDataNode = skinningData.getChild("vertex_weights");
		int[] effectorJointCounts = getEffectiveJointsCounts(weightsDataNode);
		List<VertexSkinData> vertexWeights = getSkinData(weightsDataNode, effectorJointCounts, weights);

		return new SkinningData(jointsList, vertexWeights);
	}

	private List<String> loadJointsList() {
		XMLNode inputNode = skinningData.getChild("vertex_weights");
		String jointDataID = inputNode.getChildWithAttribute("input", "semantic", "JOINT").getAttribute("source").substring(1);
		XMLNode jointsNode = skinningData.getChildWithAttribute("source", "id", jointDataID).getChild("Name_array");
		String[] names = jointsNode.getData().split(" ");
		List<String> jointsList = new ArrayList<String>();

		for (int i = 0; i < names.length; i++) {
			jointsList.add(names[i]);
		}

		return jointsList;
	}

	private float[] loadWeights() {
		XMLNode inputNode = skinningData.getChild("vertex_weights");
		String weightsDataID = inputNode.getChildWithAttribute("input", "semantic", "WEIGHT").getAttribute("source").substring(1);
		XMLNode weightsNode = skinningData.getChildWithAttribute("source", "id", weightsDataID).getChild("float_array");
		String[] rawData = weightsNode.getData().split(" ");
		float[] weights = new float[rawData.length];

		for (int i = 0; i < weights.length; i++) {
			weights[i] = Float.parseFloat(rawData[i]);
		}

		return weights;
	}

	private int[] getEffectiveJointsCounts(XMLNode weightsDataNode) {
		String[] rawData = weightsDataNode.getChild("vcount").getData().split(" ");
		int[] counts = new int[rawData.length];

		for (int i = 0; i < rawData.length; i++) {
			counts[i] = Integer.parseInt(rawData[i]);
		}

		return counts;
	}

	private List<VertexSkinData> getSkinData(XMLNode weightsDataNode, int[] counts, float[] weights) {
		String[] rawData = weightsDataNode.getChild("v").getData().split(" ");
		List<VertexSkinData> skinningData = new ArrayList<VertexSkinData>();
		int pointer = 0;

		for (int i = 0; i < counts.length; i++) {
			VertexSkinData skinData = new VertexSkinData();

			for (int j = 0; j < counts[i]; j++) {
				int jointId = Integer.parseInt(rawData[pointer++]);
				int weightId = Integer.parseInt(rawData[pointer++]);
				skinData.addJointEffect(jointId, weights[weightId]);
			}

			skinData.limitJointNumber(maxWeights);

			skinningData.add(skinData);
		}

		return skinningData;
	}
}
