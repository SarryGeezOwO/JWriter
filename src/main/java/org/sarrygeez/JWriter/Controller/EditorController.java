package org.sarrygeez.JWriter.Controller;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Application;
import org.sarrygeez.JWriter.Core.Command;
import org.sarrygeez.JWriter.Core.Commands.Redo;
import org.sarrygeez.JWriter.Core.Commands.Undo;
import org.sarrygeez.JWriter.Core.Commands.document.DocumentAction;
import org.sarrygeez.JWriter.Core.Editor.CustomDocumentFilter;
import org.sarrygeez.JWriter.Core.Editor.DocumentHistory;
import org.sarrygeez.JWriter.Core.Editor.HistoryListener;
import org.sarrygeez.JWriter.View.DocumentHighlighter;
import org.sarrygeez.JWriter.View.EditorView;
import org.sarrygeez.JWriter.View.StatusBarView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorController {

    private StatusBarView statusBar;
    private final EditorView view;
    private final TextFormatterController formatterController;
    private final Application app;
    private final DocumentHistory history;

    private final JComponent lineCount;
    private final JTextPane textEditor;
    private final HashMap<String, Command> commandMap = new HashMap<>();
    private final DocumentHighlighter highlighter;

    public void addHistoryListener(HistoryListener listener) {
        history.addHistoryListener(listener);
    }

    public EditorController(Application app) {
        this.app = app;

        history = new DocumentHistory();
        CustomDocumentFilter documentFilter = new CustomDocumentFilter(history, this);
        this.formatterController = new TextFormatterController(this);

        // Setup UI components
        // EditorView is the combination of the LineCount and the actual textEditor
        this.view = new EditorView(this, documentFilter);
        lineCount = view.displayLineCount();
        textEditor = view.displayTextEditor();

        // Highlighter setup
        this.highlighter = new DocumentHighlighter(textEditor);

        // More setup.... 🔥🔥🔥`
        app.getThemeManager().registerComponent(this.highlighter);
        app.getThemeManager().registerComponent(this.formatterController.getView());
        setupKeyBinds();

        // Initiate CommandMap
        commandMap.put("undo", new Undo(history, this));
        commandMap.put("redo", new Redo(history, this));
    }

    public void requestFocus() {
        getTextEditor().requestFocus();
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public Application getApp() {
        return app;
    }

    public JTextPane getTextEditor() {
        return textEditor;
    }

    public DocumentHighlighter getHighlighter() {
        return highlighter;
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

    // Use values from EditorContext and pass it in here
    @SuppressWarnings("unused")
    public void UpdateFont(String family, int size, float spacing) {
        SimpleAttributeSet attrs = getView().getAttrs();
        StyleConstants.setFontFamily(attrs, family);
        StyleConstants.setFontSize(attrs, size);
        StyleConstants.setSpaceBelow(attrs, spacing);
        getView().setAttrs(attrs);
    }

    // Use this to display the editor to a Container
    public JPanel display() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        JPanel p = new JPanel(new MigLayout("fill, insets 0, gap 0"));

        p.add(lineCount, "west, span, grow");
        p.add(textEditor, "center, span, grow");

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.getHorizontalScrollBar().setUnitIncrement(15);
        scroll.getVerticalScrollBar().setUnitIncrement(15);

        root.add(formatterController.getView(), BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    public void setEditable(boolean flag) {
        textEditor.setEditable(flag);
    }

    @SuppressWarnings("all")
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

        InputMap inputMap = Application.getFrame().getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = Application.getFrame().getRootPane().getActionMap();

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

    public void executeCommand(String commandKey) {
        Command command = commandMap.get(commandKey);
        if(command != null) {
            command.execute();
        }
        else {
            System.err.println("Command not found: " + commandKey);
        }
    }
}
