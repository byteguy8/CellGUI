package cellgui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCellGroup extends AbstractCell implements CellGroup {
    private final List<Cell> cells = new ArrayList<>();

    public AbstractCellGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void onInit() {
        super.onInit();

        for (Cell cell : cells) {
            cell.setEnvironment(getEnvironment());
            cell.onInit();
        }
    }

    @Override
    public void onProcess() {
        super.onProcess();

        for (Cell cell : cells) {
            cell.setEnvironment(getEnvironment());
            cell.onProcess();
        }
    }

    @Override
    public void onDraw(CDrawer drawer) {
        super.onDraw(drawer);

        if (getWidth() > 0 && getHeight() > 0) {
            BufferedImage canvas = new BufferedImage(
                    getWidth(),
                    getHeight(),
                    BufferedImage.TRANSLUCENT
            );

            Graphics2D jDrawer = (Graphics2D) canvas.getGraphics();

            for (Cell cell : cells) {
                CDrawer d = new CDrawer(jDrawer);

                d.setCanvas(cell);
                cell.onDraw(d);
            }

            drawer.drawJImage(getContentX(), getContentY(), canvas);

            jDrawer.dispose();
        }
    }

    @Override
    public int childCount() {
        return cells.size();
    }

    @Override
    public List<Cell> getChildren() {
        return new ArrayList<>(cells);
    }

    @Override
    public Cell getChild(int index) {
        return cells.get(index);
    }

    @Override
    public void addChild(Cell cell) {
        cell.setParent(this);
        cells.add(cell);
    }
}
