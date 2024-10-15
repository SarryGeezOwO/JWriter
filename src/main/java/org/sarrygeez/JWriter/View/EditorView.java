package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.DocumentUtils;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;

public class EditorView implements ThemedComponent{

    private final EditorController controller;
    private final JTextPane textEditor;
    private final JPanel lineCount;
    private int lineNumber;
    private AttributeSet attrs;

/*
        TODO: Setup the caret
              work on the shit from trello
*/


    @Override
    public void applyTheme(Theme theme) {
        textEditor.setBackground(Color.decode(theme.getEditor("background")));
        lineCount.setBackground(Color.decode(theme.getEditor("lineCountBackground")));
    }

    public EditorView(EditorController controller, CustomDocumentFilter documentFilter) {
        this.controller = controller;

        // TextEditor Component
        textEditor = new JTextPane();
        StyledDocument styledDocument = textEditor.getStyledDocument();
        ((AbstractDocument) styledDocument).setDocumentFilter(documentFilter);

        // TODO: Implement this when settings is added
        //textEditor.setCaret(new CustomCaret());


        // Line Count Component
        lineCount = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                initLineCount(g);
            }
        };
    }

    public JTextPane displayTextEditor() {
        SimpleAttributeSet as = new SimpleAttributeSet();
        // Tabs
        int tabSize = 40; // Set the tab size (in pixels)
        TabStop[] tabStops = new TabStop[10];
        for (int i = 0; i < tabStops.length; i++) {
            tabStops[i] = new TabStop((i + 1) * tabSize);
        }

        TabSet tabSet = new TabSet(tabStops);
        StyleConstants.setTabSet(as, tabSet);

        // Set Default Font ([Lexend Deca] index = 1)
        StyleConstants.setFontFamily(as, FontLoader.appFontsFamily.get(1));
        StyleConstants.setFontSize(as, 16);
        //StyleConstants.setLineSpacing(as, 0.3f); // lmao
        StyleConstants.setSpaceBelow(as, 3f);
        attrs = as;

        textEditor.setParagraphAttributes(as, false);
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
        float lineSpace = StyleConstants.getSpaceBelow(attrs);
        int lineHeight = (int)(fontHeight + (lineSpace));

        int startOffset = textEditor.viewToModel2D(new Point(0, 0));
        int startLine = textEditor.getDocument().getDefaultRootElement().getElementIndex(startOffset);
        int y = 0;

        Graphics2D g2d = (Graphics2D) g.create();

        // Draw number lines after the highlight, so it doesn't get affected by the highlight color
        for(int i = startLine; y < DocumentUtils.getTotalLineCount(textEditor) * lineHeight; i++) {
            String lineN = String.format("%4s", i + 1);
            y += lineHeight;

            g2d.drawString(lineN, 10, y - lineSpace - 2);
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
