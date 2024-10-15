package org.sarrygeez.JWriter.Core.Commands;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Command;
import org.sarrygeez.JWriter.Core.Editor.DocumentHistory;
import org.sarrygeez.JWriter.Core.Editor.DocumentMemento;

import java.util.Optional;

public class Undo implements Command {

    private final DocumentHistory history;
    private final EditorController controller;

    public Undo(DocumentHistory history, EditorController controller) {
        this.history = history;
        this.controller = controller;
    }

    @Override
    public void execute() {
        Optional<DocumentMemento> memento = history.getUndoState();
        memento.ifPresent(controller::setDocumentMemento);
    }
}
