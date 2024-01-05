package cellgui;

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
    private int x;
    private int y;
    private volatile int width;
    private volatile int height;

    private int action = -1;
    private Cell last = null;
    private Cell focused = null;

    private volatile boolean running = false;
    private List<Cell> cells = new ArrayList<>();

    private volatile CMouseInf mouseInformation = new CMouseInf();

    private Canvas jCanvas = null;
    private JFrame jWindows = null;
    private Thread guiThread = null;
    private BufferStrategy bufferStrategy = null;

    public CWindows(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private void initWindows() {
        try {
            EventQueue.invokeAndWait(() -> {
                jWindows = new JFrame();

                jWindows.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                jWindows.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent windowEvent) {
                        super.windowClosed(windowEvent);
                        running = false;
                    }
                });

                jCanvas = new Canvas();
                jCanvas.setSize(width, height);
                jCanvas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        super.mousePressed(mouseEvent);

                        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                            mouseInformation.left = true;
                        } else if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                            mouseInformation.right = true;
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        super.mouseReleased(mouseEvent);

                        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                            mouseInformation.left = false;
                        } else if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                            mouseInformation.right = false;
                        }
                    }
                });

                jWindows.add(jCanvas);
                jWindows.pack();

                jCanvas.createBufferStrategy(2);
                bufferStrategy = jCanvas.getBufferStrategy();

                jWindows.setLocation(x, y);
                jWindows.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void recognition() {
        if (jWindows.getWidth() != width || jWindows.getHeight() != height) {
            width = jWindows.getWidth();
            height = jWindows.getHeight();
            jCanvas.setMinimumSize(new Dimension(width, height));
        }

        Point mousePosition = jCanvas.getMousePosition();

        if (mousePosition != null) {
            mouseInformation.x = mousePosition.x;
            mouseInformation.y = mousePosition.y;
        }
    }

    private void processMouseInput(CMouseInf mi) {
        // Allow dragging
        final Cell current = last;

        if (mi.left && current != null && current.canDrag()) {
            if (action == 0) {
                int x = current.getX() + mi.xMov;
                int y = current.getY() + mi.yMov;

                boolean isValidX = x >= 0 && x + current.getOverallWidth() <= jCanvas.getWidth();
                boolean isValidY = y >= 0 && y + current.getOverallHeight() <= jCanvas.getHeight();

                if (isValidX) {
                    current.setX(x);
                }

                if (isValidY) {
                    current.setY(y);
                }
            } else if (action == 1) {
                int w = current.getWidth() + mi.xMov;
                int h = current.getHeight() + mi.yMov;

                boolean validWidth = w >= current.getMinWidth() && (w <= current.getMaxWidth() || current.getMaxWidth() == -1);
                boolean validHeight = h >= current.getMinHeight() && (h <= current.getMaxHeight() || current.getMaxHeight() == -1);

                if (validWidth) {
                    current.setWidth(w);
                }

                if (validHeight) {
                    current.setHeight(h);
                }
            }

            return;
        }

        final Cell f = this.focused;

        boolean quitFocus = true;
        boolean processFocus = true;
        boolean processAction = true;

        for (int i = cells.size() - 1; i >= 0; i--) {
            Cell c = cells.get(i);

            if (mi.left) {
                if (processFocus) {
                    if (f == null) {
                        if (c.isInside(mi.x, mi.y)) {
                            focused = c;
                            quitFocus = false;
                            processFocus = false;
                        }
                    } else {
                        if (f.isInside(mi.x, mi.y)) {
                            focused = f;
                            quitFocus = false;
                            processFocus = false;
                        }
                    }

                    if (i - 1 < 0 && quitFocus) {
                        focused = null;
                    }
                }

                if (processAction) {
                    if (f == null) {
                        if (c.isInsideHandler(mi.x, mi.y)) {
                            last = c;
                            action = 1;
                            processAction = false;

                            continue;
                        }

                        if (c.isInside(mi.x, mi.y)) {
                            last = c;
                            action = 0;
                            processAction = false;
                        }
                    } else {
                        if (f.isInsideHandler(mi.x, mi.y)) {
                            last = f;
                            action = 1;
                            processAction = false;
                        } else if (f.isInside(mi.x, mi.y)) {
                            last = f;
                            action = 0;
                            processAction = false;
                        }
                    }
                }
            }
        }

        if (processAction) {
            action = -1;
            last = null;
        }
    }

    private void processCells() {
        for (Cell cell : cells) {
            CEnvironment environment = new CEnvironment(width, height, mouseInformation);

            cell.setEnvironment(environment);
            cell.onProcess();
        }
    }

    private void drawCells() {
        Graphics2D graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        CDrawer d = new CDrawer(graphics2D);

        graphics2D.setColor(Color.BLACK);
        graphics2D.clearRect(x, y, width, height);
        graphics2D.fillRect(x, y, width, height);

        final Cell current = focused;

        for (Cell cell : cells) {
            if (cell.equals(current)) {
                continue;
            }

            d.setCanvas(cell);
            cell.onDraw(d);
        }

        if (current != null) {
            d.setCanvas(current);
            current.onDraw(d);
        }

        bufferStrategy.show();
        graphics2D.dispose();
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public void addCell(Cell cell) {
        cells.add(cell);
        focused = cells.get(cells.size() - 1);
    }

    private int put(int x, int y, long[][] b, int current) {
        if (current + 1 >= b.length) {
            current = 0;
        } else {
            current++;
        }

        b[current][0] = x;
        b[current][1] = y;
        b[current][2] = System.currentTimeMillis();

        return current;
    }

    @Override
    public void start() {
        initWindows();

        running = true;

        guiThread = new Thread(() -> {
            int current = 0;
            long[][] buff = new long[2][3];

            CEnvironment environment = new CEnvironment(width, height, mouseInformation);

            for (Cell cell : cells) {
                cell.setEnvironment(environment);
                cell.onInit();
            }

            while (running) {
                recognition();

                final CMouseInf mi = mouseInformation;
                current = put(mi.x, mi.y, buff, current);

                int xMov;
                int yMov;

                if (buff[0][2] > buff[1][2]) {
                    xMov = (int) (buff[0][0] - buff[1][0]);
                    yMov = (int) (buff[0][1] - buff[1][1]);
                } else {
                    xMov = (int) (buff[1][0] - buff[0][0]);
                    yMov = (int) (buff[1][1] - buff[0][1]);
                }

                mouseInformation.xMov = xMov;
                mouseInformation.yMov = yMov;

                processMouseInput(mi);
                processCells();
                drawCells();

                try {
                    Thread.sleep(17);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        guiThread.start();
    }
}
