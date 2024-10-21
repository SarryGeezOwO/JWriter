package org.sarrygeez.JWriter.Core.Commands.document;

public abstract class DocumentAction {

    private boolean isExecuted = true; // By default, they're true, executed by a third party

    public void execute() {
        if(isExecuted) {
            return;
        }

        isExecuted = true;
    }
    public void unexecute() {
        if(!isExecuted) {
            return;
        }

        isExecuted = false;
    }
}
