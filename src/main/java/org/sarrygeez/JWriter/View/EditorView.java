package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.DocumentUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class EditorView implements ThemedComponent{

    private final EditorController controller;
    private final JTextPane textEditor;
    private final JPanel lineCount;
    private int lineNumber;

    @Override
    public void applyTheme(Theme theme) {
        textEditor.setBackground(Color.decode(theme.getEditor("background")));
        lineCount.setBackground(Color.decode(theme.getEditor("lineCountBackground")));
    }

    public EditorView(EditorController controller) {
        this.controller = controller;
        textEditor = new JTextPane();
        lineCount = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                initLineCount(g);
            }
        };
    }

    public JTextPane displayTextEditor() {
        textEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {update();}
            @Override
            public void removeUpdate(DocumentEvent e) {update();}
            @Override
            public void changedUpdate(DocumentEvent e) {update();}
            private void update() {
                lineCount.repaint();
                textEditor.repaint();
            }
        });

        textEditor.addCaretListener(e -> {
            int offset = textEditor.getCaretPosition();
            lineNumber = textEditor.getDocument().getDefaultRootElement().getElementIndex(offset);
        });
        return textEditor;
    }

    public JPanel displayLineCount() {
        lineCount.setPreferredSize(new Dimension(50, 0));
        return lineCount;
    }

    private void initLineCount(Graphics g) {
        // Calculate the height of a line accounting for line space
        FontMetrics metrics = DocumentUtils.getFontMetricsForStyledDocument(textEditor.getStyledDocument(), textEditor);
        int fontHeight = metrics.getHeight();
        float lineSpace = StyleConstants.getLineSpacing(textEditor.getStyledDocument().getDefaultRootElement().getAttributes());
        int lineHeight = (int)(fontHeight * (1 + lineSpace));

        int startOffset = textEditor.viewToModel2D(new Point(0, 0));
        int startLine = textEditor.getDocument().getDefaultRootElement().getElementIndex(startOffset);
        int y = 0;

        Graphics2D g2d = (Graphics2D) g.create();

        // Draw number lines after the highlight, so it doesn't get affected by the highlight color
        for(int i = startLine; y < DocumentUtils.getTotalLineCount(textEditor) * lineHeight; i++) {
            String lineN = String.format("%4s", i + 1);
            y += lineHeight;

            // Draw line number at current y
            g2d.drawString(lineN, 10, y);

        }
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
