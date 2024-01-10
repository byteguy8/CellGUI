package cellgui.base;

import cellgui.CPosition;

import java.util.List;

public interface CellGroup extends Cell {
    int getChildX(Cell child);

    int getChildY(Cell child);

    void setChildPosition(Cell child, CPosition position);

    int childCount();

    List<Cell> getChildren();

    Cell getChild(int index);

    void addChild(Cell cell);
}
