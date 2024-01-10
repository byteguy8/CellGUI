package cellgui.base;

import cellgui.CColor;

import java.awt.image.BufferedImage;

public interface CDrawer {
    void drawJImage(int x, int y, BufferedImage image);

    void drawHandler();

    void fillWith(CColor CColor);

    void drawString(String str, int x, int y, CColor color);

    void drawRect(int x, int y, int w, int h, CColor color, boolean fill);

    void drawCell(int x, int y, Cell cell);
}
