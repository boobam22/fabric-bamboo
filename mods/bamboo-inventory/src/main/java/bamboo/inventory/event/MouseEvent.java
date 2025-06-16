package bamboo.inventory.event;

public class MouseEvent {
    public static final MouseEvent INSTANCE = new MouseEvent();

    private MouseEvent() {
    }

    public boolean handleClick(long window, int button, int action, int modifiers) {
        return false;
    }

    public boolean handleScroll(long window, double horizontal, double vertical) {
        return false;
    }

    public boolean handleMove(long window, double x, double y) {
        return false;
    }
}
