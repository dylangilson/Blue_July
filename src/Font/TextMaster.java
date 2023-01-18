/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 2, 2021
 */

package Font;

import Engine.Main;
import Entities.Player;
import RenderEngine.FontRenderer;
import Utilities.GlobalConstants;

import Utilities.InternalJarFile;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMaster {

    public static final int NUM_LINES = 7;
    public static final float CHATLOG_FONT_SIZE = 1f;
    public static final float CHATLOG_X_OFFSET = 0.012f;
    public static final float CHATLOG_Y_OFFSET = 0.961f;
    public static final float CHATLOG_NEXT_LINE_Y_OFFSET = 0.0265f;
    public static final float BORDERWIDTH_NO_EFFECT = 0f;
    public static final float BORDEREDGE_NO_EFFECT = 0.4f;
    public static final float BORDERWIDTH_NORMAL_EFFECT = 0.7f;
    public static final float BORDEREDGE_NORMAL_EFFECT = 0.1f;
    public static final float BORDERWIDTH_GLOW_EFFECT = 0.4f;
    public static final float BORDEREDGE_GLOW_EFFECT = 0.5f;
    public static final float MIN_LINE_LENGTH = 0f;
    public static final float MAX_LINE_LENGTH = 1f;
    public static final int ALPHANUMERIC_ASCII_OFFSET = 32;
    public static final int MAX_CHARACTERS_PER_MESSAGE = 120;
    public static final Vector3f NORMAL_TEXT_COLOUR = GlobalConstants.BLACK;
    public static final Vector3f USER_TEXT_COLOUR = GlobalConstants.ULTRAMARINE;
    public static final Vector3f CELEBRATORY_TEXT_COLOUR = GlobalConstants.PURPLE;
    public static final Vector3f VALUABLE_TEXT_COLOUR = GlobalConstants.DARK_GOLDENROD;
    public static final Vector3f FRIENDLY_TEXT_COLOUR = GlobalConstants.FOREST_GREEN;
    public static final Vector3f ENEMY_TEXT_COLOUR = GlobalConstants.RED;

    private static final Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();

    public static FontType MALGUN_GOTHIC;
    public static GUIText currentText;
    public static GUIText[] chatLogTexts;

    private static Player player;
    private static FontRenderer renderer;

    public static void init(Player player) {
        renderer = new FontRenderer();
        TextMaster.player = player;
        MALGUN_GOTHIC = new FontType(Main.LOADER.loadFontTexture("Malgun Gothic", "Fonts"), new InternalJarFile("res/2D Textures/Fonts/Malgun Gothic.fnt"));

        chatLogTexts = new GUIText[NUM_LINES];
        chatLogTexts[0] = new GUIText("Welcome to Blue July.", CHATLOG_FONT_SIZE, MALGUN_GOTHIC, GlobalConstants.BLACK, NORMAL_TEXT_COLOUR,
                new Vector2f(BORDERWIDTH_NO_EFFECT, BORDEREDGE_NO_EFFECT), new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET - CHATLOG_NEXT_LINE_Y_OFFSET),
                MAX_LINE_LENGTH, false);
        loadText(chatLogTexts[0]);

        for (int i = 1; i < NUM_LINES; i++) {
            chatLogTexts[i] = new GUIText("", CHATLOG_FONT_SIZE, MALGUN_GOTHIC, GlobalConstants.BLACK, NORMAL_TEXT_COLOUR, new Vector2f(BORDERWIDTH_NO_EFFECT,
                    BORDEREDGE_NO_EFFECT), new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET - (i + 1) * CHATLOG_NEXT_LINE_Y_OFFSET), MAX_LINE_LENGTH, false);
            loadText(chatLogTexts[i]);
        }

        currentText = new GUIText(player.getName() + ": ", CHATLOG_FONT_SIZE, MALGUN_GOTHIC, USER_TEXT_COLOUR, NORMAL_TEXT_COLOUR,
                new Vector2f(BORDERWIDTH_NO_EFFECT, BORDEREDGE_NO_EFFECT), new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET), MAX_LINE_LENGTH, false);
        loadText(currentText);
    }

    public static void setCurrentText() {
        currentText = new GUIText(player.getName() + ": ", CHATLOG_FONT_SIZE, MALGUN_GOTHIC, USER_TEXT_COLOUR, NORMAL_TEXT_COLOUR,
                new Vector2f(BORDERWIDTH_NO_EFFECT, BORDEREDGE_NO_EFFECT), new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET), MAX_LINE_LENGTH, false);
        loadText(currentText);
    }

    // to update chatlog with a message, let newText = currentText.getTextString()
    public static void updateChatLog(String newText, Vector3f textColour) {
        String[] chats = new String[NUM_LINES];
        Vector3f[] textColours = new Vector3f[NUM_LINES];

        for (int i = 0; i < NUM_LINES - 1; i++) {
            if (chatLogTexts[i] != null) {
                textColours[i] = chatLogTexts[i].getColour();
            }
        }

        for (int i = 0; i < NUM_LINES - 1; i++) {
            chats[i] = chatLogTexts[i].getTextString();
            removeText(chatLogTexts[i]);
        }

        removeText(chatLogTexts[NUM_LINES - 1]);

        chatLogTexts[0] = new GUIText(newText, CHATLOG_FONT_SIZE, MALGUN_GOTHIC, textColour, NORMAL_TEXT_COLOUR,
                new Vector2f(BORDERWIDTH_NO_EFFECT,BORDEREDGE_NO_EFFECT), new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET - CHATLOG_NEXT_LINE_Y_OFFSET),
                MAX_LINE_LENGTH, false);

        for (int i = 1; i < NUM_LINES; i++) {
            chatLogTexts[i] = new GUIText(chats[i - 1], CHATLOG_FONT_SIZE, MALGUN_GOTHIC, textColours[i - 1], NORMAL_TEXT_COLOUR,
                    new Vector2f(BORDERWIDTH_NO_EFFECT, BORDEREDGE_NO_EFFECT),
                    new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET - (i + 1) * CHATLOG_NEXT_LINE_Y_OFFSET), MAX_LINE_LENGTH, false);
        }

        if (newText.startsWith(player.getName() + ": ")) {
            removeText(currentText);
            setCurrentText();
        }

        for (int i = 0; i < NUM_LINES; i++) {
            loadText(chatLogTexts[i]);
        }
    }

    public static void updateCurrentText() {
        removeText(currentText);

        currentText = new GUIText(currentText.getTextString(), CHATLOG_FONT_SIZE, MALGUN_GOTHIC, USER_TEXT_COLOUR, NORMAL_TEXT_COLOUR,
                new Vector2f(BORDERWIDTH_NO_EFFECT, BORDEREDGE_NO_EFFECT), new Vector2f(CHATLOG_X_OFFSET, CHATLOG_Y_OFFSET), MAX_LINE_LENGTH, false);
        loadText(currentText);
    }

    public static void render() {
        renderer.render(texts);
    }

    public static void loadText(GUIText text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vaoID = Main.LOADER.loadTextToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vaoID, data.getVertexCount());

        List<GUIText> textBatch = texts.get(font);

        if (textBatch == null) {
            textBatch = new ArrayList<GUIText>();
            texts.put(font, textBatch);
        }

        textBatch.add(text);
    }

    public static void removeText(GUIText text) {
        List<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);

        if (textBatch.isEmpty()) {
            texts.remove(text.getFont());
        }
    }

    public static void free() {
        renderer.free();
    }
}
