package org.sarrygeez.JWriter.View;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Widget.RoundedPanel;

import javax.swing.*;
import java.awt.*;

public class HeaderView extends JPanel implements ThemedComponent{

    private final JPanel sidebarHeader;
    private final JPanel noteHeader;

    @Override
    public void applyTheme(Theme theme) {
        this.setBackground(Color.decode(theme.getEditor("background")));
        noteHeader.setBackground(Color.decode(theme.getEditor("background")));
        sidebarHeader.setBackground(Color.decode(theme.getColor("accent")));
    }

    public HeaderView() {
        setPreferredSize(new Dimension(0, 120));
        setLayout(new BorderLayout());

        sidebarHeader = new RoundedPanel(0, 60, 0, 0);
        noteHeader = new JPanel();

        sidebarHeader.setPreferredSize(new Dimension(310, 0));
        add(sidebarHeader, BorderLayout.WEST);
        add(noteHeader, BorderLayout.CENTER);
    }
}
