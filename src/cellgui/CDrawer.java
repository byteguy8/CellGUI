package cellgui;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class CDrawer implements CDrawerInterface {
    private final Graphics2D jDrawer;
    private Rectangle containerShape = null;

    public CDrawer(Graphics2D jDrawer) {
        this.jDrawer = jDrawer;
    }

    public void setCanvas(Cell canvas) {
        containerShape = new Rectangle(
                canvas.getX() + canvas.getStartMargin(),
                canvas.getY() + canvas.getTopMargin(),
                canvas.getContainerWidth(),
                canvas.getContainerHeight()
        );

        jDrawer.setClip(containerShape);
    }

    @Override
    public void drawJImage(int x, int y, BufferedImage image) {
        jDrawer.drawImage(image, x, y, null);
    }

    @Override
    public void fillWith(CColor color) {
        final Color c = jDrawer.getColor();

        try {
            jDrawer.setColor(new Color(color.r, color.g, color.b));
            jDrawer.setClip(containerShape);
            jDrawer.fill(containerShape);
        } finally {
            jDrawer.setColor(c);
        }
    }

    @Override
    public void drawRect(int x, int y, int w, int h, CColor color, boolean fill) {
        final Color previousColor = jDrawer.getColor();

        try {
            Color newColor = new Color(color.r, color.g, color.b);
            Rectangle rect = new Rectangle(
                    containerShape.x + x,
                    containerShape.y + y,
                    w,
                    h
            );

            jDrawer.setColor(newColor);

            if (fill) {
                jDrawer.fill(rect);
            } else {
                jDrawer.draw(rect);
            }
        } finally {
            jDrawer.setColor(previousColor);
        }
    }
}
