package cellgui.controls;

import cellgui.CColor;
import cellgui.base.CEnvironment;
import cellgui.base.AbstractCell;
import cellgui.base.CBrush;

public class CLabel extends AbstractCell {
    private int fromX;
    private int fromY;

    private String text;
    private String textToRender;
    private CColor textColor;

    public CLabel(String text) {
        super(0, 0);

        this.text = text;
        this.textColor = new CColor(0, 0, 0);
    }

    private String determinateVisibleText() {
        int until = 0;
        int width = getWidth();
        CEnvironment environment = getEnvironment();

        for (int i = 0; i < text.length(); i++) {
            int w = environment.getStringWidth(text.substring(0, until + 1));

            if (w > width) {
                break;
            }

            until++;
        }

        return text.substring(0, until);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CColor getTextColor() {
        return textColor;
    }

    public void setTextColor(CColor textColor) {
        this.textColor = textColor;
    }

    @Override
    public void onInit() {
        super.onInit();

        setBackgroundColor(CColor.TRANSPARENT);
        setWidth(getEnvironment().getStringWidth(text));
        setHeight(getEnvironment().getFontHeight());
    }

    @Override
    public void onLayout() {
        super.onLayout();

        int strWidth = getEnvironment().getStringWidth(text);
        int strHeight = getEnvironment().getFontHeight();

        setWidth(strWidth);
        setHeight(strHeight);

        fromX = (getWidth() - strWidth) / 2;
        fromY = (getHeight() - strHeight) / 2;
    }

    @Override
    public void onDraw(CBrush drawer) {
        super.onDraw(drawer);

        drawer.drawString(
                text,
                fromX,
                fromY,
                textColor
        );
    }
}
