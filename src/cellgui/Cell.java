package cellgui;

public interface Cell {
    int getX();

    int getContentX();

    void setX(int x);

    int getY();

    int getContentY();

    void setY(int y);

    int getWidth();

    int getContainerWidth();

    int getOverallWidth();

    void setWidth(int width);

    int getHeight();

    int getContainerHeight();

    int getOverallHeight();

    void setHeight(int height);

    int getMinWidth();

    void setMinWidth(int width);

    int getMinHeight();

    void setMinHeight(int height);

    int getMaxWidth();

    void setMaxWidth(int with);

    int getMaxHeight();

    void setMaxHeight(int height);

    int getTopPadding();

    void setTopPadding(int padding);

    int getBottomPadding();

    void setBottomPadding(int padding);

    int getStartPadding();

    void setStartPadding(int padding);

    int getEndPadding();

    void setEndPadding(int padding);

    int getTopMargin();

    void setTopMargin(int margin);

    int getBottomMargin();

    void setBottomMargin(int margin);

    int getStartMargin();

    void setStartMargin(int margin);

    int getEndMargin();

    void setEndMargin(int margin);

    CColor getColor();

    void setColor(CColor color);

    boolean canDrag();

    void setDrag(boolean drag);

    CSizeFlag getHorizontalSizeFlag();

    void setHorizontalSizeFlag(CSizeFlag flag);

    CSizeFlag getVerticalSizeFlag();

    void setVerticalSizeFlag(CSizeFlag flag);

    void setParent(Cell parent);

    Cell getParent();

    CEnvironment getEnvironment();

    void setEnvironment(CEnvironment environment);

    void setOnChangeListener(COnChangeListener listener);

    boolean isInside(int x, int y);

    boolean isInsideHandler(int x, int y);

    void onInit();

    void onProcess();

    void onDraw(CDrawer drawer);
}
