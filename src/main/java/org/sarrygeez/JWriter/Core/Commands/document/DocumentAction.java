package org.sarrygeez.JWriter.Core.Commands.document;

public abstract class DocumentAction {

    public boolean isExecuted = true; // By default, they're true, executed by a third party

    public abstract void execute();
    public abstract void unexecute();
}
