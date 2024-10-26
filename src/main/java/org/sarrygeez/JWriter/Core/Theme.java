package org.sarrygeez.JWriter.Core;

import org.sarrygeez.JWriter.Core.Utils.Logger.LogType;
import org.sarrygeez.JWriter.Launcher;

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
        if(colors.get(key) == null) {
            Launcher.log(LogType.ERROR, "Colors property not found {"+key+"}.");
            return "#d63936"; // Default to a fucking red color IDK anymore...
        }
        return colors.get(key);
    }

    public String getEditor(String key) {
        if(editor.get(key) == null) {
            Launcher.log(LogType.ERROR, "Editor property not found {"+key+"}.");
            return "#d63936"; // assuming this map only contains color properties lmao
        }
        return editor.get(key);
    }

}
