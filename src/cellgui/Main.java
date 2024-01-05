package cellgui;

public class Main {
    private static CGroupRow createRow() {
        CGroupRow row = new CGroupRow(0, 0);

        Cell c0 = new AbstractCell(0, 0, 89, 41) {
        };
        c0.setColor(new CColor(150, 200, 75));
        c0.setStartMargin(10);
        c0.setEndMargin(10);
        c0.setTopMargin(10);
        c0.setBottomMargin(10);

        Cell c1 = new AbstractCell(0, 0, 89, 41) {
        };
        c1.setColor(new CColor(150, 200, 75));
        c1.setStartMargin(10);
        c1.setEndMargin(10);
        c1.setTopMargin(10);
        c1.setBottomMargin(10);

        row.addChild(c0);
        row.addChild(c1);

        return row;
    }

    public static void main(String[] args) {
        CWindows windows = new CWindows(0, 0, 500, 500);

        CGroupColumn column = new CGroupColumn(0, 0);

        column.addChild(createRow());
        column.addChild(createRow());
        column.addChild(createRow());
        column.addChild(createRow());
        column.addChild(createRow());

        windows.addCell(column);

        windows.start();
    }
}
