package cellgui.base;

import cellgui.CColor;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class CBrush implements CDrawer {
    private final int mx;
    private final int my;
    private boolean transport;
    private final Graphics2D graphics2D;

    private Cell cell = null;
    private Rectangle contentShape = null;
    private Rectangle containerShape = null;

    public CBrush(Graphics2D graphics2D) {
        this(0, 0, graphics2D);
        this.transport = false;
    }

    public CBrush(int mx, int my, Graphics2D graphics2D) {
        this.mx = mx;
        this.my = my;
        this.graphics2D = graphics2D;

        this.transport = true;
    }

    private Color toJavaColor(CColor color) {
        return new Color(color.r, color.g, color.b, color.a);
    }

    public void setCell(Cell cell) {
        int x;
        int y;

        if (transport) {
            x = mx;
            y = my;
        } else {
            x = cell.getX();
            y = cell.getY();
        }

        this.cell = cell;

        containerShape = new Rectangle(
                x + cell.getStartMargin(),
                y + cell.getTopMargin(),
                cell.getContainerWidth(),
                cell.getContainerHeight()
        );

        contentShape = new Rectangle(
                x + cell.getStartMargin() + cell.getStartPadding(),
                y + cell.getTopMargin() + cell.getTopPadding(),
                cell.getWidth(),
                cell.getHeight()
        );

        graphics2D.setClip(contentShape);
    }

    @Override
    public void drawJImage(int x, int y, BufferedImage image) {
        graphics2D.drawImage(
                image,
                contentShape.getBounds().x + x,
                contentShape.getBounds().y + y,
                null
        );
    }

    @Override
    public void drawHandler() {
        final Color c = graphics2D.getColor();

        try {
            graphics2D.setClip(containerShape);

            int hw = 10;
            int hh = 10;
            int hx = cell.getX() + cell.getStartMargin() + cell.getContainerWidth() - hw;
            int hy = cell.getY() + cell.getTopMargin() + cell.getContainerHeight() - hh;

            graphics2D.setColor(Color.RED);
            graphics2D.fillRect(
                    hx,
                    hy,
                    hw,
                    hh
            );
        } finally {
            graphics2D.setColor(c);
            graphics2D.setClip(contentShape);
        }
    }

    @Override
    public void fillWith(CColor color) {
        final Color c = graphics2D.getColor();

        try {
            graphics2D.setColor(toJavaColor(color));
            graphics2D.setClip(containerShape);
            graphics2D.fill(containerShape);
        } finally {
            graphics2D.setColor(c);
            graphics2D.setClip(contentShape);
        }
    }

    @Override
    public void drawString(String str, int x, int y, CColor color) {
        final Color previousColor = graphics2D.getColor();
        final Color actualColor = toJavaColor(color);

        final FontMetrics metrics = graphics2D.getFontMetrics();

        try {
            graphics2D.setColor(actualColor);

            graphics2D.drawString(
                    str,
                    contentShape.x + x,
                    contentShape.y + y + metrics.getMaxAscent()
            );
        } finally {
            graphics2D.setColor(previousColor);
        }
    }

    @Override
    public void drawRect(int x, int y, int w, int h, CColor color, boolean fill) {
        final Color previousColor = graphics2D.getColor();

        try {
            Color newColor = toJavaColor(color);
            Rectangle rect = new Rectangle(
                    contentShape.x + x,
                    contentShape.y + y,
                    w,
                    h
            );

            graphics2D.setColor(newColor);

            if (fill) {
                graphics2D.fill(rect);
            } else {
                graphics2D.draw(rect);
            }
        } finally {
            graphics2D.setColor(previousColor);
        }
    }

    @Override
    public void drawCell(int x, int y, Cell cell) {
        cell.setEnvironment(this.cell.getEnvironment());

        if (!cell.isInitialized()) {
            cell.onInit();
        }

        final BufferedImage canvas = new BufferedImage(
                contentShape.width,
                contentShape.height,
                BufferedImage.TRANSLUCENT);

        final Graphics2D g2d = (Graphics2D) canvas.getGraphics();
        final CBrush brush = new CBrush(g2d);

        brush.setCell(cell);

        cell.setX(x);
        cell.setY(y);

        cell.onEvent();
        cell.onLayout();
        cell.onBeforeDraw();
        cell.onDraw(brush);
        cell.onEnd();

        g2d.dispose();

        drawJImage(0, 0, canvas);
    }
}
