/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package PostProcessing;

import Bloom.BrightFilter;
import Bloom.CombineFilter;
import Engine.Main;
import GaussianBlur.HorizontalBlur;
import GaussianBlur.VerticalBlur;
import Models.RawModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class PostProcessing {
	
	private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};

	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static BrightFilter brightFilter;
	private static HorizontalBlur horizontalBlur;
	private static VerticalBlur verticalBlur;
	private static CombineFilter combineFilter;

	public static void init() {
		quad = Main.LOADER.loadQuadToVAO(POSITIONS);
		contrastChanger = new ContrastChanger();
		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		horizontalBlur = new HorizontalBlur(Display.getWidth() / 5, Display.getHeight() / 5);
		verticalBlur = new VerticalBlur(Display.getWidth() / 5, Display.getHeight() / 5);
		combineFilter = new CombineFilter();
	}

	public static void doPostProcessing(int colourTexture) {
		start();

		// bloom effect
		/*
		brightFilter.render(colourTexture);
		horizontalBlur.render(brightFilter.getOutputTexture(), false);
		verticalBlur.render(horizontalBlur.getOutputTexture(), false);
		combineFilter.render(colourTexture, verticalBlur.getOutputTexture());
		*/

		contrastChanger.render(colourTexture, true);

		end();
	}
	
	public static void free() {
		contrastChanger.free();
		brightFilter.free();
		horizontalBlur.free();
		verticalBlur.free();
		combineFilter.free();
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
