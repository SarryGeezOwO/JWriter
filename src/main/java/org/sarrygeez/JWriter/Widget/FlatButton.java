package org.sarrygeez.JWriter.Widget;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FlatButton extends JButton {

    public FlatButton(String text, ActionListener listener) {
        super(text);
        addActionListener(listener);
    }
}
