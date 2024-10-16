package org.sarrygeez.JWriter.Core.Editor;

import javax.swing.text.*;

public class CustomDocumentFilter extends DocumentFilter {

    private boolean isProgrammaticChange = false;
    private final DocumentHistory history;

    public CustomDocumentFilter(DocumentHistory history) {
        this.history = history;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (!isProgrammaticChange) {
            updateHistory();
        }
        super.insertString(fb, offset, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (!isProgrammaticChange) {
            updateHistory();
        }
        super.replace(fb, offset, length, text, attrs);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (!isProgrammaticChange) {
            updateHistory();
        }
        super.remove(fb, offset, length);
    }

    private void updateHistory() {
        history.saveEditorState();
    }

    public void beginProgrammaticChange() {
        isProgrammaticChange = true;
    }

    public void endProgrammaticChange() {
        isProgrammaticChange = false;
    }
}