/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 23, 2023
 */

package Engine;

import Font.GUIText;
import Font.TextMaster;
import Utilities.GlobalConstants;
import org.lwjgl.util.vector.Vector2f;

public class Ignore {

    private GUIText text;

    public void renderPanel() {
        loadText(new Vector2f(0.7605f, 0.55f));
    }

    public void clearPanel() {
        removeText();
    }

    public void loadText(Vector2f position) {
        this.text = new GUIText("Ignore list will be available with the addition of Multiplayer!", 1.75f, TextMaster.MALGUN_GOTHIC,
                GlobalConstants.SKY_BLUE, TextMaster.NORMAL_TEXT_COLOUR, new Vector2f(TextMaster.BORDERWIDTH_NORMAL_EFFECT, TextMaster.BORDEREDGE_NORMAL_EFFECT),
                position, TextMaster.MAX_LINE_LENGTH / 5, true);

        TextMaster.loadText(this.text);
    }

    public void removeText() {
        TextMaster.removeText(this.text);
    }

    public GUIText getText() {
        return text;
    }
}
