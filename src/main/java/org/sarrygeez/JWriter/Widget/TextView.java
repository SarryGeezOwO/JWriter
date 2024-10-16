package org.sarrygeez.JWriter.Widget;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import java.awt.*;

public class TextView extends JLabel implements ThemedComponent {

    private final boolean isAccent;

    @Override
    public void applyTheme(Theme theme) {
        setForeground(Color.decode(theme.getColor(
                (isAccent) ? "accentText" : "text"
        )));
    }

    public TextView(String text, boolean isAccent) {
        super(text);
        this.isAccent = isAccent;
    }

    public void setFontSize(int size) {
        setFont(getFont().deriveFont((float)size));
    }

    public TextView(boolean isAccent) {
        this.isAccent = isAccent;
    }
}
