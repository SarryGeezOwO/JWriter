package org.sarrygeez.JWriter.View;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Controller.EditorController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Widget.ImageButton;

import javax.swing.*;
import java.awt.*;

public class TextFormatterView extends JPanel implements ThemedComponent{

    private final EditorController controller;
    private final JPanel main = new JPanel(new MigLayout("insets 3, gap 2, fillY"));
    private final JPanel dummy = new JPanel();

    private final ImageButton boldButton = new ImageButton("bold", 20);
    private final ImageButton italicButton = new ImageButton("italic", 20);
    private final ImageButton underlineButton = new ImageButton("underline", 20);

    @Override
    public void applyTheme(Theme theme) {
        main.setBackground(Color.decode(theme.getColor("primary")));
        dummy.setBackground(Color.decode(theme.getEditor("lineCountBackground")));
        ComponentDecorator.addBorder(new Insets(1, 1, 1, 1), main, theme);
    }

// UI design goal can be seen in: root/misc/textFormatUI.png
    public TextFormatterView(EditorController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(0, 38));
        setLayout(new BorderLayout());

        dummy.setPreferredSize(new Dimension(50, 0));

        insertSeparator();
        insertButton(boldButton);
        insertButton(italicButton);
        insertButton(underlineButton);
        insertSeparator();

        add(dummy, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
    }

    private void insertButton(ImageButton btn) {
        main.add(btn);
        controller.getApp().getThemeManager().registerComponent(btn);
    }

    private void insertSeparator() {
        JSeparator separator = new JSeparator();
        separator.setOrientation(JSeparator.VERTICAL);

        main.add(separator, "grow, gap 5, gapTop 5, gapBottom 5");
    }
}
