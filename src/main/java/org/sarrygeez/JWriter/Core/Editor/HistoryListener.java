package org.sarrygeez.JWriter.Core.Editor;

import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;

public interface HistoryListener {

    void onActionAdded(int newPointer, DocumentAction action);
    void onPointerMove(int newPointer, boolean isForward);

}
