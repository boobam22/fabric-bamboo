package bamboo.lib.keybinding;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

public class KeyMap {
    private static final HashMap<String, Integer> name2code = new HashMap<>();
    private static final HashMap<Integer, String> code2name = new HashMap<>();

    public static int toCode(String name) {
        return name2code.getOrDefault(name.toLowerCase(), -1);
    }

    public static String toName(int code) {
        return code2name.getOrDefault(code, "unknown");
    }

    private static void register(String name, int code) {
        name2code.put(name.toLowerCase(), code);
        code2name.put(code, name.toLowerCase());
    }

    static {
        register("0", GLFW.GLFW_KEY_0);
        register("1", GLFW.GLFW_KEY_1);
        register("2", GLFW.GLFW_KEY_2);
        register("3", GLFW.GLFW_KEY_3);
        register("4", GLFW.GLFW_KEY_4);
        register("5", GLFW.GLFW_KEY_5);
        register("6", GLFW.GLFW_KEY_6);
        register("7", GLFW.GLFW_KEY_7);
        register("8", GLFW.GLFW_KEY_8);
        register("9", GLFW.GLFW_KEY_9);

        register("a", GLFW.GLFW_KEY_A);
        register("b", GLFW.GLFW_KEY_B);
        register("c", GLFW.GLFW_KEY_C);
        register("d", GLFW.GLFW_KEY_D);
        register("e", GLFW.GLFW_KEY_E);
        register("f", GLFW.GLFW_KEY_F);
        register("g", GLFW.GLFW_KEY_G);
        register("h", GLFW.GLFW_KEY_H);
        register("i", GLFW.GLFW_KEY_I);
        register("j", GLFW.GLFW_KEY_J);
        register("k", GLFW.GLFW_KEY_K);
        register("l", GLFW.GLFW_KEY_L);
        register("m", GLFW.GLFW_KEY_M);
        register("n", GLFW.GLFW_KEY_N);
        register("o", GLFW.GLFW_KEY_O);
        register("p", GLFW.GLFW_KEY_P);
        register("q", GLFW.GLFW_KEY_Q);
        register("r", GLFW.GLFW_KEY_R);
        register("s", GLFW.GLFW_KEY_S);
        register("t", GLFW.GLFW_KEY_T);
        register("u", GLFW.GLFW_KEY_U);
        register("v", GLFW.GLFW_KEY_V);
        register("w", GLFW.GLFW_KEY_W);
        register("x", GLFW.GLFW_KEY_X);
        register("y", GLFW.GLFW_KEY_Y);
        register("z", GLFW.GLFW_KEY_Z);

        register("control", GLFW.GLFW_KEY_LEFT_CONTROL);
        register("shift", GLFW.GLFW_KEY_LEFT_SHIFT);
        register("alt", GLFW.GLFW_KEY_LEFT_ALT);
        register("super", GLFW.GLFW_KEY_LEFT_SUPER);
        register("space", GLFW.GLFW_KEY_SPACE);

        register("up_arrow", GLFW.GLFW_KEY_UP);
        register("down_arrow", GLFW.GLFW_KEY_DOWN);
        register("left_arrow", GLFW.GLFW_KEY_LEFT);
        register("right_arrow", GLFW.GLFW_KEY_RIGHT);

        register("f1", GLFW.GLFW_KEY_F1);
        register("f2", GLFW.GLFW_KEY_F2);
        register("f3", GLFW.GLFW_KEY_F3);
        register("f4", GLFW.GLFW_KEY_F4);
        register("f5", GLFW.GLFW_KEY_F5);
        register("f6", GLFW.GLFW_KEY_F6);
        register("f7", GLFW.GLFW_KEY_F7);
        register("f8", GLFW.GLFW_KEY_F8);
        register("f9", GLFW.GLFW_KEY_F9);
        register("f10", GLFW.GLFW_KEY_F10);
        register("f11", GLFW.GLFW_KEY_F11);
        register("f12", GLFW.GLFW_KEY_F12);

        register("left_button", GLFW.GLFW_MOUSE_BUTTON_LEFT);
        register("middle_button", GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
        register("right_button", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }
}
