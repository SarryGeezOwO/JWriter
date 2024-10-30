package org.sarrygeez.JWriter;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Controller.HeaderController;
import org.sarrygeez.JWriter.Controller.TitleBarMenuController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.Core.Utils.Logger.LogType;
import org.sarrygeez.JWriter.View.NoteOverviewView;
import org.sarrygeez.JWriter.View.SidebarView;
import org.sarrygeez.JWriter.View.StatusBarView;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Application implements ThemedComponent {

    private static final JFrame frame = new JFrame();
    private static final List<AppListener> listeners = new ArrayList<>();
    private final Dimension startSize = new Dimension(920, 720);
    private final ThemeManager themeManager;
    private final String initialTheme;

    Application(ThemeManager themeManager, String initialTheme) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Launcher.log(LogType.INFO, "Application frame opened.");
                for (AppListener listener : listeners) {
                    listener.onStart();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                Launcher.log(LogType.INFO, "Application frame closed.");
                Launcher.getLogger().dumpToDisk();
                for (AppListener listener : listeners) {
                    listener.onClose();
                }
            }
        });

        this.themeManager = themeManager;
        this.initialTheme = initialTheme;
        themeManager.registerComponent(this);
        initFrame();
    }

    @Override
    public void applyTheme(Theme theme) {
        frame.getRootPane().setBackground(Color.decode(theme.getColor("primary")));
    }

    public static void CloseAPP() {
        frame.dispose();
    }

    public static void addListener(AppListener listener) {
        listeners.add(listener);
    }

    private void initFrame() {
        frame.setTitle("JWriter");
        frame.setSize(startSize);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    public static JFrame getFrame() {
        return frame;
    }

    public class ContentPane extends JPanel {

        ContentPane(Application app) {
            SidebarView sidebar = new SidebarView();
            NoteOverviewView overviewView = new NoteOverviewView();

            EditorController editorController = new EditorController(app, overviewView);
            HeaderController headerController = new HeaderController(app, editorController);
            TitleBarMenuController titleBarMenuController = new TitleBarMenuController(editorController);
            StatusBarView statusBar = new StatusBarView(editorController, headerController);
            editorController.setStatusBar(statusBar);

            themeManager.registerComponent(sidebar);
            themeManager.registerComponent(statusBar);
            themeManager.registerComponent(headerController.getView());
            themeManager.registerComponent(editorController.getView());

            themeManager.registerComponent(statusBar.getLineCol());
            themeManager.registerComponent(statusBar.getEditMode());

            themeManager.registerComponent(titleBarMenuController.getView());
            frame.setJMenuBar(titleBarMenuController.getView());

            setLayout(new BorderLayout());
            add(sidebar, BorderLayout.WEST);
            add(overviewView, BorderLayout.EAST);
            add(statusBar, BorderLayout.SOUTH);
            add(headerController.getView(), BorderLayout.NORTH);
            add(editorController.display(), BorderLayout.CENTER);
        }
    }
}
