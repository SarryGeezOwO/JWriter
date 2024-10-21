package org.sarrygeez.JWriter.Core.Editor;

import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DocumentHistory {

    private final List<DocumentAction> actionHistory = new ArrayList<>();
    private int pointer = 0;

    private HistoryListener listener = null;
    public void addHistoryListener(HistoryListener listener) {
        this.listener = listener;
    }

    public void addAction(DocumentAction action) {
        if(pointer < actionHistory.size()) {
            actionHistory.set(pointer, action);
            actionHistory.subList(pointer + 1, actionHistory.size()).clear();
        }
        else {
            actionHistory.add(action);
        }

        if(listener != null) {
            listener.onActionAdded(pointer, action);
        }
        pointer++;
    }

    public void executePointerCommand() {
        Optional<DocumentAction> act = getPointerAction();
        if(act.isEmpty()) {
            return;
        }

        DocumentAction action = act.get();
        if(!action.isExecuted) {
            action.execute();
            action.isExecuted = true;
        }
    }

    public void unexecutePointerCommand() {
        Optional<DocumentAction> act = getPointerAction();
        if(act.isEmpty()) {
            return;
        }

        DocumentAction action = act.get();
        if(action.isExecuted) {
            action.unexecute();
            action.isExecuted = false;
        }
    }

    public Optional<DocumentAction> getPointerAction() {
        if (pointer >= 0 && pointer < actionHistory.size()) {
            return Optional.ofNullable(actionHistory.get(pointer));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused") // For debugging purposes, yes, hate me...
    public void printHistory() {
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

            if(listener != null) {
                listener.onPointerMove(pointer, false);
            }
        }
    }

    public void movePointerForward() {
        if(actionHistory.isEmpty()) {
            return;
        }

        if(pointer < actionHistory.size()) {
            pointer++;
            pointer = Math.clamp(pointer, 0, actionHistory.size());

            if(listener != null) {
                listener.onPointerMove(Math.clamp(pointer, 0, actionHistory.size()-1), true);
            }
        }
    }

    @SuppressWarnings("unused")
    public List<DocumentAction> getHistory() {
        // to avoid other class modifying it
        return Collections.unmodifiableList(actionHistory);
    }

    @SuppressWarnings("unused")
    public int getPointer() {
        if(actionHistory.isEmpty()) {
            return 0;
        }
        return Math.clamp(pointer, 0, actionHistory.size()-1);
    }
}
