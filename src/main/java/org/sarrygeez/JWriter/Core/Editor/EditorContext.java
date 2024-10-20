package org.sarrygeez.JWriter.Core.Editor;

@SuppressWarnings("unused")
public class EditorContext {

    private static EditorEnum.CaretType caretType = EditorEnum.CaretType.BLOCK;
    private static EditorEnum.Caret caretBehaviour = EditorEnum.Caret.BLINKING;
    private static int defaultFontSize = 16;
    private static float defaultLineSpace = 3f;

    private EditorContext() {}

    public static EditorEnum.CaretType getCaretType() {
        return caretType;
    }

    public static void setCaretType(EditorEnum.CaretType caretType) {
        EditorContext.caretType = caretType;
    }

    public static EditorEnum.Caret getCaretBehaviour() {
        return caretBehaviour;
    }

    public static void setCaretBehaviour(EditorEnum.Caret caretBehaviour) {
        EditorContext.caretBehaviour = caretBehaviour;
    }

    public static int getDefaultFontSize() {
        return defaultFontSize;
    }

    public static void setDefaultFontSize(int defaultFontSize) {
        EditorContext.defaultFontSize = defaultFontSize;
    }

    public static float getDefaultLineSpace() {
        return defaultLineSpace;
    }

    public static void setDefaultLineSpace(float defaultLineSpace) {
        EditorContext.defaultLineSpace = defaultLineSpace;
    }
}
