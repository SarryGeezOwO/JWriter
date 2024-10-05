package org.sarrygeez.JWriter;

import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.View.HeaderView;

import javax.swing.*;
import java.awt.*;

public class Application {

    private final JFrame frame = new JFrame();
    private final Dimension startSize = new Dimension(920, 720);
    private final ThemeManager themeManager;

    Application(ThemeManager themeManager) {
        this.themeManager = themeManager;
        initFrame();
    }

    private void initFrame() {
        frame.setTitle("JWriter");
        frame.setSize(startSize);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(new ContentPane());

        frame.setIconImage(new ImageIcon("AppIcon.png").getImage());
        frame.setVisible(true);

        themeManager.loadTheme("Default Light Theme");
    }

    public class ContentPane extends JPanel {

        ContentPane() {

            HeaderView header = new HeaderView();
            themeManager.registerComponent(header);

            setLayout(new BorderLayout());
            add(header, BorderLayout.NORTH);
        }

    }

}
