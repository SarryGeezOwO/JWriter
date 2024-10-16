package org.sarrygeez.JWriter.Widget;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ImageLoader;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ImageButton extends JButton implements ThemedComponent {

    private ImageIcon dark;
    private ImageIcon light;
    private boolean isLight;
    private int size;

    @Override
    public void applyTheme(Theme theme) {
        isLight = theme.isLightTheme;
        setIcon(theme.isLightTheme ? dark : light);
        setBackground(Color.decode(theme.getColor("primary")));
    }

    // Use this to change image
    public void update() {
        setIcon(isLight ? dark : light);
    }

    public ImageButton(String img, int size) {
        setBorder(new EmptyBorder(5, 5, 5, 5));

        this.size = size;
        setDark(img);
        setLight(img);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDark(String dark) {
        this.dark = ImageLoader.resizeImage(ImageLoader.loadImage(dark, false), size, size);
    }

    public void setLight(String light) {
        this.light = ImageLoader.resizeImage(ImageLoader.loadImage(light, true), size, size);
    }
}
