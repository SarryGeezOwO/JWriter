package org.sarrygeez.JWriter.Utils;

import javax.swing.*;
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
}
