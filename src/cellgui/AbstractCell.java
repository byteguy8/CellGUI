package cellgui;

public abstract class AbstractCell implements Cell {
    private int x;
    private int y;
    private int width;
    private int height;
    private int minWidth;
    private int minHeight;
    private int maxWidth;
    private int maxHeight;

    private int topPadding;
    private int bottomPadding;
    private int startPadding;
    private int endPadding;

    private int topMargin;
    private int bottomMargin;
    private int startMargin;
    private int endMargin;

    private boolean drag;
    private CColor color;

    private CSizeFlag horizontalSizeFlag;
    private CSizeFlag verticalSizeFlag;

    private Cell parent;
    private COnChangeListener onChangeListener;

    private CEnvironment environment;

    public AbstractCell(int width, int height) {
        this(0, 0, width, height);
    }

    public AbstractCell(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.minWidth = 0;
        this.minHeight = 0;

        this.maxWidth = -1;
        this.maxHeight = -1;

        this.topPadding = 0;
        this.bottomPadding = 0;
        this.startPadding = 0;
        this.endPadding = 0;

        this.topMargin = 0;
        this.bottomMargin = 0;
        this.startMargin = 0;
        this.endMargin = 0;

        this.drag = true;
        this.color = new CColor(255, 255, 255);

        this.horizontalSizeFlag = CSizeFlag.NONE;
        this.verticalSizeFlag = CSizeFlag.NONE;

        this.parent = null;
        this.onChangeListener = null;

        this.environment = null;
    }

    private void notifyOnchange() {
        if (onChangeListener != null) {
            onChangeListener.onChange();
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getContentX() {
        return x + startMargin + startPadding;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getContentY() {
        return y + topMargin + topPadding;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getContainerWidth() {
        return width + startPadding + endPadding;
    }

    @Override
    public int getOverallWidth() {
        return width + startPadding + endPadding + startMargin + endMargin;
    }

    @Override
    public void setWidth(int width) {
        if (width != this.width) {
            notifyOnchange();
        }

        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getContainerHeight() {
        return height + topPadding + bottomPadding;
    }

    @Override
    public int getOverallHeight() {
        return height + topPadding + bottomPadding + topMargin + bottomMargin;
    }

    @Override
    public void setHeight(int height) {
        if (height != this.height) {
            notifyOnchange();
        }

        this.height = height;
    }

    @Override
    public int getMinWidth() {
        return minWidth;
    }

    @Override
    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    @Override
    public int getMinHeight() {
        return minHeight;
    }

    @Override
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public int getTopPadding() {
        return topPadding;
    }

    @Override
    public void setTopPadding(int padding) {
        this.topPadding = padding;
    }

    @Override
    public int getBottomPadding() {
        return bottomPadding;
    }

    @Override
    public void setBottomPadding(int padding) {
        this.bottomPadding = padding;
    }

    @Override
    public int getStartPadding() {
        return startPadding;
    }

    @Override
    public void setStartPadding(int padding) {
        this.startPadding = padding;
    }

    @Override
    public int getEndPadding() {
        return endPadding;
    }

    @Override
    public void setEndPadding(int padding) {
        this.endPadding = padding;
    }

    @Override
    public int getTopMargin() {
        return topMargin;
    }

    @Override
    public void setTopMargin(int margin) {
        this.topMargin = margin;
    }

    @Override
    public int getBottomMargin() {
        return bottomMargin;
    }

    @Override
    public void setBottomMargin(int margin) {
        this.bottomMargin = margin;
    }

    @Override
    public int getStartMargin() {
        return startMargin;
    }

    @Override
    public void setStartMargin(int margin) {
        this.startMargin = margin;
    }

    @Override
    public int getEndMargin() {
        return endMargin;
    }

    @Override
    public void setEndMargin(int margin) {
        this.endMargin = margin;
    }

    @Override
    public CColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(CColor color) {
        if (color == null) {
            throw new IllegalArgumentException("color can't be null");
        }

        this.color = color;
    }

    @Override
    public boolean canDrag() {
        return this.drag;
    }

    @Override
    public void setDrag(boolean drag) {
        this.drag = drag;
    }

    @Override
    public CSizeFlag getHorizontalSizeFlag() {
        return horizontalSizeFlag;
    }

    @Override
    public void setHorizontalSizeFlag(CSizeFlag flag) {
        this.horizontalSizeFlag = flag;
    }

    @Override
    public CSizeFlag getVerticalSizeFlag() {
        return verticalSizeFlag;
    }

    @Override
    public void setVerticalSizeFlag(CSizeFlag flag) {
        this.verticalSizeFlag = flag;
    }

    @Override
    public void setParent(Cell parent) {
        this.parent = parent;
    }

    @Override
    public Cell getParent() {
        return this.parent;
    }

    @Override
    public CEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(CEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void setOnChangeListener(COnChangeListener listener) {
        this.onChangeListener = listener;
    }

    @Override
    public boolean isInside(int x, int y) {
        boolean insideX = x >= getX() + getStartMargin() && x <= getX() + getStartMargin() + getContainerWidth();
        boolean insideY = y >= getY() + getTopMargin() && y <= getY() + getTopMargin() + getContainerHeight();

        return insideX && insideY;
    }

    @Override
    public boolean isInsideHandler(int x, int y) {
        int hw = 10;
        int hh = 10;
        int hx = (getX() + getStartMargin() + getContainerWidth()) - hw;
        int hy = (getY() + getTopMargin() + getContainerHeight()) - hh;

        boolean insideX = x >= hx && x <= hx + hw;
        boolean insideY = y >= hy && y <= hy + hh;

        return insideX && insideY;
    }

    @Override
    public void onInit() {
        if (environment == null) {
            throw new IllegalStateException("Unexpected environment value");
        }

        if (getHorizontalSizeFlag() == CSizeFlag.EXPAND) {
            if (parent == null) {
                int width = getEnvironment().width - getStartPadding() - getEndPadding() - getStartMargin() - getEndMargin();

                setMinWidth(width);
                setWidth(width);
                setMaxWidth(width);
            }
        }

        if (getVerticalSizeFlag() == CSizeFlag.EXPAND) {
            if (parent == null) {
                int padding = getStartPadding() + getEndPadding();
                int margin = getStartMargin() + getEndMargin();
                int height = getEnvironment().height - padding - margin;

                setMinHeight(height);
                setHeight(height);
                setMaxHeight(height);
            }
        }
    }

    @Override
    public void onProcess() {
        if (environment == null) {
            throw new IllegalStateException("Unexpected environment value");
        }
    }

    @Override
    public void onDraw(CDrawer drawer) {
        if (environment == null) {
            throw new IllegalStateException("Unexpected environment value");
        }

        drawer.fillWith(color);

        CMouseInf mouseInf = environment.mouseInf;

        if (isInsideHandler(mouseInf.x, mouseInf.y)) {
            int hw = 10;
            int hh = 10;
            int hx = getContainerWidth() - hw;
            int hy = getContainerHeight() - hh;

            drawer.drawRect(hx, hy, hw, hh, new CColor(255, 0, 0), true);
        }
    }
}
