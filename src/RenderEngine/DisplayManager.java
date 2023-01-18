/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 25, 2020
 */

package RenderEngine;

import Utilities.ResourceStreamReader;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DisplayManager {

    public static final int ESCAPE = 1; // LWJGL Keycode for escape == 1 (Windowed mode)
    public static final int F12 = 88; // LWJGL Keycode for F12 == 88 (Fullscreen mode)
    public static final int WINDOW_WIDTH = 1280; // window width in pixels
    public static final int WINDOW_HEIGHT = 720; // window height in pixels
    public static final int GAMEVIEW_WIDTH = 330; // gameview width in pixels
    public static final int GAMEVIEW_HEIGHT = 150; // gameview height in pixels
    private static final int FPS_MAX = 120; // maximum Frames Per Second

    private static long lastFrameTime; // time of last frame in milliseconds
    private static float deltaTime; // time between frames

    public static void createDisplay() {
        PixelFormat format = new PixelFormat(8, 8, 0, 8);
        ContextAttribs attributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setFullscreen(false);

            DisplayMode[] modes = Display.getAvailableDisplayModes();
            DisplayMode mode = new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT); // default window

            for (int i = 0; i < modes.length; i++) {
                if (modes[i].getBitsPerPixel() == 32 && modes[i].getFrequency() == 50 && modes[i].isFullscreenCapable()
                        && modes[i].getHeight() == WINDOW_HEIGHT && modes[i].getWidth() == WINDOW_WIDTH) {
                    mode = modes[i];
                    break;
                }
            }

            Display.setDisplayMode(mode);

            Display.create(format, attributes);
            Display.setTitle("Blue July");

            GL11.glEnable(GL13.GL_MULTISAMPLE);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        try {
            Display.setIcon(new ByteBuffer[] {
                new ImageIOImageData().imageToByteBuffer(ImageIO.read(ResourceStreamReader.getResourceStream("res/2D Textures/Icons/OSRS LOGO16.png")),
                        false, false, null),
                new ImageIOImageData().imageToByteBuffer(ImageIO.read(ResourceStreamReader.getResourceStream("res/2D Textures/Icons/OSRS LOGO32.png")),
                        false, false, null),
                new ImageIOImageData().imageToByteBuffer(ImageIO.read(ResourceStreamReader.getResourceStream("res/2D Textures/Icons/OSRS LOGO64.png")),
                        false, false, null),
                new ImageIOImageData().imageToByteBuffer(ImageIO.read(ResourceStreamReader.getResourceStream("res/2D Textures/Icons/OSRS LOGO128.png")),
                        false, false, null)
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        lastFrameTime = getCurrentTimeInMilliseconds();
    }

    public static void updateDisplay() {
        Display.sync(FPS_MAX);
        Display.update();

        switchScreenMode();

        long currentFrameTime = getCurrentTimeInMilliseconds();
        deltaTime = (currentFrameTime - lastFrameTime) / 1000f; // delta stores time in seconds
        lastFrameTime = currentFrameTime;
    }

    public static void switchScreenMode() {
        if (Keyboard.isKeyDown(ESCAPE)) {
            try {
                Display.setFullscreen(false);
            } catch(LWJGLException e) {
                e.printStackTrace();
            }
        } else if (Keyboard.isKeyDown(F12)) {
            try {
                Display.setFullscreen(true);
            } catch(LWJGLException e) {
                e.printStackTrace();
            }
        }
    }

    // game camera
    public static void changeToGameView() {
        GL11.glViewport(0, GAMEVIEW_HEIGHT, WINDOW_WIDTH - GAMEVIEW_WIDTH, WINDOW_HEIGHT - GAMEVIEW_HEIGHT);
    }

    // HUD
    public static void changeToHUDView() {
        GL11.glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    public static long getCurrentTimeInMilliseconds() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    public static float getFrameTimeInSeconds() {
        return deltaTime;
    }

    public static int getGameviewWidth() {
        return GAMEVIEW_WIDTH;
    }

    public static int getGameviewHeight() {
        return GAMEVIEW_HEIGHT;
    }
}
