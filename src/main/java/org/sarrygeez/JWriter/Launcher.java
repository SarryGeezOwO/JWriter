package org.sarrygeez.JWriter;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.sarrygeez.JWriter.Core.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    private final ThemeManager themeManager = new ThemeManager();

    Launcher() {
        setupFlatLaF();
        themeManager.loadThemeFiles("TestDir");

        EventQueue.invokeLater(() -> new Application(themeManager));
    }

    private void setupFlatLaF() {
        FlatInterFont.install();
        FlatMacDarkLaf.setup();

        UIManager.put("defaultFont", new Font(FlatInterFont.FAMILY, Font.PLAIN, 13));
        UIManager.put("TitlePane.centerTitle", true);
        System.setProperty("flatlaf.menuBarEmbedded", "true");
    }

    public static void main(String[] args) {
        new Launcher();
    }
}