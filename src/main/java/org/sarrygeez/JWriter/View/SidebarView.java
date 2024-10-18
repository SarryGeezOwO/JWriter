package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;

import javax.swing.*;
import java.awt.*;

public class SidebarView extends JPanel implements ThemedComponent {

    @Override
    public void applyTheme(Theme theme) {
        setBackground(Color.decode(theme.getColor("primary")));
        ComponentDecorator.addBorder(new Insets(1,0,0,1), this, theme);
    }

    public SidebarView() {
        setPreferredSize(new Dimension(260, 0));
    }
}
