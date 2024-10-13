package org.sarrygeez.JWriter.Core.Utils;

import org.sarrygeez.JWriter.Core.Theme;

import javax.swing.*;
import java.awt.*;

public class ComponentDecorator {

    private ComponentDecorator() {}

    public static void addBorder(Insets inset, JComponent component, Theme theme) {
        if(!theme.enableBorder) {
            return;
        }

        component.setBorder(BorderFactory.createMatteBorder(
            inset.top, inset.left, inset.bottom, inset.right, Color.decode(theme.getColor("border"))
        ));
    }
}
