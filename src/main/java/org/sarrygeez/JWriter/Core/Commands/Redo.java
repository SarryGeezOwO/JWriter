package org.sarrygeez.JWriter.Core.Commands;

import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Command;
import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;
import org.sarrygeez.JWriter.Core.Editor.DocumentHistory;

import java.util.Optional;

public class Redo implements Command {

    private final DocumentHistory history;
    private final EditorController controller;

    public Redo(DocumentHistory history, EditorController controller) {
        this.history = history;
        this.controller = controller;
    }

    @Override
    public void execute() {
        if(!controller.isEditable()) {
            return;
        }
        history.executePointerCommand();
        history.movePointerForward();
    }
}
