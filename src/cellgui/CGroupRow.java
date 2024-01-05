package cellgui;

public class CGroupRow extends AbstractCellGroup {
    private int childrenChangesCount = 0;

    public CGroupRow(int x, int y) {
        super(x, y, 0, 0);
    }

    private void updateCell() {
        int width = 0;
        int height = 0;

        int childX = 0;

        for (Cell child : getChildren()) {
            child.setX(childX);
            child.setY(0);

            childX += child.getOverallWidth();
            width += child.getOverallWidth();

            if (height < child.getOverallHeight()) {
                height = child.getOverallHeight();
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
