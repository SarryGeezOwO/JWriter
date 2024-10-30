package org.sarrygeez.JWriter.Widget;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HighlightedTextWidget extends RoundedPanel implements ThemedComponent {

    private final String text;
    private final int start, end;
    private final JTextArea textArea = new JTextArea();
    private final JLabel lineLabel = new JLabel();

    @Override
    public void applyTheme(Theme theme) {

    }

    public HighlightedTextWidget(String text, int start , int end) {
        super(15);
        this.text = text;
        this.start = start;
        this.end = end;

        setLayout(new BorderLayout());
        initComp();
    }

    private void initComp() {
        lineLabel.setText(String.format("%d  →  %d", start, end));
        lineLabel.setFont(lineLabel.getFont().deriveFont(12f));
        lineLabel.setBorder(new EmptyBorder(10, 10, 0, 10));

        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.revalidate();

        add(lineLabel, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
        repaint();
    }

    @SuppressWarnings("unused")
    public int getFontHeight() {
        return textArea.getFontMetrics(textArea.getFont()).getHeight();
    }

    @SuppressWarnings("unused")
    public String getText() {
        return text;
    }

    @SuppressWarnings("unused")
    public int getStart() {
        return start;
    }

    @SuppressWarnings("unused")
    public int getEnd() {
        return end;
    }
}
