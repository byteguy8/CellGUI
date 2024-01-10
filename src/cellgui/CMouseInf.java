package cellgui;

import java.awt.event.MouseEvent;

public class CMouseInf {
    private int x;
    private int y;

    private int xMov;
    private int yMov;

    private boolean leftPressed;
    private boolean leftReleased;
    private long leftReleaseTime;

    private boolean rightPressed;
    private boolean rightReleased;
    private long rightReleaseTime;

    private int currentMov = 0;
    private final long[][] moveBuffer = new long[2][3];

    public CMouseInf() {
        this.x = 0;
        this.y = 0;
        this.xMov = 0;
        this.yMov = 0;

        this.leftPressed = false;
        this.leftReleased = false;

        this.rightPressed = false;
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

    public synchronized void keyPressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            this.leftPressed = true;
        } else if (event.getButton() == MouseEvent.BUTTON2) {
            this.rightPressed = true;
        }
    }

    public synchronized void keyReleased(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            this.leftPressed = false;
            this.leftReleased = true;
            this.leftReleaseTime = System.nanoTime();
        } else if (event.getButton() == MouseEvent.BUTTON3) {
            this.rightPressed = false;
            this.rightReleased = true;
            this.rightReleaseTime = System.nanoTime();
        }
    }

    public synchronized void restart(long before, long after) {
        if (leftReleased && before >= leftReleaseTime) {
            this.leftReleased = false;
        }

        if (rightReleased && before >= rightReleaseTime) {
            this.rightReleased = false;
        }
    }

    public void setPosition(CVector position) {
        this.x = position.getX();
        this.y = position.getY();

        this.currentMov = put(this.x, this.y, moveBuffer, this.currentMov);

        int xMov;
        int yMov;

        if (moveBuffer[0][2] > moveBuffer[1][2]) {
            xMov = (int) (moveBuffer[0][0] - moveBuffer[1][0]);
            yMov = (int) (moveBuffer[0][1] - moveBuffer[1][1]);
        } else {
            xMov = (int) (moveBuffer[1][0] - moveBuffer[0][0]);
            yMov = (int) (moveBuffer[1][1] - moveBuffer[0][1]);
        }

        this.xMov = xMov;
        this.yMov = yMov;
    }

    public CVector getPosition() {
        return new CVector(this.x, this.y);
    }

    public CVector getMovement() {
        return new CVector(this.xMov, this.yMov);
    }

    public boolean isLeftPressed() {
        return this.leftPressed;
    }

    public boolean isRightPressed() {
        return this.rightPressed;
    }

    public boolean isLeftClicked() {
        return this.leftReleased;
    }

    public boolean isRightClicked() {
        return this.rightReleased;
    }
}
