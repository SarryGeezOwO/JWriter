package org.sarrygeez.JWriter.View;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Controller.HeaderController;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;
import org.sarrygeez.JWriter.Widget.RoundedPanel;
import org.sarrygeez.JWriter.Widget.TextView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderView extends JPanel implements ThemedComponent{

    private final JPanel sidebarHeader;
    private final JPanel noteHeader;

    private final JTextField titleField = new JTextField();
    private final TextView dateCreated = new TextView(true);
    private final HeaderController controller;

    @Override
    public void applyTheme(Theme theme) {
        sidebarHeader.setBackground(Color.decode(theme.getColor("accent")));

        Color primary = Color.decode(theme.getColor("primary"));
        this.setBackground(primary);
        noteHeader.setBackground(primary);
        titleField.setBackground(primary);
    }

    public HeaderView(HeaderController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(0, 100));
        setLayout(new BorderLayout());

        initTitleField();
        initDateCreated();

        sidebarHeader = new RoundedPanel(0, 30, 0, 0);
        noteHeader = new JPanel();

        noteHeader.setLayout(new MigLayout("fillX, insets 10, gap 0"));
        noteHeader.add(titleField, "span, grow");
        noteHeader.add(dateCreated, "span, grow");

        sidebarHeader.setPreferredSize(new Dimension(310, 0));
        add(sidebarHeader, BorderLayout.WEST);
        add(noteHeader, BorderLayout.CENTER);
    }

    private void initTitleField() {
        titleField.setBorder(new EmptyBorder(10, 0, 10, 0));
        titleField.setFont(new Font(
                FontLoader.appFontsFamily.get(1), Font.BOLD, 24));

        titleField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Title...");
        titleField.addActionListener(e -> controller.transferFocus());
    }

    private void initDateCreated() {
        dateCreated.setText("October, 26, 2024");
        dateCreated.setFontSize(16);
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public TextView getDateCreated() {
        return dateCreated;
    }
}
