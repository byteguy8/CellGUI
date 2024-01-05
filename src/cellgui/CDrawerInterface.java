package cellgui;

import java.awt.image.BufferedImage;

public interface CDrawerInterface {
    public void drawJImage(int x, int y, BufferedImage image);

    void fillWith(CColor CColor);

    void drawRect(int x, int y, int w, int h, CColor color, boolean fill);
}
