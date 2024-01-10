package cellgui;

import cellgui.base.CBrush;
import cellgui.base.CEnvironment;
import cellgui.base.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class CWindows implements CWindowsInterface {
    private final CRect winRect;
    private final CRect canvasRect;

    private int action = -1;
    private Cell focusedCell = null;

    private volatile boolean running = false;
    private final List<Cell> cells = new ArrayList<>();

    private final CMouseInf mouseInf = new CMouseInf();

    private Canvas jCanvas = null;
    private JFrame jWindows = null;
    private Thread guiThread = null;
    private BufferStrategy bufferStrategy = null;

    public CWindows(int x, int y, int width, int height) {
        this.winRect = new CRect(x, y, width, height);
        this.canvasRect = new CRect();
    }

    private synchronized CRect getWinRect() {
        return new CRect(
                this.winRect.getX(),
                this.winRect.getY(),
                this.winRect.getWidth(),
                this.winRect.getHeight()
        );
    }

    private synchronized CRect getCanvasRect() {
        return new CRect(
                this.canvasRect.getX(),
                this.canvasRect.getY(),
                this.canvasRect.getWidth(),
                this.canvasRect.getHeight()
        );
    }

    private synchronized void updateWinRect(CUpdater<CRect> updater) {
        updater.update(this.winRect);
    }

    private synchronized void updateCanvasRect(CUpdater<CRect> updater) {
        updater.update(this.canvasRect);
    }

    private void initWindows() {
        try {
            EventQueue.invokeAndWait(() -> {
                final CRect winRect = getWinRect();

                jWindows = new JFrame();

                jWindows.setLocation(winRect.getX(), winRect.getY());
                jWindows.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                jWindows.setMinimumSize(new Dimension(winRect.getWidth(), winRect.getHeight()));

                jWindows.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent windowEvent) {
                        super.windowClosed(windowEvent);
                        running = false;
                    }
                });

                jCanvas = new Canvas();
                jCanvas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        super.mousePressed(mouseEvent);

                        mouseInf.keyPressed(mouseEvent);
                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        super.mouseReleased(mouseEvent);

                        mouseInf.keyReleased(mouseEvent);
                    }
                });

                jWindows.add(jCanvas);
                jWindows.pack();

                jCanvas.createBufferStrategy(2);
                bufferStrategy = jCanvas.getBufferStrategy();

                jWindows.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void moveCellsDown(int index, List<Cell> cells) {
        if (cells.size() < 2) {
            return;
        }

        final Cell upperCell = cells.get(index);

        for (int i = index; i < cells.size(); i++) {
            if (i + 1 < cells.size()) {
                Cell c = cells.get(i + 1);
                cells.set(i, c);
            }
        }

        cells.set(cells.size() - 1, upperCell);
    }

    private int getUpperZCell(List<Cell> cells, CMouseInf mouseInf) {
        int z = -1;

        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            CVector mPosition = mouseInf.getPosition();

            if (c.isInside(mPosition.getX(), mPosition.getY())) {
                if (i > z) {
                    z = i;
                }
            }
        }

        return z;
    }

    private Cell determinateFocusedCell(CMouseInf mouseInf) {
        if (mouseInf.isLeftClicked()) {
            action = -1;
        }

        final int upperCellIndex = getUpperZCell(cells, mouseInf);

        if (upperCellIndex == -1) {
            if (mouseInf.isLeftClicked()) {
                action = -1;
                focusedCell = null;
            }
        } else {
            if (mouseInf.isLeftPressed()) {
                if (action != -1) {
                    return focusedCell;
                }

                focusedCell = cells.get(upperCellIndex);

                CVector position = mouseInf.getPosition();

                if (focusedCell.isInsideHandler(position.getX(), position.getY())) {
                    action = 0;
                } else if (focusedCell.isInside(position.getX(), position.getY())) {
                    action = 1;
                }

                if (cells.size() > 1) {
                    moveCellsDown(upperCellIndex, cells);
                }
            }
        }

        return focusedCell;
    }

    private void mouseRecognition() {
        try {
            EventQueue.invokeAndWait(() -> {
                Point mPosition = jCanvas.getMousePosition();

                if (mPosition != null) {
                    mouseInf.setPosition(new CVector(mPosition.x, mPosition.y));
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.err.printf("Failed to get mouse position: %s\n", e.getMessage());
        }
    }

    private void surfaceRecognition() {
        // Windows and canvas position and size
        try {
            final CRect winRect = getWinRect();
            final CRect canvasRect = getCanvasRect();

            EventQueue.invokeAndWait(() -> {
                final int winX = jWindows.getX();
                final int winY = jWindows.getY();
                final int winWidth = jWindows.getWidth();
                final int winHeight = jWindows.getHeight();
                final Container container = jWindows.getContentPane();

                if (winX != winRect.getX() || winY != winRect.getY()) {
                    winRect.setX(winX);
                    winRect.setY(winY);

                    canvasRect.setX(container.getX());
                    canvasRect.setY(container.getY());
                }

                if (winWidth != winRect.getWidth() || winHeight != winRect.getHeight()) {
                    winRect.setWidth(winWidth);
                    winRect.setHeight(winHeight);
                }

                if (container.getWidth() != canvasRect.getWidth() || container.getHeight() != canvasRect.getHeight()) {
                    canvasRect.setWidth(container.getWidth());
                    canvasRect.setHeight(container.getHeight());
                }
            });

            updateWinRect(updater -> {
                updater.setX(winRect.getX());
                updater.setY(winRect.getY());
                updater.setWidth(winRect.getWidth());
                updater.setHeight(winRect.getHeight());
            });

            updateCanvasRect(updater -> {
                updater.setX(canvasRect.getX());
                updater.setY(canvasRect.getY());
                updater.setWidth(canvasRect.getWidth());
                updater.setHeight(canvasRect.getHeight());
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.err.printf("Failed to get windows size: %s\n", e.getMessage());
        }
    }

    private void mouseInput(CMouseInf mouseInf) {
        final Cell current = determinateFocusedCell(mouseInf);

        if (current == null) {
            return;
        }

        if (mouseInf.isLeftPressed()) {
            final CVector mMovement = mouseInf.getMovement();

            if (action == 0) {
                int w = current.getWidth() + mMovement.getX();
                int h = current.getHeight() + mMovement.getY();

                boolean validWidth = w >= current.getMinWidth() && (w <= current.getMaxWidth() || current.getMaxWidth() == -1);
                boolean validHeight = h >= current.getMinHeight() && (h <= current.getMaxHeight() || current.getMaxHeight() == -1);

                if (validWidth) {
                    current.setWidth(w);
                }

                if (validHeight) {
                    current.setHeight(h);
                }
            } else if (action == 1) {
                int x = current.getX() + mMovement.getX();
                int y = current.getY() + mMovement.getY();

                boolean isValidX = x >= 0 && x + current.getOverallWidth() <= jCanvas.getWidth();
                boolean isValidY = y >= 0 && y + current.getOverallHeight() <= jCanvas.getHeight();

                if (isValidX) {
                    current.setX(x);
                }

                if (isValidY) {
                    current.setY(y);
                }
            }
        }
    }

    private void setCellsEnvironment(List<Cell> cells, CEnvironment environment) {
        for (Cell cell : cells) {
            cell.setEnvironment(environment);
        }
    }

    private void onCellsInit(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.onInit();
        }
    }

    private void onCellsEvent(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.onEvent();
        }
    }

    private void onCellsLayout(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.onLayout();
        }
    }

    private void onCellsBeforeDraw(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.onBeforeDraw();
        }
    }

    private void onDrawCells(List<Cell> cells, Graphics2D graphics2D) {
        final CRect canvasRect = getCanvasRect();
        final CBrush brush = new CBrush(graphics2D);

        graphics2D.setColor(Color.BLACK);
        graphics2D.clearRect(
                canvasRect.getX(),
                canvasRect.getY(),
                canvasRect.getWidth(),
                canvasRect.getHeight()
        );
        graphics2D.fillRect(
                canvasRect.getX(),
                canvasRect.getY(),
                canvasRect.getWidth(),
                canvasRect.getHeight()
        );

        final Cell current = focusedCell;

        for (Cell cell : cells) {
            if (cell.equals(current)) {
                continue;
            }

            brush.setCell(cell);
            cell.onDraw(brush);
        }

        if (current != null) {
            brush.setCell(current);
            current.onDraw(brush);
        }

        bufferStrategy.show();
    }

    private void onEndCells(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.onEnd();
        }
    }

    private void setGraphicsHints(Graphics2D g2d) {
        g2d.setRenderingHint(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void executeCellThread() {
        long start;
        long end = 0;
        boolean isInit = true;

        while (running) {
            final Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();

            setGraphicsHints(g2d);

            try {
                final CEnvironment environment = new CEnvironment(
                        getWinRect(),
                        getCanvasRect(),
                        mouseInf,
                        g2d);

                start = end;

                mouseRecognition();
                surfaceRecognition();
                mouseInput(mouseInf);

                setCellsEnvironment(cells, environment);

                if (isInit) {
                    onCellsInit(cells);
                    isInit = false;
                }

                onCellsEvent(cells);
                onCellsLayout(cells);
                onCellsBeforeDraw(cells);
                onDrawCells(cells, g2d);
                onEndCells(cells);

                Thread.sleep(17);

                end = System.nanoTime();
                mouseInf.restart(start, end);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                g2d.dispose();
            }
        }
    }

    @Override
    public int x() {
        final CRect canvasRect = getCanvasRect();

        return canvasRect.getX();
    }

    @Override
    public int y() {
        final CRect canvasRect = getCanvasRect();

        return canvasRect.getY();
    }

    @Override
    public int width() {
        final CRect canvasRect = getCanvasRect();

        return canvasRect.getWidth();
    }

    @Override
    public int height() {
        final CRect canvasRect = getCanvasRect();

        return canvasRect.getHeight();
    }

    @Override
    public void addCell(Cell cell) {
        cells.add(cell);
        focusedCell = cells.get(cells.size() - 1);
    }

    @Override
    public void start() {
        initWindows();

        running = true;

        guiThread = new Thread(this::executeCellThread);

        guiThread.start();
    }
}
