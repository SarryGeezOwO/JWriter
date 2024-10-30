package org.sarrygeez.JWriter.View;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Widget.ExtendablePanel;
import org.sarrygeez.JWriter.Widget.WidgetEnum;

import java.awt.*;

public class NoteOverviewView extends ExtendablePanel {

    @Override
    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        ComponentDecorator.addBorder(new Insets(1,1,0,0), this, theme);
    }

    public NoteOverviewView() {
        super(WidgetEnum.Direction.LEFT, 50, 400);
        setPreferredSize(new Dimension(300, 0));
        setVisible(false);
    }
}
