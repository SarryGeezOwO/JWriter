package org.sarrygeez.JWriter.Core.Editor;

import org.sarrygeez.JWriter.Core.Utils.Logger.LogType;
import org.sarrygeez.JWriter.Launcher;
import org.sarrygeez.JWriter.Widget.HighlightedTextWidget;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class RefreshListWorker extends SwingWorker<Void, Void> {
    private final LinkedHashMap<Integer, Integer> lineLocations;
    private final Document doc;
    private final Container container;

    public RefreshListWorker(LinkedHashMap<Integer, Integer> lineLocations, Document doc, Container container) {
        this.lineLocations = lineLocations;
        this.doc = doc;
        this.container = container;
    }

    @Override
    protected Void doInBackground() {
        container.removeAll();

        if (lineLocations.isEmpty()) {
            return null;
        }

        for (Map.Entry<Integer, Integer> entry : lineLocations.sequencedEntrySet()) {
            int start = entry.getKey();
            int end = entry.getValue();
            String text;

            try {
                text = doc.getText(start + 1, (end - start) - 2);
            }
            catch (BadLocationException e) {
                Launcher.log(LogType.FAILURE, "Failed rendering highlighted text.", e);
                continue;
            }

            HighlightedTextWidget widget = new HighlightedTextWidget(
                    text.trim(), getLineFromOffset(start, doc), getLineFromOffset(end, doc));
            widget.setPreferredSize(new Dimension(container.getWidth() - 25, widget.getPreferredSize().height - 2));
            container.add(widget);
        }

        return null;
    }

    @Override
    protected void done() {
        // Update UI when work from background is done
        container.revalidate();
        container.repaint();
    }


    private int getLineFromOffset(int offset, Document doc) {
        return doc.getDefaultRootElement().getElementIndex(offset) + 1;
    }
}

