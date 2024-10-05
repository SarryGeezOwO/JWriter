package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Core.Theme;

import javax.swing.*;
import java.awt.*;

public class HeaderView extends JPanel implements ThemedComponent{

    @Override
    public void applyTheme(Theme theme) {
        setBackground(Color.decode(theme.getColor("accent")));
    }

    public HeaderView() {
        setPreferredSize(new Dimension(0, 200));
    }
}
