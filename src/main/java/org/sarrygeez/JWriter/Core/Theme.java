package org.sarrygeez.JWriter.Core;

import java.util.HashMap;

@SuppressWarnings("unused")
public class Theme {

    public String name;
    public String description;
    public boolean isLightTheme;
    public boolean enableBorder;
    public HashMap<String, String> colors;
    public HashMap<String, String> editor;

    public String getColor(String key) {
        return colors.get(key);
    }

    public String getEditor(String key) {
        return editor.get(key);
    }

}
