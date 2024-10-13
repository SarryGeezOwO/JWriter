package org.sarrygeez.JWriter;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.View.HeaderView;
import org.sarrygeez.JWriter.View.SidebarView;

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

        frame.setContentPane(new ContentPane(this));

        frame.setIconImage(new ImageIcon("AppIcon.png").getImage());
        frame.setVisible(true);
        themeManager.loadTheme("Dark Lunar Pink Theme");
    }

    public JFrame getFrame() {
        return frame;
    }

    public class ContentPane extends JPanel {

        ContentPane(Application app) {

            HeaderView header = new HeaderView();
            SidebarView sidebar = new SidebarView();

            EditorController editorController = new EditorController(app);

            themeManager.registerComponent(header);
            themeManager.registerComponent(sidebar);
            themeManager.registerComponent(editorController.getView());

            setLayout(new BorderLayout());
            add(header, BorderLayout.NORTH);
            add(sidebar, BorderLayout.WEST);
            add(editorController.display(), BorderLayout.CENTER);
        }

    }
}
