package org.sarrygeez.JWriter.Core.Utils;

import org.sarrygeez.JWriter.Core.Utils.Logger.LogType;
import org.sarrygeez.JWriter.Launcher;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FontLoader {

    private FontLoader() {}

    private static final HashMap<String, Font> fontMap = new HashMap<>();
    private static final String[] appFonts = {
            "BalooBhai-Regular.ttf",
            "LexendDeca-Regular.ttf"
    };

    public static final List<String> appFontsFamily = new ArrayList<>();

    public static void loadAppFonts() {
        for(String font : appFonts) {
            try(InputStream is = FontLoader.class.getResourceAsStream("/AppFonts/" + font)) {
                if (is == null) {
                    String errMsg = "Resource font not found: {"+font+"}.";
                    Launcher.log(LogType.ERROR, errMsg);
                    throw new RuntimeException(errMsg);
                }

                Font appFont = Font.createFont(Font.TRUETYPE_FONT, is);
                appFontsFamily.add(appFont.getFamily());

                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(appFont);
                fontMap.put(appFont.getFamily(), appFont);
            }
            catch (IOException e) {
                Launcher.log(LogType.ERROR, "FontStream incomplete read.", e);
                throw new RuntimeException(e);
            }
            catch (FontFormatException e) {
                // Prepare a fallback font for UIs using an AppFont(resource Font)
                Launcher.log(LogType.ERROR, "Font not loaded properly {"+font+"}.", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadCustomFont(String baseDir) {
        File dir = new File(baseDir + "/fonts");
        if (dir.mkdir()) {
            Launcher.log(LogType.INFO, "Base directory for custom fonts created");
        }
        for(File font : Objects.requireNonNull(dir.listFiles())) {
            try {
                if(!font.getName().endsWith(".ttf")) {
                    continue;
                }

                Font f = Font.createFont(Font.TRUETYPE_FONT, font);
                fontMap.put(f.getFamily(), f);

                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
                Launcher.log(LogType.SUCCESS, "Custom Font loaded: {"+ f.getFamily() +"}.");
            }
            // If a catch happens, ignore the font from being available
            catch (IOException e) {
                Launcher.log(LogType.ERROR, "FontStream incomplete read.", e);
            }
            catch (FontFormatException e) {
                Launcher.log(LogType.ERROR, "Font not loaded properly {"+font.getName()+"}.", e);
            }
        }
    }

    @SuppressWarnings("unused")
    public static Font getFont(String fontFamily) {
        return fontMap.get(fontFamily);
    }
}
