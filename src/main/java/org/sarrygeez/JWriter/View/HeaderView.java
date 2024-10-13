package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Widget.RoundedPanel;

import javax.swing.*;
import java.awt.*;

public class HeaderView extends JPanel implements ThemedComponent{

    private final JPanel sidebarHeader;
    private final JPanel noteHeader;

    @Override
    public void applyTheme(Theme theme) {
        sidebarHeader.setBackground(Color.decode(theme.getColor("accent")));
        this.setBackground(Color.decode(theme.getColor("primary")));
        noteHeader.setBackground(Color.decode(theme.getColor("primary")));
        ComponentDecorator.addBorder(new Insets(0,0,1,0), noteHeader, theme);
    }

    public HeaderView() {
        setPreferredSize(new Dimension(0, 100));
        setLayout(new BorderLayout());

        sidebarHeader = new RoundedPanel(0, 30, 0, 0);
        noteHeader = new JPanel();

        sidebarHeader.setPreferredSize(new Dimension(310, 0));
        add(sidebarHeader, BorderLayout.WEST);
        add(noteHeader, BorderLayout.CENTER);
    }
}
