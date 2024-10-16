package org.sarrygeez.JWriter.Core;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ThemeManager {

    private Theme currentTheme;
    private final List<ThemedComponent> themedComponents = new ArrayList<>();
    private final HashMap<String, Theme> availableThemes = new HashMap<>();
    private final String[] resourceThemes = {
            "DefaultDark.json", "DefaultLight.json"
    };

    public void loadResourceThemeFiles() {
        Gson gson = new Gson();
        try {
            for(String theme : resourceThemes) {
                InputStream is = ThemeManager.class.getResourceAsStream("/DefaultTheme/" + theme);
                if (is == null) {
                    throw new RuntimeException("Theme file not found: {"+ theme +"}.");
                }

                String json = new String(is.readAllBytes());

                // Add The theme to the map
                Type type = new TypeToken<Theme>(){}.getType();
                Theme material = gson.fromJson(json, type);
                availableThemes.put(material.name, material);

                is.close();
            }
        }
        catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

    public void loadThemeFiles(String baseDir) {
        Gson gson = new Gson();

        File themeDir = new File(baseDir);
        for(File theme : Objects.requireNonNull(themeDir.listFiles())) {

            try (BufferedReader reader = new BufferedReader(new FileReader(theme)); ) {

                // Read the file and transform it to a JSON type
                String line;
                StringBuilder json = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    json.append(line);
                }

                // Add The theme to the map
                Type type = new TypeToken<Theme>(){}.getType();
                Theme material = gson.fromJson(json.toString(), type);
                availableThemes.put(material.name, material);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadTheme(String name) {
        Theme theme = availableThemes.get(name);
        loadTheme(theme);
    }

    public void loadTheme(Theme theme) {
        currentTheme = theme;
        applyTheme();
    }

    public void applyTheme() {
        System.out.println(getCurrentTheme().name);
        try {
            // Update Frame mode
            UIManager.setLookAndFeel(currentTheme.isLightTheme ?
                    new FlatMacLightLaf() : new FlatMacDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        // Update all components
        for(ThemedComponent comp : themedComponents) {
            comp.applyTheme(currentTheme);
        }

        FlatLaf.updateUI();
    }

    public void registerComponent(ThemedComponent component) {
        themedComponents.add(component);
    }

    public void unregisterComponent(ThemedComponent component) {
        themedComponents.remove(component);
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }
}
