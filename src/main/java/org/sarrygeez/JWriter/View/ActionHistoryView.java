package org.sarrygeez.JWriter.View;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;
import org.sarrygeez.JWriter.Core.Editor.DocumentHistory;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ActionHistoryView extends JScrollPane implements ThemedComponent {

    private final DocumentHistory history;
    private final Font font;
    private final JPanel p = new JPanel();
    private final JLabel header = new JLabel();

    @Override
    public void applyTheme(Theme theme) {

    }

    public ActionHistoryView(DocumentHistory history) {
        this.history = history;
        getViewport().setView(p);
        setPreferredSize(new Dimension(200, 0));
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        p.setLayout(new MigLayout("fillX, insets 10, gap 5, flowY"));
        font = FontLoader.getFont("Lexend Deca");

        header.setText("Action History");
        header.setFont(font.deriveFont(Font.BOLD, 18f));

        setBorder(new EmptyBorder(0,0,0,0));
        refreshData(history.getPointer());
    }

    public void refreshData(int pointer) {
        p.removeAll();
        p.add(header, "north, grow");

        for(int i = 0; i < history.getActionHistory().size(); i++) {
            DocumentAction j = history.getActionHistory().get(i);
            DocumentAction current = history.getActionHistory().get(pointer);

            p.add(actionView(j , j == current));
        }
        p.revalidate();
        p.repaint();
    }

    private JLabel actionView(DocumentAction action, boolean isSelected) {
        JLabel label = new JLabel(action.toString() + (isSelected ? " «":""));
        label.setFont(font.deriveFont(14f));

        return label;
    }
}
