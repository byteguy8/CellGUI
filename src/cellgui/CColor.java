package cellgui;

import java.awt.*;
import java.util.Random;

public class CColor {
    public final int r;
    public final int g;
    public final int b;
    public final int a;

    public static final CColor TRANSPARENT = new CColor(0, 0, 0, 0);

    public CColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public CColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public static CColor randomColor() {
        final Random random = new Random(System.nanoTime());

        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        return new CColor(r, g, b);
    }
}
