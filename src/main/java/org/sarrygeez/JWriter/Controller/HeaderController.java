package org.sarrygeez.JWriter.Controller;

import org.sarrygeez.JWriter.Application;
import org.sarrygeez.JWriter.View.HeaderView;

public class HeaderController {

    private final HeaderView view;
    private final EditorController editor;

    public HeaderController(Application app, EditorController editor) {
        this.editor = editor;
        this.view = new HeaderView(this);
        app.getThemeManager().registerComponent(view.getDateCreated());
    }

    public void transferFocus() {
        editor.getTextEditor().requestFocus();
    }

    public void setEditable(boolean flag) {
        view.getTitleField().setEditable(flag);
    }

    public HeaderView getView() {
        return view;
    }
}
