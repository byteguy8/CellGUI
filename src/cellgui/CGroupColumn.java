package cellgui;

public class CGroupColumn extends AbstractCellGroup {
    private int childrenChangesCount = 0;

    public CGroupColumn(int x, int y) {
        super(x, y, 0, 0);
    }

    private void updateCell() {
        int width = 0;
        int height = 0;

        int childY = 0;

        for (Cell child : getChildren()) {
            child.setX(0);
            child.setY(childY);

            childY += child.getOverallHeight();
            height += child.getOverallHeight();

            if (width < child.getOverallWidth()) {
                width = child.getOverallWidth();
            }
        }

        if (getHorizontalSizeFlag() == CSizeFlag.NONE || (getHorizontalSizeFlag() == CSizeFlag.FIT && getParent() == null)) {
            setMinWidth(width);
            setWidth(width);
        }

        if (getVerticalSizeFlag() == CSizeFlag.NONE || (getVerticalSizeFlag() == CSizeFlag.FIT && getParent() == null)) {
            setMinHeight(height);
            setHeight(height);
        }
    }

    @Override
    public void onInit() {
        super.onInit();

        updateCell();
    }

    @Override
    public void onProcess() {
        super.onProcess();

        if (childrenChangesCount > 0) {
            updateCell();
            childrenChangesCount = 0;
        }
    }

    @Override
    public void addChild(Cell cell) {
        super.addChild(cell);

        cell.setOnChangeListener(() -> {
            childrenChangesCount++;
        });
    }
}
