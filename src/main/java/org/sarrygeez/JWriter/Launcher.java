package org.sarrygeez.JWriter;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;
import org.sarrygeez.JWriter.Core.Utils.Logger.LogType;
import org.sarrygeez.JWriter.Core.Utils.Logger.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Launcher {

    private final ThemeManager themeManager = new ThemeManager();

    // There are probably better ways of accessing this
    // rather than a one-way access point lmao
    // But we are using In-Memory logging anyway
    // The writing to the disc will be triggered when the app closed
    private static final Logger logger = new Logger();
    private static String initTheme = "Default Dark Theme"; // Startup theme to use

    Launcher(String baseDir) {
        // Default themes are stored in the resource directory
        themeManager.loadThemeFiles(baseDir + "/themes");
        themeManager.loadResourceThemeFiles();
        FontLoader.loadCustomFont(baseDir);
        FontLoader.loadAppFonts();

        handleCrash();
        EventQueue.invokeLater(() ->
                new Application(themeManager, initTheme));
    }

    private void handleCrash() {
        // Handle Application crash e.g., Unhandled exception somewhere from the code
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Unhandled exception: " + throwable.getMessage());
            for(int i = 0; i < 6; i++) {
                StackTraceElement trace = throwable.getStackTrace()[i];
                System.err.println(trace);
            }
            Launcher.log(LogType.FATAL, "Application crashed.", throwable);
            Launcher.getLogger().dumpToDisk();
            System.exit(69); // 69 means a undefined fatal error for now idk...
        });
    }

    private static void setupFlatLaF() {
        FlatInterFont.install();
        FlatJetBrainsMonoFont.install();
        FlatMacDarkLaf.setup();

        UIManager.put("defaultFont", new Font(FlatInterFont.FAMILY, Font.PLAIN, 13));
        UIManager.put("TitlePane.centerTitle", true);
        System.setProperty("flatlaf.menuBarEmbedded", "true");
    }

    private static void setupLogger(String baseDir) {
        File logFolder = new File(baseDir + "/logs");
        if(logFolder.mkdir()) {
            System.out.println("Logger base folder created");
        }
        logger.setBaseDir(logFolder.getAbsolutePath());
        logger.log(LogType.INFO, "Program process started.");
    }

    public synchronized static Logger getLogger() {
        return logger;
    }

    public synchronized static void log(LogType type, String msg) {
        logger.log(type, msg);
    }

    public static void log(LogType type, String msg, Throwable err) {
        logger.log(type, msg, err);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        setupFlatLaF();

        // Don't open the app, instead show an error window
        if(args.length < 1) {
            JOptionPane.showMessageDialog(
                    null, "Base directory is missing", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dir = args[0];
        setupLogger(dir);
        /*
        Optional launcher configurations
            "-dl" < Disable logging(for idk, small fixes ig?)
            "-t"  < Select a theme on startup (overriding the saved settings)
        */
        if(args.length > 1) { // Additional argument options
            for(int i = 1; i < args.length; i++) {
                handleAdditionalArg(args[i]);
            }
        }
        new Launcher(dir);

        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        log(LogType.INFO, "Application configuration took: "+ elapsedTime + "ms");
    }

    private static void handleAdditionalArg(String arg) {
        switch (arg) {
            case "-dl":
                getLogger().setWriteToDisc(false);
                break;
            case "-t":
            case "-theme":
                System.out.print("Theme Name\n> ");
                Scanner scanner = new Scanner(System.in);
                initTheme = scanner.nextLine();
                log(LogType.DEBUG, "Override initial theme: " + initTheme);
                break;
            default:
                System.err.println("Unknown argument: {"+ arg + "}.");
                break;
        }
    }
}