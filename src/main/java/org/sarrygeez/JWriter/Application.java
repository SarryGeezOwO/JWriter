package org.sarrygeez.JWriter;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Controller.HeaderController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.View.SidebarView;
import org.sarrygeez.JWriter.View.StatusBarView;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import java.awt.*;

public class Application implements ThemedComponent {

    private final JFrame frame = new JFrame();
    private final Dimension startSize = new Dimension(920, 720);
    private final ThemeManager themeManager;
    private final String initialTheme;

    Application(ThemeManager themeManager, String initialTheme) {
        this.themeManager = themeManager;
        this.initialTheme = initialTheme;
        themeManager.registerComponent(this);
        initFrame();
    }

    @Override
    public void applyTheme(Theme theme) {
        frame.getRootPane().setBackground(Color.decode(theme.getColor("primary")));
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
        themeManager.loadTheme(initialTheme);
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public Application getThis() {
        return this;
    }

    public JFrame getFrame() {
        return frame;
    }

    public class ContentPane extends JPanel {

        ContentPane(Application app) {
            SidebarView sidebar = new SidebarView();

            EditorController editorController = new EditorController(app);
            HeaderController headerController = new HeaderController(app);
            StatusBarView statusBar = new StatusBarView(editorController, headerController);
            editorController.setStatusBar(statusBar);

            themeManager.registerComponent(sidebar);
            themeManager.registerComponent(statusBar);
            themeManager.registerComponent(headerController.getView());
            themeManager.registerComponent(editorController.getView());

            themeManager.registerComponent(statusBar.getLineCol());
            themeManager.registerComponent(statusBar.getEditMode());

            setLayout(new BorderLayout());
            add(sidebar, BorderLayout.WEST);
            add(statusBar, BorderLayout.SOUTH);
            add(headerController.getView(), BorderLayout.NORTH);
            add(editorController.display(), BorderLayout.CENTER);
        }

    }
}
