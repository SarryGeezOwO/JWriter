package org.sarrygeez.JWriter.Controller;

import org.sarrygeez.JWriter.Application;
import org.sarrygeez.JWriter.View.HeaderView;

public class HeaderController {

    private final HeaderView view;
    private final Application app;

    public HeaderController(Application app) {
        this.view = new HeaderView();
        this.app = app;
        app.getThemeManager().registerComponent(view.getDateCreated());
    }

    public void setEditable(boolean flag) {
        view.getTitleField().setEditable(flag);
    }

    public HeaderView getView() {
        return view;
    }
}
