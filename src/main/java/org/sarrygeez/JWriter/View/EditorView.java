package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;
import org.sarrygeez.JWriter.Core.Editor.EditorContext;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Core.Utils.DocumentUtils;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EditorView implements ThemedComponent{

    private final EditorController controller;
    private final JTextPane textEditor;
    private final JPanel lineCount;
    private int lineNumber;

    private SimpleAttributeSet attrs;
    private Color lineCountForeground;
    private Color lineHighlightColor;
    private Color lineHighlightForeground;
    private Color editorBackgroundCol;

    private float lineSpace;
    private int lineHeight;
/*
        TODO: Setup the caret
              work on the shit from trello
*/

    @Override
    public void applyTheme(Theme theme) {
        editorBackgroundCol = (Color.decode(theme.getEditor("background")));
        textEditor.setForeground(Color.decode(theme.getEditor("foreground")));
        lineCount.setBackground(Color.decode(theme.getEditor("lineCountBackground")));

        lineCountForeground = Color.decode(theme.getEditor("lineCountForeground"));
        lineHighlightColor = Color.decode(theme.getEditor("lineHighlightBackground"));
        lineHighlightForeground = Color.decode(theme.getEditor("lineHighlightForeground"));
        ComponentDecorator.addPaddedBorder(new EmptyBorder(5,5,0,5), new Insets(0,1,0,0), textEditor, theme);
    }

    public EditorView(EditorController controller, CustomDocumentFilter documentFilter) {
        this.controller = controller;

        // TextEditor Component
        textEditor = new JTextPane() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D)g.create();

                // Paint the background first
                g2d.setColor(editorBackgroundCol);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Not sure, to draw line highlight
                //drawLineHighlight(g2d, this); // Line Highlight

                // Only paint the highlight after the BG but before the text
                controller.getHighlighter().paintComponent(g2d);
                super.paintComponent(g2d);
            }
        };
        textEditor.setOpaque(false);
        StyledDocument styledDocument = textEditor.getStyledDocument();
        ((AbstractDocument) styledDocument).setDocumentFilter(documentFilter);

        initEditorStyle();

        // TODO: Implement this when settings is added
        //textEditor.setCaret(new CustomCaret());

        // Line Count Component
        lineCount = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLineCount(g);
            }
        };

        updateFontMetrics();
    }

    private void initEditorStyle() {
        attrs = new SimpleAttributeSet();
        // Tabs
        int tabSize = 40; // Set the tab size (in pixels)
        TabStop[] tabStops = new TabStop[10];
        for (int i = 0; i < tabStops.length; i++) {
            tabStops[i] = new TabStop((i + 1) * tabSize);
        }

        TabSet tabSet = new TabSet(tabStops);
        StyleConstants.setTabSet(attrs, tabSet);

        // Set Default Font ([Lexend Deca] index = 1)
        StyleConstants.setFontFamily(attrs, FontLoader.appFontsFamily.get(1));
        StyleConstants.setFontSize(attrs, EditorContext.getDefaultFontSize());
        StyleConstants.setSpaceBelow(attrs, EditorContext.getDefaultLineSpace());

        textEditor.setParagraphAttributes(attrs, false);
    }

    public JTextPane displayTextEditor() {
        textEditor.addCaretListener(e -> {
            int offset = textEditor.getCaretPosition();
            lineNumber = textEditor.getDocument().getDefaultRootElement().getElementIndex(offset);
            int col = offset - controller.getOffsetFromLine(lineNumber);
            controller.getStatusBar().setLineStatus(lineNumber+1, col);

            textEditor.repaint();
            lineCount.repaint();
        });
        return textEditor;
    }

    public JPanel displayLineCount() {
        lineCount.setPreferredSize(new Dimension(50, 0));
        return lineCount;
    }

    public void updateFontMetrics() {
        // Calculate the height of a line accounting for line space
        FontMetrics metrics = DocumentUtils.getFontMetricsForStyledDocument(textEditor.getStyledDocument(), textEditor);
        lineSpace = StyleConstants.getSpaceBelow(attrs);
        int fontHeight = metrics.getHeight();

        lineHeight = (int)(fontHeight + (lineSpace));
    }

    private void drawLineCount(Graphics g) {
        int startOffset = textEditor.viewToModel2D(new Point(0, 0));
        int startLine = textEditor.getDocument().getDefaultRootElement().getElementIndex(startOffset);
        int y = 0;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font(FontLoader.appFontsFamily.get(1), Font.PLAIN, 13));

        drawLineHighlight(g2d, lineCount);

        // Draw number lines after the highlight, so it doesn't get affected by the highlight color
        for(int i = startLine; y < DocumentUtils.getTotalLineCount(textEditor) * lineHeight; i++) {
            String lineN = String.format("%4s", i + 1);
            y += lineHeight;

            g2d.setColor(i == lineNumber ? lineHighlightForeground : lineCountForeground);
            g2d.drawString(lineN, 10, y - lineSpace);
        }
    }

    private void drawLineHighlight(Graphics g, JComponent source) {
        g.setColor(lineHighlightColor);
        try {
            int startOffset = textEditor.getDocument().getDefaultRootElement()
                    .getElement(getLineNumber()).getStartOffset();

            Rectangle2D lineRect = textEditor.modelToView2D(startOffset);
            if (lineRect != null) {
                // Refers to the actual TextEditor
                Point panelPoint = SwingUtilities.convertPoint(
                        textEditor, new Point(0, (int) lineRect.getY()), source);

                g.fillRect(0, panelPoint.y-2, source.getWidth(), (int)lineRect.getHeight()+4);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public SimpleAttributeSet getAttrs() {
        return attrs;
    }

    public void setAttrs(SimpleAttributeSet attrs) {
        this.attrs = attrs;
        textEditor.setCharacterAttributes(attrs, false);
    }
}
