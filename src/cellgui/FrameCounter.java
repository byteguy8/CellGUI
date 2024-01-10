package cellgui;

public class FrameCounter {
    private int frameCount = 0;
    private int frames = 0;
    private long start = 0;

    public void init() {
        start = System.currentTimeMillis();
    }

    public void frame() {
        long time = System.currentTimeMillis();
        long diff = time - start;

        if (diff >= 1000) {
            if (diff == 1000) {
                frameCount++;
            }

            start = time;
            frames = frameCount;

            frameCount = 0;
        } else {
            frameCount++;
        }
    }

    public int frameCount() {
        return frames;
    }
}
