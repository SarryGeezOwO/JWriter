package org.sarrygeez.JWriter.View;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Core.Editor.RefreshListWorker;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;
import org.sarrygeez.JWriter.Widget.ExtendablePanel;
import org.sarrygeez.JWriter.Widget.WidgetEnum;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.LinkedHashMap;

public class NoteOverviewView extends ExtendablePanel {

    private final JPanel listPane = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));

    @Override
    public void applyTheme(Theme theme) {
        super.applyTheme(theme);
        ComponentDecorator.addBorder(new Insets(1,1,0,0), this, theme);
    }

    public NoteOverviewView() {
        super(WidgetEnum.Direction.LEFT, 190, 400);
        setPreferredSize(new Dimension(300, 0));
        setVisible(false);

        listPane.setOpaque(false);
        getContentPane().add(createHeader(), BorderLayout.NORTH);
        getContentPane().add(listPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new MigLayout("FillY, insets 5, gap 5"));

        JLabel label = new JLabel("Highlighted Texts");
        label.setFont(FontLoader.getFont("Lexend Deca").deriveFont(Font.BOLD, 16f));

        p.add(label);
        return p;
    }

    public void refreshListAsync(LinkedHashMap<Integer, Integer> lineLocations, Document doc) {
        RefreshListWorker worker = new RefreshListWorker(lineLocations, doc, listPane);
        worker.execute(); // start the background task
    }

}
