package org.sarrygeez.JWriter.Core.Utils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class DocumentUtils {

    private DocumentUtils() {}

    public static FontMetrics getFontMetricsForStyledDocument(StyledDocument doc, JTextPane textPane) {
        AttributeSet as = doc.getDefaultRootElement().getAttributes();

        // Retrieve the Font from the attribute set
        String fontFamily = StyleConstants.getFontFamily(as);
        int fontSize = StyleConstants.getFontSize(as);
        boolean isBold = StyleConstants.isBold(as);
        boolean isItalic = StyleConstants.isItalic(as);

        int fontStyle = Font.PLAIN;
        if (isBold) fontStyle |= Font.BOLD;
        if (isItalic) fontStyle |= Font.ITALIC;

        Font font = new Font(fontFamily, fontStyle, fontSize);

        // Use JTextPane to get the FontMetrics for the specific Font
        return textPane.getFontMetrics(font);
    }

    public static int getTotalLineCount(JTextPane textPane) {
        Document doc = textPane.getDocument();
        int lineCount;

        try {
            Element root = doc.getDefaultRootElement();
            lineCount = root.getElementCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lineCount;
    }
}
