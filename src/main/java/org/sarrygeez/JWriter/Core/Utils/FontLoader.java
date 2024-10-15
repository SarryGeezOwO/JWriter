package org.sarrygeez.JWriter.Core.Utils;

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
        try {
            for(String font : appFonts) {
                InputStream is = FontLoader.class.getResourceAsStream("/AppFonts/" + font);
                if (is == null) {
                    throw new RuntimeException("Font file not found: {"+ font +"}.");
                }

                Font appFont = Font.createFont(Font.TRUETYPE_FONT, is);
                appFontsFamily.add(appFont.getFamily());

                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(appFont);

                is.close();
            }
        }
        catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadCustomFont(String baseDir) {
        try {
            File dir = new File(baseDir + "/fonts");
            if (dir.mkdir()) {
                // Change this into a proper logger please
                System.out.println("Base directory for fonts created");
            }

            for(File font : Objects.requireNonNull(dir.listFiles())) {
                if(!font.getName().endsWith(".ttf")) {
                    continue;
                }

                Font f = Font.createFont(Font.TRUETYPE_FONT, font);
                fontMap.put(f.getFamily(), f);

                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
                System.out.println("Custom Font loaded: {"+ f.getFamily() +"}.");
            }
        }
        catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Font getFont(String fontFamily) {
        return fontMap.get(fontFamily);
    }
}
