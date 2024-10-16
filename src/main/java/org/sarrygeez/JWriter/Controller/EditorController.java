package org.sarrygeez.JWriter.Controller;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Application;
import org.sarrygeez.JWriter.Core.Command;
import org.sarrygeez.JWriter.Core.Commands.Redo;
import org.sarrygeez.JWriter.Core.Commands.Undo;
import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;
import org.sarrygeez.JWriter.Core.Editor.DocumentHistory;
import org.sarrygeez.JWriter.Core.Editor.DocumentMemento;
import org.sarrygeez.JWriter.View.EditorView;
import org.sarrygeez.JWriter.View.StatusBarView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class EditorController {

    private StatusBarView statusBar;
    private final EditorView view;
    private final Application app;

    private final JComponent lineCount;
    private final JTextPane textEditor;
    private final CustomDocumentFilter documentFilter;
    private final HashMap<String, Command> commandMap = new HashMap<>();

    public EditorController(Application app) {
        DocumentHistory history = new DocumentHistory(this);
        this.documentFilter = new CustomDocumentFilter(history);
        this.app = app;

        // Setup UI components
        // EditorView is the combination of the LineCount and the actual textEditor
        this.view = new EditorView(this, documentFilter);
        lineCount = view.displayLineCount();
        textEditor = view.displayTextEditor();

        setupKeyBinds();

        // Initiate CommandMap
        commandMap.put("undo", new Undo(history, this));
        commandMap.put("redo", new Redo(history, this));
    }

    public void setStatusBar(StatusBarView statusBar) {
        this.statusBar = statusBar;
    }

    public StatusBarView getStatusBar() {
        return statusBar;
    }

    public EditorView getView() {
        return view;
    }

    // Use this to display the editor to a Container
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

    public void setEditable(boolean flag) {
        textEditor.setEditable(flag);
    }

    public boolean isEditable() {
        return textEditor.isEditable();
    }

    public int getOffsetFromLine(int line) {
        if(line == 0) return 0;

        StyledDocument doc = textEditor.getStyledDocument();
        Element root = doc.getDefaultRootElement();

        if(line < 1 || line > root.getElementCount()) {
            return -1;
        }

        Element lineElement = root.getElement(line);
        return lineElement.getStartOffset();
    }

    private void setupKeyBinds() {
        Map<String, String> keyBindings = Map.of(
                "control Z", "undo",
                "control Y", "redo"
        );

        InputMap inputMap = app.getFrame().getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = app.getFrame().getRootPane().getActionMap();

        keyBindings.forEach((keyStroke, actionKey) -> {
            inputMap.put(KeyStroke.getKeyStroke(keyStroke), actionKey);
            addToActionMap(actionMap, actionKey, e -> executeCommand(actionKey));
        });
    }

    private void addToActionMap(ActionMap map, String key, ActionListener action) {
        map.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }

    private void executeCommand(String commandKey) {
        Command command = commandMap.get(commandKey);
        if(command != null) {
            command.execute();
        }
        else {
            System.err.println("Command not found: " + commandKey);
        }
    }

    public DocumentMemento createMemento() {
        return new DocumentMemento(textEditor.getText());
    }

    public void setDocumentMemento(DocumentMemento state) {
        documentFilter.beginProgrammaticChange();
        textEditor.setText(state.state());
        documentFilter.endProgrammaticChange();
    }
}
