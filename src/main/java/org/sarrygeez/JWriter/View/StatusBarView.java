package org.sarrygeez.JWriter.View;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Controller.HeaderController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Widget.ImageButton;
import org.sarrygeez.JWriter.Widget.TextView;

import javax.swing.*;
import java.awt.*;

public class StatusBarView extends JPanel implements ThemedComponent {

    private final TextView lineCol = new TextView(false);
    private final TextView editModeLabel = new TextView(false);
    private final ImageButton editMode = new ImageButton("Unlocked", 20);
    private boolean active = true; // Refers to the editMode being 'active'
    // Active    = Unlocked
    // NotActive = Lock

    private final EditorController editor;
    private final HeaderController header;

    @Override
    public void applyTheme(Theme theme) {
        setBackground(Color.decode(theme.getColor("primary")));
        ComponentDecorator.addBorder(new Insets(2,0,0,0), this, theme);
    }

    public StatusBarView(EditorController editor, HeaderController header) {
        this.editor = editor;
        this.header = header;

        setPreferredSize(new Dimension(0, 30));
        setLayout(new MigLayout("fill, inset 0, gap 10, rtl"));

        editMode.addActionListener(e -> setActive(!isActive()));
        editModeLabel.setFontSize(12);

        lineCol.setText("1:0");
        lineCol.setFontSize(12);

        add(editMode, "width 50, split");
        add(lineCol);
        add(editModeLabel, "span, grow, alignX trailing");
        editModeLabel.setText(active ? "   Edit-mode" : "   Read-only");
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        // Change Image
        editModeLabel.setText(active ? "   Edit-mode" : "   Read-only");
        editMode.setDark(active ? "Unlocked":"Lock");
        editMode.setLight(active ? "Unlocked":"Lock");
        editMode.update();

        editor.setEditable(active);
        header.setEditable(active);
    }

    public TextView getLineCol() {
        return lineCol;
    }

    public ImageButton getEditMode() {
        return editMode;
    }

    public void setLineStatus(int row, int column) {
        lineCol.setText(row + ":" + column);
    }
}
