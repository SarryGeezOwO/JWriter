package org.sarrygeez.JWriter.View;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Controller.TextFormatterController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Widget.ColorChooser;
import org.sarrygeez.JWriter.Widget.ImageButton;

import javax.swing.*;
import java.awt.*;

import static org.sarrygeez.JWriter.Controller.TextFormatterController.Styles.*;

public class TextFormatterView extends JPanel implements ThemedComponent{

    private final TextFormatterController controller;
    private final JPanel main = new JPanel(new MigLayout("insets 0 7 0 7, gap 2, fillY"));
    private final JPanel dummy = new JPanel();

    private final ImageButton boldButton = new ImageButton("bold", 20);
    private final ImageButton italicButton = new ImageButton("italic", 20);
    private final ImageButton underlineButton = new ImageButton("underline", 20);
    private final ImageButton strikeButton = new ImageButton("strike", 22);
    private final boolean[] isButtonActive = {false, false, false, false}; // Yes, this shit is primitive af

    private final ColorChooser highlightColorChooser = new ColorChooser("Text Highlight Color");
    private final ImageButton helpButton = new ImageButton("question", 20);
    private final ImageButton overviewButton = new ImageButton("Overview", 22);

    private Color defaultCol;
    private Color activeCol;

    @Override
    public void applyTheme(Theme theme) {
        main.setBackground(Color.decode(theme.getColor("primary")));
        dummy.setBackground(Color.decode(theme.getEditor("lineCountBackground")));
        ComponentDecorator.addBorder(new Insets(1, 1, 1, 0), main, theme);

        defaultCol = Color.decode(theme.getColor("primary"));
        activeCol = Color.decode(theme.getColor("accent"));
    }

// UI design goal can be seen in: root/misc/textFormatUI.png
    public TextFormatterView(TextFormatterController controller) {
        UIManager.put("Separator.height", 8);
        UIManager.put("Separator.stripeWidth", 2);

        this.controller = controller;
        setPreferredSize(new Dimension(0, 35));
        setLayout(new BorderLayout());
        dummy.setPreferredSize(new Dimension(50, 0));
        initButtons();

        insertSeparator();
        insertToggleButton(boldButton,      0, 'b');
        insertToggleButton(italicButton,    1, 'i');
        insertToggleButton(underlineButton, 2, 'u');
        insertToggleButton(strikeButton,    3, 's');
        insertSeparator();
        main.add(highlightColorChooser, "grow, width 20:20:20, height 20:20:20");
        insertSeparator();
        insertButton(overviewButton, 'o');
        insertButton(helpButton, 'h');

        add(dummy, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
    }

    private void initButtons() {
        boldButton.addActionListener(e      -> controller.setStyle(BOLD));
        italicButton.addActionListener(e    -> controller.setStyle(ITALIC));
        underlineButton.addActionListener(e -> controller.setStyle(UNDERLINE));
        strikeButton.addActionListener(e    -> controller.setStyle(STRIKE));
        overviewButton.addActionListener(e  -> controller.toggleOverviewView());
        helpButton.addActionListener(e      -> System.out.println("Hello world..."));
    }

    private void insertButton(ImageButton btn, char mm) {
        main.add(btn, "grow");
        controller.getEditorController().getApp().getThemeManager().registerComponent(btn);
        btn.setMnemonic(mm);
        btn.addActionListener(e -> controller.getEditorController().requestFocus());
    }

    private void insertToggleButton(ImageButton btn, int index, char mm) {
        main.add(btn, "grow");
        controller.getEditorController().getApp().getThemeManager().registerComponent(btn);
        btn.setMnemonic(mm);
        btn.addActionListener(e -> {
            // Transfer focus to editor, and set isActive value
            isButtonActive[index] = !isButtonActive[index];
            controller.getEditorController().requestFocus();

            // Change UI color ig
            if(!isButtonActive[index]) {
                btn.setBackground(defaultCol);
                return;
            }
            btn.setBackground(activeCol);
        });
    }

    public ColorChooser GetHighlightColorChooser() {
        return highlightColorChooser;
    }

    private void insertSeparator() {
        JSeparator separator = new JSeparator();
        separator.setOrientation(JSeparator.VERTICAL);

        main.add(separator, "grow, gap 5, gapTop 7, gapBottom 7");
    }
}
