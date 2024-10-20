package org.sarrygeez.JWriter.Core.Commands.document;

import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class InsertText implements DocumentAction {

    // Introduction of isExecuted, enables actions to be executed once

    private final String text;
    private final int offset;
    private final Document editor;
    private final AttributeSet attrs;
    private final CustomDocumentFilter filter;
    private boolean isExecuted = true; // By default, they're true, executed by the CustomDocumentFilter

    public InsertText(String text, int offset, CustomDocumentFilter filter, Document editor, AttributeSet attrs) {
        this.text = text;
        this.offset = offset;
        this.attrs = attrs;
        this.editor = editor;
        this.filter = filter;
    }

    @Override
    public void execute() {
        if(isExecuted) {
            return;
        }

        try {
            filter.beginProgrammaticChange();
            editor.insertString(offset, text, attrs);
            filter.endProgrammaticChange();
            isExecuted = true;
        }
        catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unexecute() {
        if(!isExecuted) {
            return;
        }

        try {
            filter.beginProgrammaticChange();
            editor.remove(offset, text.length());
            filter.endProgrammaticChange();
            isExecuted = false;
        }
        catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Insert Text: \"" + text + "\"";
    }

    @SuppressWarnings("unused")
    public String getText() {
        return text;
    }

    @SuppressWarnings("unused")
    public int getOffset() {
        return offset;
    }
}
