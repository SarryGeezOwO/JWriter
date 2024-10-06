package org.sarrygeez.JWriter.Controller;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Core.ThemeManager;
import org.sarrygeez.JWriter.View.EditorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EditorController {

    private final EditorView view;

    private final JComponent lineCount;
    private final JTextPane textEditor;

    public EditorController() {
        this.view = new EditorView(this);
        lineCount = view.displayLineCount();
        textEditor = view.displayTextEditor();
    }

    public EditorView getView() {
        return view;
    }

    public JScrollPane display() {
        JPanel p = new JPanel(new MigLayout("fill, insets 0, gap 0"));

        p.add(lineCount, "west, span, grow");
        p.add(textEditor, "center, span, grow");

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.getHorizontalScrollBar().setUnitIncrement(15);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        return scroll;
    }
}
