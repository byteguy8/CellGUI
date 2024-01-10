package cellgui.base;

import cellgui.CVector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public abstract class AbstractCellGroup extends AbstractCell implements CellGroup {
    private final List<Cell> children = new ArrayList<>();
    private final Map<Cell, CVector> localChildrenPositions = new HashMap<>();

    public AbstractCellGroup(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    private void drawChild(Graphics2D g2d, Cell cell) {
        int x = getChildX(cell);
        int y = getChildY(cell);

        // All CellGroups must update the local position
        // of its children in order to achieve a correct drawing of them.
        // If x or y local position of any child is -1, it means that
        // the CellGroup didn't update the child local position.
        if (x == -1) {
            throw new IllegalStateException("Unexpected child x local position value: -1");
        } else if (y == -1) {
            throw new IllegalStateException("Unexpected child y local position value: -1");
        }

        CBrush brush = new CBrush(x, y, g2d);

        brush.setCell(cell);
        cell.onDraw(brush);
    }

    @Override
    public void setEnvironment(CEnvironment environment) {
        super.setEnvironment(environment);

        for (Cell child : children) {
            child.setEnvironment(environment);
        }
    }

    @Override
    public void onInit() {
        super.onInit();

        for (Cell cell : children) {
            cell.onInit();
        }
    }

    @Override
    public void onEvent() {
        super.onEvent();

        for (Cell cell : children) {
            cell.onEvent();
        }
    }

    @Override
    public void onLayout() {
        super.onLayout();

        for (Cell cell : children) {
            cell.onLayout();
        }
    }

    @Override
    public void onBeforeDraw() {
        super.onBeforeDraw();

        for (Cell cell : children) {
            cell.onBeforeDraw();
        }
    }

    @Override
    public void onDraw(CBrush drawer) {
        super.onDraw(drawer);

        if (getWidth() > 0 && getHeight() > 0) {
            BufferedImage canvas = new BufferedImage(
                    getWidth(),
                    getHeight(),
                    BufferedImage.TRANSLUCENT
            );

            Graphics2D g2d = (Graphics2D) canvas.getGraphics();

            for (Cell cell : children) {
                drawChild(g2d, cell);
            }

            drawer.drawJImage(0, 0, canvas);
            g2d.dispose();
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();

        for (Cell cell : children) {
            cell.onEnd();
        }
    }

    @Override
    public int getChildX(Cell child) {
        if (localChildrenPositions.containsKey(child)) {
            return localChildrenPositions.get(child).getX();
        }

        return -1;
    }

    @Override
    public int getChildY(Cell child) {
        if (localChildrenPositions.containsKey(child)) {
            return localChildrenPositions.get(child).getY();
        }

        return -1;
    }

    @Override
    public void setChildPosition(Cell child, CVector position) {
        if (position == null) {
            throw new IllegalArgumentException("position is null");
        }

        if (position.getX() < 0) {
            throw new IllegalArgumentException("Illegal child x local position: local positions should be grater or equals to 0");
        } else if (position.getY() < 0) {
            throw new IllegalArgumentException("Illegal child y local position: local positions should be grater or equals to 0");
        }

        localChildrenPositions.put(child, position);
    }

    @Override
    public int childCount() {
        return children.size();
    }

    @Override
    public List<Cell> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public Cell getChild(int index) {
        return children.get(index);
    }

    @Override
    public void addChild(Cell cell) {
        cell.setParent(this);

        children.add(cell);
    }
}
