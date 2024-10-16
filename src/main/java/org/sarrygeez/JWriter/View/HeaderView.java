package org.sarrygeez.JWriter.View;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.Core.Utils.ComponentDecorator;
import org.sarrygeez.JWriter.Core.Utils.FontLoader;
import org.sarrygeez.JWriter.Core.Utils.ImageLoader;
import org.sarrygeez.JWriter.Widget.RoundedPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderView extends JPanel implements ThemedComponent{

    private final JPanel sidebarHeader;
    private final JPanel noteHeader;

    private final JTextField titleField = new JTextField();
    private final JLabel dateCreated = new JLabel();

    @Override
    public void applyTheme(Theme theme) {
        sidebarHeader.setBackground(Color.decode(theme.getColor("accent")));

        Color primary = Color.decode(theme.getColor("primary"));
        this.setBackground(primary);
        noteHeader.setBackground(primary);
        titleField.setBackground(primary);
        ComponentDecorator.addBorder(new Insets(0,0,2,0), noteHeader, theme);
    }

    public HeaderView() {
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
                FontLoader.appFontsFamily.get(1), Font.BOLD, 20));

        titleField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Title...");
        titleField.addActionListener(e -> {
            titleField.transferFocus();
            // Save changes to disc???
        });
    }

    private void initDateCreated() {
        dateCreated.setText("October, 26, 2024");
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public JLabel getDateCreated() {
        return dateCreated;
    }
}
