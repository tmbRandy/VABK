package tmb.randy.vabk.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class GuiNumericField extends GuiButton {
    private static final int DEFAULT_VALUE = 0;
    private static final int BUTTON_WIDTH = 12;
    private int steps = 1;
    private final GuiTextField guiTextField;
    private final GuiButton guiButtonDec;
    private final GuiButton guiButtonInc;

    private String previous = String.valueOf(DEFAULT_VALUE);
    private int minimum = Integer.MIN_VALUE;
    private int maximum = Integer.MAX_VALUE;
    private boolean wasFocused = false;

    public GuiNumericField(final FontRenderer fontRenderer, final int id, final int x, final int y) {
        this(fontRenderer, id, x, y, 100, 20);
    }

    public GuiNumericField(final FontRenderer fontRenderer, final int id, final int x, final int y, final int width) {
        this(fontRenderer, id, x, y, width, 20);
    }

    public GuiNumericField(final FontRenderer fontRenderer, final int id, final int x, final int y, final int width, final int height) {
        super(id, 0, 0, width, height, "");
        this.guiTextField = new GuiTextField(0, fontRenderer, x + 1, y + 1, width - BUTTON_WIDTH * 2 - 2, height - 2);
        this.guiButtonDec = new GuiButton(1, x + width - BUTTON_WIDTH * 2, y, BUTTON_WIDTH, height, "-");
        this.guiButtonInc = new GuiButton(2, x + width - BUTTON_WIDTH * 1, y, BUTTON_WIDTH, height, "+");

        setValue(DEFAULT_VALUE);
    }

    @Override
    public boolean mousePressed(final Minecraft minecraft, final int x, final int y) {
        if (this.wasFocused && !this.guiTextField.isFocused()) {
            this.wasFocused = false;
            return true;
        }

        this.wasFocused = this.guiTextField.isFocused();

        return this.guiButtonDec.mousePressed(minecraft, x, y) || this.guiButtonInc.mousePressed(minecraft, x, y);
    }


    @Override
    public void drawButton(final Minecraft minecraft, final int x, final int y) {
        if (this.visible) {
            try {
                this.guiTextField.drawTextBox();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.guiButtonInc.drawButton(minecraft, x, y);
            this.guiButtonDec.drawButton(minecraft, x, y);
        }
    }

    public void mouseClicked(final int x, final int y, final int action) {
        final Minecraft minecraft = Minecraft.getMinecraft();

        this.guiTextField.mouseClicked(x, y, action);

        if (this.guiButtonInc.mousePressed(minecraft, x, y)) {
            setValue(getValue() + steps);
        }

        if (this.guiButtonDec.mousePressed(minecraft, x, y)) {
            setValue(getValue() - steps);
        }
    }

    public boolean keyTyped(final char character, final int code) {
        if (!this.guiTextField.isFocused()) {
            return false;
        }

        final int cursorPositionOld = this.guiTextField.getCursorPosition();

        this.guiTextField.textboxKeyTyped(character, code);

        String text = this.guiTextField.getText();
        final int cursorPositionNew = this.guiTextField.getCursorPosition();

        if (text.length() == 0 || text.equals("-")) {
            return true;
        }

        try {
            long value = Long.parseLong(text);
            boolean outOfRange = false;

            if (value > this.maximum) {
                value = this.maximum;
                outOfRange = true;
            } else if (value < this.minimum) {
                value = this.minimum;
                outOfRange = true;
            }

            text = String.valueOf(value);

            if (!text.equals(this.previous) || outOfRange) {
                this.guiTextField.setText(String.valueOf(value));
                this.guiTextField.setCursorPosition(cursorPositionNew);
            }

            this.previous = text;

            return true;
        } catch (final NumberFormatException nfe) {
            this.guiTextField.setText(this.previous);
            this.guiTextField.setCursorPosition(cursorPositionOld);
        }

        return false;

    }

    public void updateCursorCounter() {
        this.guiTextField.updateCursorCounter();
    }

    public boolean isFocused() {
        return this.guiTextField.isFocused();
    }

    public void setPosition(final int x, final int y) {
        this.guiTextField.xPosition = x + 1;
        this.guiTextField.yPosition = y + 1;
        this.guiButtonInc.xPosition = x + width - BUTTON_WIDTH * 2;
        this.guiButtonInc.yPosition = y;
        this.guiButtonDec.xPosition = x + width - BUTTON_WIDTH * 1;
        this.guiButtonDec.yPosition = y;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        this.guiTextField.setEnabled(enabled);
        this.guiButtonInc.enabled = enabled;
        this.guiButtonDec.enabled = enabled;
    }

    public void setMinimum(final int minimum) {
        this.minimum = minimum;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public void setMaximum(final int maximum) {
        this.maximum = maximum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    public void setValue(int value) {
        if (value > this.maximum) {
            value = this.maximum;
        } else if (value < this.minimum) {
            value = this.minimum;
        }
        this.guiTextField.setText(String.valueOf(value));
    }

    public int getValue() {
        final String text = this.guiTextField.getText();
        if (text.length() == 0 || text.equals("-")) {
            return DEFAULT_VALUE;
        }
        return Integer.parseInt(text);
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}