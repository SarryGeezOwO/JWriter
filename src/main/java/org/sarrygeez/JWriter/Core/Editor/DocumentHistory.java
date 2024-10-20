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

    public Optional<DocumentAction> getPointerAction() {
        if (pointer >= 0 && pointer < actionHistory.size()) {
            return Optional.ofNullable(actionHistory.get(pointer));
        } else {
            return Optional.empty();
        }
    }

    public void movePointerBack() {
        if(actionHistory.isEmpty()) {
            return;
        }

        if(pointer > 0) {
            pointer--;
            pointer = Math.clamp(pointer, 0, actionHistory.size()-1);

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

    public List<DocumentAction> getActionHistory() {
        // to avoid other class modifying it
        return Collections.unmodifiableList(actionHistory);
    }

    public int getPointer() {
        if(actionHistory.isEmpty()) {
            return 0;
        }
        return Math.clamp(pointer, 0, actionHistory.size()-1);
    }
}
