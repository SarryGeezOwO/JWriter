package org.sarrygeez.JWriter.Core.Utils;

import org.sarrygeez.JWriter.Core.Utils.Logger.LogType;
import org.sarrygeez.JWriter.Launcher;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {

    public static ImageIcon loadImage(String image, boolean isLight) {
        ImageIcon icn;
        try {
            InputStream stream = ImageLoader.class.getResourceAsStream(
                    "/Icons/"+image+"-"+ ((isLight) ? "Light":"Dark") + ".png");

            if (stream == null) {
                Launcher.log(LogType.ERROR, "Icon not found.");
                throw new RuntimeException("Icon not found.");
            }

            icn = new ImageIcon(stream.readAllBytes());
            stream.close();
        }
        catch (IOException e) {
            // Prepare a dummy image or a fallback image if the image failed to load
            Launcher.log(LogType.ERROR,
                    "Error: loadingImage " +
                            (isLight ? "Light" : "Dark") +
                            " {"+image+"}.",
                    e);
            throw new RuntimeException(e); // Remove this when fallback image is ready
        }
        return icn;
    }

    public static ImageIcon resizeImage(ImageIcon icn, int width, int height) {
        Image img = icn.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
