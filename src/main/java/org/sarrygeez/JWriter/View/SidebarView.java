package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Core.Theme;

import javax.swing.*;
import java.awt.*;

public class SidebarView extends JPanel implements ThemedComponent {

    @Override
    public void applyTheme(Theme theme) {
        setBackground(Color.decode(theme.getColor("primary")));
    }

    public SidebarView() {
        setPreferredSize(new Dimension(260, 0));
    }
}
