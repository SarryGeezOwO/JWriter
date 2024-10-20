package org.sarrygeez.JWriter.Core.Editor;

import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentHistory {

    private final List<DocumentAction> actionHistory = new ArrayList<>();
    private int pointer = 0;

    public void addAction(DocumentAction action) {
        pointer++;
        if(pointer < actionHistory.size()) {
            actionHistory.set(pointer, action);
            actionHistory.subList(pointer + 1, actionHistory.size()).clear();
        }
        else {
            actionHistory.add(action);
        }
    }

    public Optional<DocumentAction> getPointerAction() {
        if (pointer >= 0 && pointer < actionHistory.size()) {
            return Optional.ofNullable(actionHistory.get(pointer));
        } else {
            System.out.println("Pointer out of bounds.");
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused") // Cool
    private void printHistory() {
        if(pointer <= -1 || pointer >= actionHistory.size()) {
            return;
        }

        DocumentAction current = actionHistory.get(pointer);
        for(DocumentAction action : actionHistory) {
            if(action == current) {
                System.out.print("->[" + action.toString()+"] ");
            }
            else {
                System.out.print("  ["+action.toString()+"] ");
            }
            System.out.print("   ");
        }
        System.out.println();
    }

    public void movePointerBack() {
        if(actionHistory.isEmpty()) {
            return;
        }

        if(pointer > 0) {
            pointer--;
        }
    }

    public void movePointerForward() {
        if(actionHistory.isEmpty()) {
            return;
        }

        if(pointer + 1 < actionHistory.size()) {
            pointer++;
        }
    }
}
