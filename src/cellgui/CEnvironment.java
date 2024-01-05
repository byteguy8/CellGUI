package cellgui;

public class CEnvironment {
    public final int width;
    public final int height;
    public final CMouseInf mouseInf;

    public CEnvironment(int width, int height, CMouseInf mouseInf) {
        this.width = width;
        this.height = height;
        this.mouseInf = mouseInf;
    }
}
