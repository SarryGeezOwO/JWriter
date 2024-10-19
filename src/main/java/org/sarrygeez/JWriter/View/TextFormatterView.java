package org.sarrygeez.JWriter.View;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Controller.TextFormatterController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Widget.ImageButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private Color defaultCol;
    private Color activeCol;

    private final String[] headingLevels = {
            " Normal text", " Heading 1", " Heading 2", " Heading 3", " Heading 4"};
    private final JComboBox<String> box = new JComboBox<>(headingLevels);

    @Override
    public void applyTheme(Theme theme) {
        main.setBackground(Color.decode(theme.getColor("primary")));
        dummy.setBackground(Color.decode(theme.getEditor("lineCountBackground")));
        ComponentDecorator.addBorder(new Insets(1, 1, 1, 1), main, theme);

        defaultCol = Color.decode(theme.getColor("primary"));
        activeCol = Color.decode(theme.getColor("accent"));

        UIManager.put("ComboBox.selectionBackground", Color.decode(theme.getColor("secondary")));
        UIManager.put("ComboBox.selectionForeground", Color.decode(theme.getColor("primary")));
        UIManager.put("Separator.foreground", Color.decode(theme.getColor("border")));

        box.putClientProperty(FlatClientProperties.STYLE,
            "buttonStyle: button" +
            ";background:" + theme.getColor("accent") +
            ";foreground:" + theme.getColor("text") +
            ";buttonEditableBackground:" + theme.getColor("accent") +
            ";buttonArrowColor:" + theme.getColor("text"));
        box.revalidate();
    }

// UI design goal can be seen in: root/misc/textFormatUI.png
    public TextFormatterView(TextFormatterController controller) {
        UIManager.put("Separator.height", 8);
        UIManager.put("Separator.stripeWidth", 2);

        this.controller = controller;
        setPreferredSize(new Dimension(0, 35));
        setLayout(new BorderLayout());
        dummy.setPreferredSize(new Dimension(50, 0));

        box.setBorder(new EmptyBorder(0, 5, 0, 5));
        box.addItemListener(e -> {
            // TODO: Implement this
            if (box.getSelectedItem() == e.getItem()) {
                System.out.println("Change Heading -> " + e.getItem().toString());
                controller.getEditorController().requestFocus();
            }
        });
        initButtons();

        main.add(box, "growY, gapTop 5, gapBottom 5");
        insertSeparator();
        insertButton(boldButton,      0);
        insertButton(italicButton,    1);
        insertButton(underlineButton, 2);
        insertButton(strikeButton,    3);
        insertSeparator();

        add(dummy, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
    }

    private void initButtons() {
        boldButton.addActionListener(e      -> controller.setStyle(BOLD));
        italicButton.addActionListener(e    -> controller.setStyle(ITALIC));
        underlineButton.addActionListener(e -> controller.setStyle(UNDERLINE));
        strikeButton.addActionListener(e    -> controller.setStyle(STRIKE));
    }

    private void insertButton(ImageButton btn, int index) {
        main.add(btn, "grow");
        controller.getEditorController().getApp().getThemeManager().registerComponent(btn);
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

    private void insertSeparator() {
        JSeparator separator = new JSeparator();
        separator.setOrientation(JSeparator.VERTICAL);

        main.add(separator, "grow, gap 5, gapTop 7, gapBottom 7");
    }
}
