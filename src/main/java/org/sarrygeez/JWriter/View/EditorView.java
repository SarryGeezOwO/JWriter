package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;
import org.sarrygeez.JWriter.Core.Editor.EditorContext;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;
import org.sarrygeez.JWriter.Widget.LineCountWidget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EditorView implements ThemedComponent{

    private final EditorController controller;
    private final JTextPane textEditor;
    private final LineCountWidget lineCount;
    private int lineNumber;

    public Color lineCountForeground;
    public Color lineHighlightColor;
    public Color lineHighlightForeground;

    private SimpleAttributeSet attrs;
    private Color editorBackgroundCol;
/*
        TODO: Setup the caret
              work on the shit from trello
*/

    @Override
    public void applyTheme(Theme theme) {
        editorBackgroundCol = (Color.decode(theme.getEditor("background")));
        textEditor.setForeground(Color.decode(theme.getEditor("foreground")));
        ComponentDecorator.addPaddedBorder(new EmptyBorder(5,5,0,5), new Insets(0,1,0,0), textEditor, theme);

        lineCountForeground = Color.decode(theme.getEditor("lineCountForeground"));
        lineHighlightColor = Color.decode(theme.getEditor("lineHighlightBackground"));
        lineHighlightForeground = Color.decode(theme.getEditor("lineHighlightForeground"));
    }

    public EditorView(EditorController controller, CustomDocumentFilter documentFilter) {
        this.controller = controller;

        // TextEditor Component
        textEditor = new JTextPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g.create();

                // Paint the background first
                g2d.setColor(editorBackgroundCol);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Not sure, to draw line highlight
                // Might be better with double buffering, not sure yet tho
                drawLineHighlight(g2d, this); // Line Highlight

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
        lineCount = new LineCountWidget(this);
        lineCount.updateFontMetrics();
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

    public void drawLineHighlight(Graphics g, JComponent source) {
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

    public LineCountWidget displayLineCount() {
        lineCount.setPreferredSize(new Dimension(50, 0));
        return lineCount;
    }

    public JTextPane getTextEditor() {
        return textEditor;
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
