package org.sarrygeez.JWriter.Core.Editor;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Commands.document.InsertText;
import org.sarrygeez.JWriter.Core.Commands.document.RemoveText;

import javax.swing.text.*;

public class CustomDocumentFilter extends DocumentFilter {

    private boolean isProgrammaticChange = false;
    private final DocumentHistory history;
    private final EditorController controller;

    public CustomDocumentFilter(DocumentHistory history, EditorController controller) {
        this.history = history;
        this.controller = controller;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (!isProgrammaticChange) {
            System.out.println("Insert"); // WTF THIS SHIT DOESN'T TRIGGER, BUT REPLACE DOES?????
        }
        super.insertString(fb, offset, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (!isProgrammaticChange) {
            InsertText action = new InsertText(text, offset, this, fb.getDocument(), attrs);
            history.addAction(action);
        }
        super.replace(fb, offset, length, text, attrs);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (!isProgrammaticChange) {
            String text = fb.getDocument().getText(offset, length);
            AttributeSet attr = controller.getTextEditor().getCharacterAttributes();

            RemoveText action = new RemoveText(text, offset, this, fb.getDocument(), attr);
            history.addAction(action);
        }
        super.remove(fb, offset, length);
    }

    public void beginProgrammaticChange() {
        isProgrammaticChange = true;
    }

    public void endProgrammaticChange() {
        isProgrammaticChange = false;
    }
}