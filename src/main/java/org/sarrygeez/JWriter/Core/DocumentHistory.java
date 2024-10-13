package org.sarrygeez.JWriter.Core;

import org.sarrygeez.JWriter.Controller.EditorController;

import java.util.Optional;
import java.util.Stack;

public class DocumentHistory {

    private final Stack<DocumentMemento> undoStack = new Stack<>();
    private final Stack<DocumentMemento> redoStack = new Stack<>();
    private final EditorController controller;

    public DocumentHistory(EditorController controller) {
        this.controller = controller;
    }

    public void flushHistory() {
        undoStack.clear();
        redoStack.clear();
    }

    public void saveEditorState() {
        undoStack.push(controller.createMemento());
        redoStack.clear();
    }

    public Optional<DocumentMemento> getUndoState() {
        // Avoid Throwing that shit is garbage, instead let's just
        // expect a null being returned instead

        if(undoStack.isEmpty()) {
            return Optional.empty();
        }

        redoStack.add(controller.createMemento());
        return Optional.ofNullable(undoStack.pop());
    }

    public Optional<DocumentMemento> getRedoState() {
        if(redoStack.isEmpty()) {
            return Optional.empty();
        }

        undoStack.add(controller.createMemento());
        return Optional.ofNullable(redoStack.pop());
    }

    public int getUndoSize() {
        return undoStack.size();
    }

    public int getRedoSize() {
        return redoStack.size();
    }
}
