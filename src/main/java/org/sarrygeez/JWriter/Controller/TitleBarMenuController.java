package org.sarrygeez.JWriter.Controller;

import org.sarrygeez.JWriter.View.TitleBarMenuView;

public class TitleBarMenuController {

    private final EditorController editor;
    private final TitleBarMenuView view;

    public TitleBarMenuController(EditorController editor) {
        this.editor = editor;
        this.view = new TitleBarMenuView(this);
    }

    public EditorController getEditor() {
        return editor;
    }

    public TitleBarMenuView getView() {
        return view;
    }
}
