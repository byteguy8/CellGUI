package cellgui.base;

import cellgui.CMouseInf;
import cellgui.CRect;
import cellgui.base.Cell;

import java.awt.*;

public class CEnvironment {
    public static class Focus {
        private int action;
        private Cell focused;

        public Focus(int action, Cell focused) {
            this.action = action;
            this.focused = focused;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public Cell getFocused() {
            return focused;
        }

        public void setFocused(Cell focused) {
            this.focused = focused;
        }
    }

    public final CRect winRect;
    public final CRect canvasRect;
    public final CMouseInf mouseInf;

    private final Graphics2D graphics2D;
    private final Font defaultFont;

    public CEnvironment(
            CRect winRect,
            CRect canvasRect,
            CMouseInf mouseInf,
            Graphics2D graphics2D
    ) {
        this.winRect = winRect;
        this.canvasRect = canvasRect;
        this.mouseInf = mouseInf;

        this.graphics2D = graphics2D;
        this.defaultFont = graphics2D.getFont();
    }

    public void restart() {
        graphics2D.setFont(defaultFont);
    }

    public void setFontSize(int size) {
        graphics2D.setFont(new Font(Font.SERIF, Font.PLAIN, size));
    }

    public int getCharWidth(char c) {
        final FontMetrics metrics = graphics2D.getFontMetrics();

        return metrics.charWidth(c);
    }

    public int getStringWidth(String str) {
        final FontMetrics metrics = graphics2D.getFontMetrics();

        return metrics.stringWidth(str);
    }

    public int getFontHeight() {
        final FontMetrics metrics = graphics2D.getFontMetrics();

        return metrics.getMaxAscent() + metrics.getMaxDescent();
    }
}
