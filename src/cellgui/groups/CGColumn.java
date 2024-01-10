package cellgui.groups;

import cellgui.CSizeFlag;
import cellgui.CVector;
import cellgui.base.AbstractCellGroup;
import cellgui.base.Cell;

public class CGColumn extends AbstractCellGroup {
    private int calculatedWidth;
    private int calculatedHeight;

    public CGColumn(int x, int y) {
        super(x, y, 0, 0);
    }

    private CVector getChildrenSize() {
        int width = 0;
        int height = 0;

        for (Cell child : getChildren()) {
            height += child.getOverallHeight();

            if (child.getOverallWidth() > width) {
                width = child.getOverallWidth();
            }
        }

        return new CVector(width, height);
    }

    private void updateCellSize() {
        if (getHorizontalSizeFlag() == CSizeFlag.NONE || (getHorizontalSizeFlag() == CSizeFlag.FIT && getParent() == null)) {
            setMinWidth(calculatedWidth);
            setWidth(calculatedWidth);
        }

        if (getVerticalSizeFlag() == CSizeFlag.NONE || (getVerticalSizeFlag() == CSizeFlag.FIT && getParent() == null)) {
            setMinHeight(calculatedHeight);
            setHeight(calculatedHeight);
        }
    }

    private void updateChildren() {
        final CVector childrenSize = getChildrenSize();

        if (childrenSize.getX() > getWidth()) {
            setWidth(childrenSize.getX());
        }

        if (childrenSize.getY() > getHeight()) {
            setHeight(childrenSize.getY());
        }

        final int fromX;
        final int fromY;

        if (getWidth() == 0) {
            fromX = 0;
        } else {
            fromX = (getWidth() - childrenSize.getX()) / 2;
        }

        if (getHeight() == 0) {
            fromY = 0;
        } else {
            fromY = (getHeight() - childrenSize.getY()) / 2;
        }

        int width = 0;
        int height = 0;

        int localChildX = fromX;
        int localChildY = fromY;

        int globalChildX = fromX + getContentX();
        int globalChildY = fromY + getContentY();

        for (Cell child : getChildren()) {
            child.setX(globalChildX);
            child.setY(globalChildY);

            setChildPosition(child, new CVector(localChildX, localChildY));

            globalChildY += child.getOverallHeight();
            localChildY += child.getOverallHeight();

            height += child.getOverallHeight();

            if (width < child.getOverallWidth()) {
                width = child.getOverallWidth();
            }
        }

        calculatedWidth = width;
        calculatedHeight = height;
    }

    @Override
    public void onInit() {
        super.onInit();

        updateChildren();
        updateCellSize();
    }

    @Override
    public void onLayout() {
        super.onLayout();

        updateChildren();
    }
}
