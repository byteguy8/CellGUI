package cellgui;

import cellgui.base.AbstractCell;
import cellgui.base.CBrush;
import cellgui.base.Cell;
import cellgui.controls.CButton;
import cellgui.controls.CLabel;
import cellgui.controls.listeners.MouseListener;
import cellgui.controls.listeners.events.MouseEvent;
import cellgui.groups.CGColumn;
import cellgui.groups.CGRow;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static CGRow createRow() {
        CGRow row = new CGRow(0, 0);

        Cell c0 = new AbstractCell(0, 0, 89, 41) {
        };
        c0.setBackgroundColor(new CColor(150, 200, 75));
        c0.setStartMargin(10);
        c0.setEndMargin(10);
        c0.setTopMargin(10);
        c0.setBottomMargin(10);

        Cell c1 = new AbstractCell(0, 0, 89, 41) {
        };
        c1.setBackgroundColor(new CColor(150, 200, 75));
        c1.setStartMargin(10);
        c1.setEndMargin(10);
        c1.setTopMargin(10);
        c1.setBottomMargin(10);

        row.addChild(c0);
        row.addChild(c1);

        return row;
    }

    private static Cell randomCell() {
        final Random random = new Random(System.nanoTime());

        Cell c0 = new AbstractCell(
                random.nextInt(500),
                random.nextInt(500),
                89,
                41
        ) {
        };

        c0.setBackgroundColor(CColor.randomColor());

        return c0;
    }

    public static void main(String[] args) {
        AtomicInteger count = new AtomicInteger(0);
        CWindows windows = new CWindows(0, 0, 500, 500);

        CLabel l0 = new CLabel("count: 0");

        CButton incrementBtn = new CButton("Increment");
        incrementBtn.setBackgroundColor(new CColor(66, 135, 245));
        incrementBtn.setActiveColor(new CColor(117, 165, 240));
        incrementBtn.setPressedColor(new CColor(48, 104, 191));
        incrementBtn.setTextColor(new CColor(255, 255, 255));

        incrementBtn.setStartMargin(10);

        incrementBtn.addMouseListener(new MouseListener() {
            @Override
            public void onPressed(MouseEvent event) {

            }

            @Override
            public void onReleased(MouseEvent event) {
                if (event.button == MouseEvent.Button.LEFT) {
                    l0.setText(String.format("count: %d", count.incrementAndGet()));
                }
            }
        });

        CButton decrementBtn = new CButton("Decrement");
        decrementBtn.setBackgroundColor(new CColor(66, 135, 245));
        decrementBtn.setActiveColor(new CColor(117, 165, 240));
        decrementBtn.setPressedColor(new CColor(48, 104, 191));
        decrementBtn.setTextColor(new CColor(255, 255, 255));

        decrementBtn.setStartMargin(10);

        decrementBtn.addMouseListener(new MouseListener() {
            @Override
            public void onPressed(MouseEvent event) {

            }

            @Override
            public void onReleased(MouseEvent event) {
                l0.setText(String.format("count: %d", count.decrementAndGet()));
            }
        });

        CGRow row = new CGRow(25, 25);

        row.addChild(l0);
        row.addChild(incrementBtn);
        row.addChild(decrementBtn);

        row.setStartPadding(10);
        row.setEndPadding(10);
        row.setTopPadding(10);
        row.setBottomPadding(10);
        row.setBackgroundColor(CColor.randomColor());

        CButton b = new CButton("Press me!");
        b.setBackgroundColor(CColor.randomColor());
        b.setTextColor(CColor.randomColor());
        b.setActiveColor(CColor.randomColor());
        b.setPressedColor(CColor.randomColor());

        windows.addCell(row);

        windows.start();
    }
}
