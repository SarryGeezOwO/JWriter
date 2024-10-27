package org.sarrygeez.JWriter.Core.Editor;

import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;
import org.sarrygeez.JWriter.Core.Commands.document.InsertText;
import org.sarrygeez.JWriter.Core.Commands.document.RemoveText;

import java.util.*;

public class DocumentHistory {

    private final List<DocumentAction> actionHistory = new ArrayList<>();
    private final List<InsertText> batchInsertList = new ArrayList<>();
    private final List<RemoveText> batchRemoveList = new ArrayList<>();
    private final Timer timer = new Timer();
    private int pointer = 0;

    private HistoryListener listener = null;
    public void addHistoryListener(HistoryListener listener) {
        this.listener = listener;
    }

    public DocumentHistory() {
        // Initiate Timer for checking batch actions
        int batchThreshold = 500;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // When the timer hits, attempt
                // to create a batchedInsert
                batchedAction(batchInsertList, getInsertText());
                batchedAction(batchRemoveList, getRemoveText());
            }
        }, 0, batchThreshold);
    }

    public void addAction(DocumentAction action) {
        if(action instanceof InsertText act) {
            batchInsertList.add(act);
            return;
        }
        else if(action instanceof RemoveText act) {
            batchRemoveList.add(act);
            return;
        }

        if(pointer < actionHistory.size()) {
            actionHistory.set(pointer, action);
            actionHistory.subList(pointer + 1, actionHistory.size()).clear();
        }
        else {
            // Add any remaining batched Actions before appending the new action
            batchedAction(batchInsertList, getInsertText());
            batchedAction(batchRemoveList, getRemoveText());
            actionHistory.add(action);
        }

        pointer++;
        if(listener != null) {
            listener.onActionAdded(pointer, action);
        }
    }

    private <T extends DocumentAction> void batchedAction(List<T> list, T action) {
        if(list.isEmpty()) {
            return;
        }
        if(action == null) {
            return;
        }

        if(pointer < actionHistory.size()) {
            // Overwrite action history
            actionHistory.set(pointer,action);
            actionHistory.subList(pointer + 1, actionHistory.size()).clear();
        }
        else {
            // Simply append
            actionHistory.add(action);
        }
        flushList(list);

        // Update the pointer and do all listeners request
        pointer++;
        if(listener != null) {
            listener.onActionAdded(pointer, action);
        }
    }

    /**
     * Combines all <code>InsertText</code> command from the batchInsertList and turns
     * all of them into a singular <code>InsertText</code>
     * @return the new BatchedInsertText
     */
    private InsertText getInsertText() {
        if(batchInsertList.isEmpty()) {
            return null;
        }

        InsertText start = batchInsertList.getFirst();
        InsertText end = batchInsertList.getLast();
        StringBuilder text = new StringBuilder();

        // Combine all characters
        for(InsertText i : batchInsertList) {
            text.append(i.getText());
        }

        // Create a new Action based on the lists information
        return new InsertText(
                text.toString(),
                start.getOffset(),
                start.getFilter(),
                start.getEditor(),
                end.getAttrs()); // Refer to the last character
    }

    private RemoveText getRemoveText() {
        if(batchRemoveList.isEmpty()) {
            return null;
        }

        RemoveText start = batchRemoveList.getFirst();
        RemoveText end = batchRemoveList.getLast();
        StringBuilder text = new StringBuilder();

        // Combine all characters
        // From last index to first index
        for(int i = batchRemoveList.size()-1; i >= 0; i--) {
            text.append(batchRemoveList.get(i).getText());
        }

        // Create a new Action based on the lists information
        return new RemoveText(
                text.toString(),
                end.getOffset(),
                start.getFilter(),
                start.getEditor(),
                end.getAttrs()); // Refer to the last character
    }

    private <T> void flushList(List<T> list) {
        list.clear();
    }

    /**
     * Performs the <code>Execute()</code> of the DocumentAction
     * that the pointer is pointing to, if the Optional returned is not null
     */
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

    /**
     * Performs the <code>Unexecute()</code> of the DocumentAction
     * that the pointer is pointing to, if the Optional returned is not null
     */
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

    /**
     * Get the DocumentAction based on the pointer's position
     * @return an Optional of a DocumentAction as a null is expected sometimes
     */
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

    public Timer getTimer() {
        return timer;
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
