package cellgui.controls.listeners.events;

public class MouseEvent {
    public enum Button {
        LEFT, RIGHT
    }

    public MouseEvent(Button button) {
        this.button = button;
    }

    public final Button button;
}
