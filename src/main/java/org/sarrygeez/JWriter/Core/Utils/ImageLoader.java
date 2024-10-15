package org.sarrygeez.JWriter.Core.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {
    private ImageLoader(){}

    public static ImageIcon loadImage(String image, boolean isLight) {
        try {
            InputStream stream = ImageLoader.class.getResourceAsStream(
                    "/Icons/"+image+"-"+ ((isLight) ? "Light":"Dark") + ".png");

            if (stream == null)
                throw new RuntimeException("Icon not found");

            stream.close();
            return new ImageIcon(stream.readAllBytes());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ImageIcon resizeImage(ImageIcon icn, int width, int height) {
        Image img = icn.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
