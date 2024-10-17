package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Core.Theme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentHighlighter implements ThemedComponent{

    private final JTextPane textPane;
    private final HashMap<Integer, Integer> locatorMap = new HashMap<>();
    private Color highlightCol;

    @Override
    public void applyTheme(Theme theme) {
        highlightCol = Color.decode(theme.getEditor("highlight"));
    }

    public DocumentHighlighter(JTextPane textPane) {
        this.textPane = textPane;
        addDocumentListener();
    }

    private void addDocumentListener() {
        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                triggerCheck();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                triggerCheck();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void triggerCheck() {
        SwingUtilities.invokeLater(() -> {
            checkForBackticks();
            textPane.repaint();
        });
    }

    private void checkForBackticks() {
        List<Integer> backtickOffsets = findAllBackticks();

        // Skip highlighting if odd
        if(backtickOffsets.size() % 2 != 0) {
            locatorMap.clear();
            return;
        }

        HashMap<Integer, Integer> newMap = new HashMap<>();

        for(int i = 0; i < backtickOffsets.size(); i += 2) {
            int start = backtickOffsets.get(i);
            int end = backtickOffsets.get(i+1);

            // Don't include the backticks when rendering the highlight
            newMap.put(start, end+1);
        }

        updateLocatorMap(newMap);
    }

    List<Integer> findAllBackticks() {
        List<Integer> offsets = new ArrayList<>();
        Document doc = textPane.getDocument();

        try {
            for (int i = 0; i < doc.getLength(); i++) {
                String c = doc.getText(i, 1);

                if("`".equals(c)) {
                    offsets.add(i);
                }
            }
        }
        catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        return offsets;
    }

    void updateLocatorMap(HashMap<Integer, Integer> map) {
        locatorMap.keySet().removeIf(offset -> !map.containsKey(offset));
        locatorMap.putAll(map);
    }

    public void paintComponent(Graphics2D g) {
        if(locatorMap.isEmpty()) {
            return;
        }

        for(Map.Entry<Integer, Integer> entry : locatorMap.entrySet()) {
            int startOffset = entry.getKey();
            int endOOffset = entry.getValue();
            drawHighlights(g, startOffset, endOOffset);
        }
    }

    private void drawHighlights(Graphics2D g, int startOffset, int endOffset) {

        int lineSpace = (int)StyleConstants.getSpaceBelow(textPane.getParagraphAttributes());

        try {
            Rectangle2D startRect = getTextRect(startOffset);
            Rectangle2D endRect = getTextRect(endOffset);

            if (startRect == null || endRect == null) {
                return;
            }

            int startLine = textPane.getDocument().getDefaultRootElement().getElementIndex(startOffset);
            int endLine = textPane.getDocument().getDefaultRootElement().getElementIndex(endOffset);

            g.setColor(new Color(
                    highlightCol.getRed(),
                    highlightCol.getGreen(),
                    highlightCol.getBlue(),
                    255
            ));

            if (startLine == endLine) {
                // Single line case
                g.fillRect(
                        (int) startRect.getX(),
                        (int) startRect.getY(),
                        (int) (endRect.getX() - startRect.getX()),
                        (int) startRect.getHeight()
                );
            } else {
                // Multi-line case
                // Draw highlight for the first line (from startOffset to the end of the line)
                Rectangle2D lineEndRect = getTextRect(getLineEndOffset(startLine));
                if(lineEndRect == null) {
                    throw new RuntimeException("error");
                }

                g.fillRect(
                        (int) startRect.getX(),
                        (int) startRect.getY(),
                        (int) (lineEndRect.getMaxX() - startRect.getX()),
                        (int) startRect.getHeight() + lineSpace
                );

                // Draw highlight for all intermediate lines (full width of the text pane)
                for (int line = startLine + 1; line < endLine; line++) {
                    Rectangle2D fullLineRect = getTextRect(getLineStartOffset(line));
                    if(fullLineRect == null) {
                        throw new RuntimeException("error");
                    }
                    g.fillRect(
                            (int) fullLineRect.getX(),
                            (int) fullLineRect.getY(),
                            textPane.getWidth(),
                            (int) fullLineRect.getHeight() + lineSpace
                    );
                }

                // Draw highlight for the last line (from the start of the line to endOffset)
                Rectangle2D lineStartRect = getTextRect(getLineStartOffset(endLine));
                if(lineStartRect == null) {
                    throw new RuntimeException("error");
                }
                g.fillRect(
                        (int) lineStartRect.getX(),
                        (int) lineStartRect.getY(),
                        (int) (endRect.getX() - lineStartRect.getX()),
                        (int) lineStartRect.getHeight()
                );
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private int getLineStartOffset(int line) throws BadLocationException {
        return textPane.getDocument().getDefaultRootElement().getElement(line).getStartOffset();
    }

    private int getLineEndOffset(int line) throws BadLocationException {
        return textPane.getDocument().getDefaultRootElement().getElement(line).getEndOffset() - 1;
    }

    private Rectangle2D getTextRect(int offset) {
        try {
            if (offset < 0 || offset >= textPane.getDocument().getLength()) {
                return null; // Return null if offset is out of bounds
            }
            return textPane.modelToView2D(offset);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
