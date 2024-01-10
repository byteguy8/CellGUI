package cellgui.controls;

import cellgui.CColor;
import cellgui.base.CEnvironment;
import cellgui.CMouseInf;
import cellgui.CVector;
import cellgui.base.AbstractCell;
import cellgui.base.CBrush;
import cellgui.controls.listeners.MouseListener;
import cellgui.controls.listeners.events.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class CButton extends AbstractCell {
    private int fromX;
    private int fromY;
    private String text;
    private long lastTimePressed = 0;

    private String textToRender;
    private CColor backgroundColor;
    private CColor activeColor;
    private CColor pressedColor;
    private CColor textColor;

    private final List<MouseListener> mouseListeners = new ArrayList<>();

    public CButton(String text) {
        super(0, 0);
        this.text = text;
    }

    private void notifyMouseListenersPressed(MouseEvent mouseEvent) {
        for (MouseListener mouseListener : mouseListeners) {
            mouseListener.onPressed(mouseEvent);
        }
    }

    private void notifyMouseListenersReleased(MouseEvent mouseEvent) {
        for (MouseListener mouseListener : mouseListeners) {
            mouseListener.onReleased(mouseEvent);
        }
    }

    private String calcText() {
        int until = 0;
        int width = getWidth();
        CEnvironment environment = getEnvironment();

        for (int i = 0; i < text.length(); i++) {
            int w = environment.getStringWidth(text.substring(0, until + 1));

            if (w > width) {
                break;
            }

            until++;
        }

        return text.substring(0, until);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CColor getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(CColor activeColor) {
        this.activeColor = activeColor;
    }

    public CColor getPressedColor() {
        return pressedColor;
    }

    public void setPressedColor(CColor pressedColor) {
        this.pressedColor = pressedColor;
    }

    public CColor getTextColor() {
        return textColor;
    }

    public void setTextColor(CColor textColor) {
        this.textColor = textColor;
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeClickListener(MouseListener listener) {
        mouseListeners.remove(listener);
    }

    @Override
    public void setBackgroundColor(CColor backgroundColor) {
        super.setBackgroundColor(backgroundColor);
        //this.backgroundColor = backgroundColor;
    }

    @Override
    public void onInit() {
        super.onInit();

        backgroundColor = getBackgroundColor();

        setStartPadding(20);
        setEndPadding(20);
        setTopPadding(10);
        setBottomPadding(10);

        setWidth(getEnvironment().getStringWidth(text));
        setHeight(getEnvironment().getFontHeight());
    }

    @Override
    public void onEvent() {
        super.onEvent();

        CMouseInf mouseInf = getEnvironment().mouseInf;
        CVector position = mouseInf.getPosition();

        if (isInside(position.getX(), position.getY())) {
            if (mouseInf.isLeftPressed()) {
                notifyMouseListenersPressed(new MouseEvent(MouseEvent.Button.LEFT));
            }

            if (mouseInf.isRightPressed()) {
                notifyMouseListenersPressed(new MouseEvent(MouseEvent.Button.RIGHT));
            }

            if (mouseInf.isLeftClicked()) {
                notifyMouseListenersReleased(new MouseEvent(MouseEvent.Button.LEFT));
            }

            if (mouseInf.isRightClicked()) {
                notifyMouseListenersReleased(new MouseEvent(MouseEvent.Button.RIGHT));
            }
        }
    }

    @Override
    public void onLayout() {
        super.onLayout();

        textToRender = calcText();

        fromX = (getWidth() - getEnvironment().getStringWidth(textToRender)) / 2;
        fromY = (getHeight() - getEnvironment().getFontHeight()) / 2;
    }

    @Override
    public void onBeforeDraw() {
        super.onBeforeDraw();

        CMouseInf mouseInf = getEnvironment().mouseInf;
        CVector position = getEnvironment().mouseInf.getPosition();

        if (lastTimePressed > 0) {
            long time = System.currentTimeMillis();

            if (time - lastTimePressed >= 250) {
                setBackgroundColor(backgroundColor);
                lastTimePressed = 0;
            }
        }

        boolean inside = isInside(position.getX(), position.getY());
        boolean insideHandler = isInsideHandler(position.getX(), position.getY());

        if (inside && !insideHandler) {
            if (mouseInf.isLeftClicked()) {
                lastTimePressed = System.currentTimeMillis();
                setBackgroundColor(pressedColor);
            } else if (mouseInf.isLeftPressed()) {
                setBackgroundColor(pressedColor);
            } else {
                setBackgroundColor(activeColor);
            }
        } else {
            setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void onDraw(CBrush drawer) {
        super.onDraw(drawer);

        drawer.drawString(
                textToRender,
                fromX,
                fromY,
                textColor
        );
    }
}
