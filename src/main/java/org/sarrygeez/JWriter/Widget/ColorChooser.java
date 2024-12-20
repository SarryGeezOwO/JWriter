package org.sarrygeez.JWriter.Widget;

import net.miginfocom.swing.MigLayout;
import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A button that displays its current color
 * and invokes a popup when clicked to choose a color
 */
public class ColorChooser extends RoundedPanel implements ThemedComponent {

    private final String label;
    private final JColorChooser chooser = new JColorChooser();
    private final JTextField hexField = new JTextField(7);
    private final List<ColorPickerListener> listeners = new ArrayList<>();

    @Override
    public void applyTheme(Theme theme) {
        hexField.setBackground(Color.decode(theme.getColor("accent")));
        chooser.setBackground(Color.decode(theme.getColor("primary")));
    }

    public void addListener(ColorPickerListener listener) {
        listeners.add(listener);
    }

    public ColorChooser(String label) {
        super(5, 5, 5, 5);
        this.label = label;

        setPreferredSize(new Dimension(35, 35));
        setBackground(Color.GRAY);

        chooser.setPreviewPanel(new JPanel());
        chooser.setColor(getBackground()); // Default ahh shit

        ColorChooser invoker = this;
        chooser.getSelectionModel().addChangeListener(e -> {
            Color color = chooser.getColor();
            setBackground(color);
            hexField.setText(String.format("#%06X", color.getRGB() & 0xFFFFFF));

            // Do all listeners request
            for(ColorPickerListener listener : listeners) {
                listener.onColorChanged(color);
            }
        });

        hexField.setBorder(new EmptyBorder(6, 6, 6, 6));
        hexField.setText(String.format("#%06X", getBackground().getRGB() & 0xFFFFFF));
        hexField.addActionListener(e -> {
            Color color;
            try {
                color = Color.decode(hexField.getText());
            }catch (NumberFormatException ex) {
                color = Color.WHITE;
            }
            chooser.getSelectionModel().setSelectedColor(color);
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setPopupSize(315, 330);
                popup.setLayout(new MigLayout("FillX, Insets 15, gap 5"));

                // Remove all default color chooser panels
                popup.setBackground(chooser.getBackground());
                AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
                for (AbstractColorChooserPanel panel : panels) {
                    if(panel.getDisplayName().equals("HSV")) {
                        panel.setBackground(chooser.getBackground());
                        removeComponents(panel);
                        continue;
                    }
                    chooser.removeChooserPanel(panel);
                }

                attachComponents(popup);
                popup.revalidate();
                popup.pack();
                popup.show(invoker, 0, getHeight() + 10);
            }
        });
    }

    private void attachComponents(Container root) {
        root.add(new JLabel(label), "span, grow, wrap");
        root.add(chooser, "grow, span, gapLeft 5, wrap");
        root.add(new JLabel("Hex:"), "grow");
        root.add(hexField, "grow, gapRight 5");
    }

    private void removeComponents(Container container) {
        for (Component component : container.getComponents()) {
            if  (
                component instanceof JSlider ||
                component instanceof JRadioButton ||
                component instanceof JSpinner ||
                component instanceof JLabel ||
                component instanceof JFormattedTextField
                )
            {
                container.remove(component);
            }
            else if (component instanceof Container) {
                removeComponents((Container) component);
            }
        }
    }

    public Color getColor() {
        return getBackground();
    }
}
