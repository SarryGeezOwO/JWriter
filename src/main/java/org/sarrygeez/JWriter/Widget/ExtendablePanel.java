package org.sarrygeez.JWriter.Widget;

import org.sarrygeez.JWriter.Core.Theme;
import org.sarrygeez.JWriter.View.ThemedComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.sarrygeez.JWriter.Widget.WidgetEnum.*;

public class ExtendablePanel extends JPanel implements ThemedComponent {

    private final JPanel dragPane = new JPanel();
    private final Direction direction;
    private final int min, max;
    private JPanel contentPane;
    private final List<ExtendableWidgetListener> listeners = new ArrayList<>();

    @Override
    public void applyTheme(Theme theme) {
        /* Override this to other class whatever you want */

        // By default, it has a styling
        Color primary = Color.decode(theme.getColor("primary"));
        contentPane.setBackground(primary);
        dragPane.setBackground(primary);
        setBackground(primary);
    }

    @SuppressWarnings("unused")
    public void addExtendableWidgetListener(ExtendableWidgetListener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unused")
    public ExtendablePanel(Direction direction) {
        this.direction = direction;
        this.min = 100;
        this.max = 3000;
    }

    @SuppressWarnings("unused")
    public ExtendablePanel(Direction direction, int min, int max) {
        this.direction = direction;
        this.min = min;
        this.max = max;
        initComponent();
    }

    private void initComponent() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        setDoubleBuffered(true);
        dragPane.setPreferredSize(new Dimension(4, 4));

        initMouseEvents();
        setLayout(new BorderLayout());

        determineDragPosition();
        add(contentPane, BorderLayout.CENTER);
    }

    private void determineDragPosition() {
        switch (direction) {
            case TOP -> add(dragPane, BorderLayout.NORTH);
            case BOTTOM -> add(dragPane, BorderLayout.SOUTH);
            case RIGHT -> add(dragPane, BorderLayout.EAST);
            case LEFT -> add(dragPane, BorderLayout.WEST);
        }
    }

    private int getCursorType(Direction direction) {
        return switch (direction) {
            case TOP -> Cursor.N_RESIZE_CURSOR;
            case BOTTOM -> Cursor.S_RESIZE_CURSOR;
            case RIGHT -> Cursor.E_RESIZE_CURSOR;
            case LEFT -> Cursor.W_RESIZE_CURSOR;
        };
    }

    @SuppressWarnings("all")
    private void initMouseEvents() {
        dragPane.setCursor(Cursor.getPredefinedCursor(
                getCursorType(this.direction)));

        JPanel pane = this;
        dragPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newValue = (direction == Direction.TOP || direction == Direction.BOTTOM
                        ?
                        calculateNewHeight(pane, min, max, (direction == Direction.TOP))
                        :
                        calculateNewWidth(pane, min, max, (direction == Direction.LEFT))
                        );
                if(newValue == max || newValue == min) {
                    return;
                }

                // Modify Panels property { Scale: Width }
                setPreferredSize((direction == Direction.TOP || direction == Direction.BOTTOM) ?
                        new Dimension(getWidth(), newValue)
                    :
                        new Dimension(newValue, getHeight())
                );
                revalidate();

                for(ExtendableWidgetListener listener : listeners) {
                    listener.onResized();
                }
            }
        });
    }

    private int calculateNewWidth(JComponent source, int min, int max, boolean isCompStart) {
        Point mouse = getMousePosition(source);

        // Get distance of mouse from this panel
        int distance;
        if(isCompStart) {
            distance = (int)mouse.getX() - dragPane.getX();
        }
        else {
            distance = (int)mouse.getX() - (dragPane.getX() + getHeight());
        }

        // Flip distance, and calculate new width
        distance *= -1;
        int newWidth = getWidth() + distance;

        // Clamp newWidth
        return Math.clamp(newWidth, min, max);
    }

    private int calculateNewHeight(JComponent source, int min, int max, boolean isCompStart) {
        Point mouse = getMousePosition(source);

        // Get distance of mouse from this panel
        int distance;
        if(isCompStart) {
            distance = (int)mouse.getY() - dragPane.getY();
        }
        else {
            distance = (int)mouse.getY() - (dragPane.getY() + getHeight());
        }

        // Flip distance, and calculate new Height
        distance *= -1;
        int newHeight = getHeight() + distance;

        // Clamp newWidth
        return Math.clamp(newHeight, min, max);
    }

    private Point getMousePosition(JComponent comp) {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouse, comp);
        return mouse;
    }

    @SuppressWarnings("unused")
    public void setContentPane(JPanel contentPane) {
        remove(contentPane);
        this.contentPane = contentPane;
        add(contentPane);
        revalidate();
        repaint();
    }

    @SuppressWarnings("unused")
    public JPanel getContentPane() {
        return contentPane;
    }
}
