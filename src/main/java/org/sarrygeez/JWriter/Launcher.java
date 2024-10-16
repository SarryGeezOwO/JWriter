package org.sarrygeez.JWriter;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    private final ThemeManager themeManager = new ThemeManager();

    Launcher(String baseDir) {
        themeManager.loadThemeFiles(baseDir + "/themes");
        themeManager.loadResourceThemeFiles();
        FontLoader.loadCustomFont(baseDir);
        FontLoader.loadAppFonts();

        // Default themes are stored in the resource directory
        EventQueue.invokeLater(() ->
                new Application(themeManager, "Default Dark Theme"));
    }

    private static void setupFlatLaF() {
        FlatInterFont.install();
        FlatJetBrainsMonoFont.install();
        FlatMacDarkLaf.setup();

        UIManager.put("defaultFont", new Font(FlatInterFont.FAMILY, Font.PLAIN, 13));
        UIManager.put("TitlePane.centerTitle", true);
        System.setProperty("flatlaf.menuBarEmbedded", "true");
    }

    public static void main(String[] args) {
        setupFlatLaF();

        // Don't open the app, instead show an error window
        if(args.length != 1) {
            JOptionPane.showMessageDialog(
                    null, "Base directory is missing", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open the app
        new Launcher(args[0]);
    }
}