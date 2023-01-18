/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 12, 2021
 */

package Font;

import Entities.Player;

import org.lwjgl.input.Keyboard;

// LWJGL key codes: https://gist.github.com/Mumfrey/5cfc3b7e14fef91b6fa56470dc05218a

public class KeyboardHandler {

    private static final int FIRST_INPUT_VALUE = 16; // key code of first alphabet character
    private static final int LAST_INPUT_VALUE = 50; // key code of final alphabet character
    private static final int CHARACTERS_AFTER_USERNAME = 2;

    // special keys
    public static final int KEY1_EXCLAMATION = 2;
    public static final int KEY2_AT = 3;
    public static final int KEY3_HASHTAG = 4;
    public static final int KEY4_DOLLAR = 5;
    public static final int KEY5_PERCENT = 6;
    public static final int KEY6_POWER = 7;
    public static final int KEY7_AND = 8;
    public static final int KEY8_STAR = 9;
    public static final int KEY9_LEFTPARENTHESIS = 10;
    public static final int KEY0_RIGHTPARENTHESIS = 11;
    public static final int MINUS_UNDERSCORE = 12;
    public static final int EQUALS_PLUS = 13;
    public static final int BACKSPACE = 14;
    public static final int TAB = 15;
    public static final int LEFTBRACKET_LEFTCURLYBRACE = 26;
    public static final int RIGHTBRACKET_RIGHTCURLYBRACE = 27;
    public static final int RETURN = 28;
    public static final int LEFT_CTRL = 29;
    public static final int SEMICOLON_COLON = 39;
    public static final int APOSTROPHE_QUOTATION = 40;
    public static final int GRAVE_TILDA = 41;
    public static final int LEFT_SHIFT = 42;
    public static final int BACKSLASH_OR = 43;
    public static final int COMMA_LESSTHAN = 51;
    public static final int PERIOD_GREATERTHAN = 52;
    public static final int SLASH_QUESTION = 53;
    public static final int RIGHT_SHIFT = 54;
    public static final int SPACE = 57;
    public static final int DELETE = 211;

    public static void getInput(Player player) {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == BACKSPACE || Keyboard.getEventKey() == DELETE) {
                    if (TextMaster.currentText.getTextString().length() > player.getName().length() + CHARACTERS_AFTER_USERNAME) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString().substring(0, TextMaster.currentText.getTextString().length() - 1));
                    }
                } else if (Keyboard.getEventKey() == LEFT_CTRL || Keyboard.getEventKey() == BACKSLASH_OR) {
                    return;
                } else if (Keyboard.getEventKey() == TAB) {
                    TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "    ");
                } else if (Keyboard.getEventKey() == SPACE) {
                    TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + " ");
                } else if (Keyboard.getEventKey() == RETURN) {
                    if (TextMaster.currentText.getTextString().length() != player.getName().length() + CHARACTERS_AFTER_USERNAME) {
                        TextMaster.updateChatLog(TextMaster.currentText.getTextString(), TextMaster.USER_TEXT_COLOUR);
                        TextMaster.removeText(TextMaster.currentText);
                        TextMaster.setCurrentText();

                        return;
                    }
                } else if (Keyboard.getEventKey() == KEY1_EXCLAMATION) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "!");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "1");
                    }
                } else if (Keyboard.getEventKey() == KEY2_AT) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "@");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "2");
                    }
                } else if (Keyboard.getEventKey() == KEY3_HASHTAG) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "#");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "3");
                    }
                } else if (Keyboard.getEventKey() == KEY4_DOLLAR) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "$");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "4");
                    }
                } else if (Keyboard.getEventKey() == KEY5_PERCENT) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "%");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "5");
                    }
                } else if (Keyboard.getEventKey() == KEY6_POWER) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "^");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "6");
                    }
                } else if (Keyboard.getEventKey() == KEY7_AND) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "&");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "7");
                    }
                } else if (Keyboard.getEventKey() == KEY8_STAR) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "*");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "8");
                    }
                } else if (Keyboard.getEventKey() == KEY9_LEFTPARENTHESIS) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "(");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "9");
                    }
                } else if (Keyboard.getEventKey() == KEY0_RIGHTPARENTHESIS) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + ")");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "0");
                    }
                } else if (Keyboard.getEventKey() == MINUS_UNDERSCORE) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "_");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "-");
                    }
                } else if (Keyboard.getEventKey() == EQUALS_PLUS) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "+");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "=");
                    }
                } else if (Keyboard.getEventKey() == LEFTBRACKET_LEFTCURLYBRACE) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "{");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "[");
                    }
                } else if (Keyboard.getEventKey() == RIGHTBRACKET_RIGHTCURLYBRACE) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "}");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "]");
                    }
                } else if (Keyboard.getEventKey() == SEMICOLON_COLON) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + ":");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + ";");
                    }
                } else if (Keyboard.getEventKey() == APOSTROPHE_QUOTATION) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "\"");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "'");
                    }
                } else if (Keyboard.getEventKey() == GRAVE_TILDA) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "~");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "`");
                    }
                } else if (Keyboard.getEventKey() == COMMA_LESSTHAN) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "<");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + ",");
                    }
                } else if (Keyboard.getEventKey() == PERIOD_GREATERTHAN) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + ">");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + ".");
                    }
                } else if (Keyboard.getEventKey() == SLASH_QUESTION) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "?");
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + "/");
                    }
                } else if (Keyboard.getEventKey() >= FIRST_INPUT_VALUE && Keyboard.getEventKey() <= LAST_INPUT_VALUE) {
                    if (Keyboard.isKeyDown(LEFT_SHIFT) || Keyboard.isKeyDown(RIGHT_SHIFT)) {
                        if (Keyboard.getEventKey() != LEFT_SHIFT) {
                            TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + Keyboard.getKeyName(Keyboard.getEventKey()));
                        }
                    } else {
                        TextMaster.currentText.setTextString(TextMaster.currentText.getTextString() + Keyboard.getKeyName(Keyboard.getEventKey()).toLowerCase());
                    }
                }

                TextMaster.updateCurrentText();
            }
        }
    }
}
