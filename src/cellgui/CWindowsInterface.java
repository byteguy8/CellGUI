package cellgui;

import cellgui.base.Cell;

public interface CWindowsInterface {
    int x();

    int y();

    int width();

    int height();

    void addCell(Cell canvas);

    void start();
}
