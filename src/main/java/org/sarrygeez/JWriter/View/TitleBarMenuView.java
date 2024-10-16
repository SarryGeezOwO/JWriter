package org.sarrygeez.JWriter.View;

import com.formdev.flatlaf.FlatClientProperties;
import org.sarrygeez.JWriter.Application;
import org.sarrygeez.JWriter.Controller.TitleBarMenuController;
import org.sarrygeez.JWriter.Core.Theme;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TitleBarMenuView extends JMenuBar implements ThemedComponent {

    private final ArrayList<JMenu> menuList = new ArrayList<>();
    private final ArrayList<JMenuItem> itemList = new ArrayList<>();
    private final TitleBarMenuController controller;

    @Override
    public void applyTheme(Theme theme) {
        putClientProperty(FlatClientProperties.STYLE,
            "background:" + theme.getColor("secondary") +
            ";foreground:" + theme.getColor("text") +
            ";hoverBackground:" + theme.getColor("accentText") +
            ";selectionBackground:" + theme.getColor("text") +
            ";selectionForeground:"+ theme.getColor("primary"));

        for(JMenuItem item : itemList) {
            item.putClientProperty(FlatClientProperties.STYLE,
                "selectionBackground:" + theme.getColor("secondary") +
                ";selectionForeground:" + theme.getColor("primary") +
                ";acceleratorSelectionForeground:" + theme.getColor("accentText"));
        }
    }

    public TitleBarMenuView(TitleBarMenuController controller) {
        this.controller = controller;
        addMenu("File");
        addMenu("Edit");
        addMenu("View");
        addMenu("Help");

        attachMenuItem(menuList.getFirst(), "New", "control N", null);
        attachMenuItem(menuList.getFirst(), "Save", "control S", null);
        attachMenuItem(menuList.getFirst(), "Export", "control alt E", null);
        attachMenuItem(menuList.getFirst(), "Exit", "control shift E", e -> Application.CloseAPP());

        attachMenuItem(menuList.get(1), "Undo", "control Z", e -> controller.getEditor().executeCommand("undo"));
        attachMenuItem(menuList.get(1), "Redo", "control Y", e -> controller.getEditor().executeCommand("redo"));
    }

    private void addMenu(String text) {
        JMenu menu = new JMenu(text);
        menuList.add(menu);
        add(menu);
    }

    private void attachMenuItem(JMenu root, String text, String key, ActionListener e) {
        JMenuItem item = new JMenuItem(text);
        if(e != null) {
            item.addActionListener(e);
        }
        itemList.add(item);
        item.setAccelerator(KeyStroke.getKeyStroke(key));
        root.add(item);
    }
}
