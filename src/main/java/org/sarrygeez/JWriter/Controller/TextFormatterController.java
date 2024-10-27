package org.sarrygeez.JWriter.Controller;

import org.sarrygeez.JWriter.View.TextFormatterView;
import org.sarrygeez.JWriter.Widget.ColorPickerListener;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextFormatterController {

    private final TextFormatterView view;
    private final EditorController editorController;

    public enum Styles {
        BOLD, ITALIC, UNDERLINE, STRIKE
    }

    public TextFormatterController(EditorController editorController) {
        this.editorController = editorController;
        this.view = new TextFormatterView(this);
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public void addColorPickerListener(ColorPickerListener listener) {
        view.GetHighlightColorChooser().addListener(listener);
    }

    public void setStyle(Styles style) {
        SimpleAttributeSet attrs = editorController.getView().getAttrs();
        if(style == Styles.BOLD)
            StyleConstants.setBold(attrs, !(isStyleActive(style, attrs)));
        else if(style == Styles.ITALIC)
            StyleConstants.setItalic(attrs, !(isStyleActive(style, attrs)));
        else if(style == Styles.UNDERLINE)
            StyleConstants.setUnderline(attrs, !(isStyleActive(style, attrs)));
        else if(style == Styles.STRIKE)
            StyleConstants.setStrikeThrough(attrs, !(isStyleActive(style, attrs)));
        editorController.getView().setAttrs(attrs);
    }

    private boolean isStyleActive(Styles style, SimpleAttributeSet attrs) {
        return switch (style) {
            case BOLD -> StyleConstants.isBold(attrs);
            case ITALIC -> StyleConstants.isItalic(attrs);
            case UNDERLINE -> StyleConstants.isUnderline(attrs);
            case STRIKE -> StyleConstants.isStrikeThrough(attrs);
        };
    }

    public TextFormatterView getView() {
        return view;
    }
}
