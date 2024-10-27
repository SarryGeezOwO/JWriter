package org.sarrygeez.JWriter.Widget;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.DocumentUtils;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;
import org.sarrygeez.JWriter.View.EditorView;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class LineCountWidget extends JPanel implements ThemedComponent {

/*
    This doesn't look like a widget at all lmao
    it uses a very specific View so, erm what the sigma?
 */

    private final EditorView view;
    private Graphics offscreenGraphics;
    private Image offscreenImage;

    private float lineSpace;
    private int lineHeight;

    @Override
    public void applyTheme(Theme theme) {
        setBackground(Color.decode(theme.getEditor("lineCountBackground")));
    }

    public LineCountWidget(EditorView view) {
        this.view = view;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(offscreenImage == null || offscreenImage.getWidth(null) != getWidth()
                || offscreenImage.getHeight(null) != getHeight()) {
            offscreenImage = createImage(getWidth(), getHeight());
            offscreenGraphics = offscreenImage.getGraphics();
        }

        Graphics2D g2d = (Graphics2D) offscreenGraphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font(FontLoader.appFontsFamily.get(1), Font.PLAIN, 13));
        g2d.clearRect(0, 0, getWidth(), getHeight());

        view.drawLineHighlight(g2d, this);
        drawLineCount(g2d);
        g.drawImage(offscreenImage, 0, 0, this);
        super.paintComponent(g2d);
    }

    public void updateFontMetrics() {
        // Calculate the height of a line accounting for line space
        FontMetrics metrics = DocumentUtils.getFontMetricsForStyledDocument(view.getTextEditor());
        lineSpace = StyleConstants.getSpaceBelow(view.getAttrs());
        int fontHeight = metrics.getHeight();

        lineHeight = (int)(fontHeight + (lineSpace));
    }

    private void drawLineCount(Graphics2D g) {
        int startOffset = view.getTextEditor().viewToModel2D(new Point(0, 0));
        int startLine = view.getTextEditor().getDocument().getDefaultRootElement().getElementIndex(startOffset);
        int y = 0;

        // Draw number lines after the highlight, so it doesn't get affected by the highlight color
        for(int i = startLine; y < DocumentUtils.getTotalLineCount(view.getTextEditor()) * lineHeight; i++) {
            String lineN = String.format("%4s", i + 1);
            y += lineHeight;

            g.setColor(i == view.getLineNumber() ? view.lineHighlightForeground : view.lineCountForeground);
            g.drawString(lineN, 10, y - lineSpace);
        }
    }
}
