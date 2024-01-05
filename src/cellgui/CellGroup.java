package cellgui;

import java.util.List;

public interface CellGroup extends Cell {
    int childCount();

    List<Cell> getChildren();

    Cell getChild(int index);

    void addChild(Cell cell);
}
