package org.sarrygeez.JWriter;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.sarrygeez.JWriter.Core.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    private final ThemeManager themeManager = new ThemeManager();

    Launcher(String themeDir) {
        themeManager.loadThemeFiles(themeDir);

        // NOTE: Default themes will be stored in the resource file
        //       For now in this case, Default theme is considered as a custom theme file
        EventQueue.invokeLater(() ->
                new Application(themeManager, "Default Theme"));
    }

    private static void setupFlatLaF() {
        FlatInterFont.install();
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
                    null, "Base directory for Themes is missing", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open the app
        new Launcher(args[0]);
    }
}