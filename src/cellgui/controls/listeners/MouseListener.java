package cellgui.controls.listeners;

import cellgui.controls.listeners.events.MouseEvent;

public interface MouseListener {
    void onPressed(MouseEvent event);

    void onReleased(MouseEvent event);
}
