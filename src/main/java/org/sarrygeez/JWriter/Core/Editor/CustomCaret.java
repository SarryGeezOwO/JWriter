package org.sarrygeez.JWriter.Core.Editor;

import javax.swing.text.DefaultCaret;
import java.awt.*;

public class CustomCaret extends DefaultCaret {

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    protected synchronized void damage(Rectangle r) {
        super.damage(r);
    }
}
