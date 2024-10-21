package org.sarrygeez.JWriter.Core.Commands.document;

import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class RemoveText extends DocumentAction{

    private final String text;
    private final int offset;
    private final Document editor;
    private final AttributeSet attrs;
    private final CustomDocumentFilter filter;

    public RemoveText(String text, int offset, CustomDocumentFilter filter, Document editor, AttributeSet attrs) {
        this.text = text;
        this.offset = offset;
        this.attrs = attrs;
        this.editor = editor;
        this.filter = filter;
    }

    @Override
    public void execute() {
        super.execute();
        try {
            filter.beginProgrammaticChange();
            editor.remove(offset, text.length());
            filter.endProgrammaticChange();
        }
        catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unexecute() {
        super.unexecute();
        try {
            filter.beginProgrammaticChange();
            editor.insertString(offset, text, attrs);
            filter.endProgrammaticChange();
        }
        catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Remove Text";
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
