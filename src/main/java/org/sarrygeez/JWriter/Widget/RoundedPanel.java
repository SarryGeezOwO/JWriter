package org.sarrygeez.JWriter.Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel{

    private  int topLeft = 0;
    private  int topRight = 0;
    private  int bottomRight = 0;
    private  int bottomLeft = 0;

    @SuppressWarnings("unused")
    public RoundedPanel() {
        setOpaque(false);
        setBorder(null);
    }

    @SuppressWarnings("unused")
    public RoundedPanel(int topLeft, int topRight, int bottomRight, int bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
        setOpaque(false);
        setBorder(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(getBackground());

        Area area = new Area(createRoundTopLeft());
        if(topRight > 0) {
            area.intersect(new Area(createRoundTopRight()));
        }
        if(bottomRight > 0) {
            area.intersect(new Area(createRoundBottomRight()));
        }
        if(bottomLeft > 0) {
            area.intersect(new Area(createRoundBottomLeft()));
        }
        g2D.fill(area);

        g2D.dispose();
        super.paintComponent(g);
    }

    private Shape createRoundTopLeft() {
        int width = getWidth();
        int height = getHeight();
        int roundX = Math.min(width, topLeft);
        int roundY = Math.min(height, topLeft);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double((double) roundX /2, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, (double) roundY / 2, width, height - (double) roundY / 2)));
        return area;
    }

    private Shape createRoundTopRight() {
        int width = getWidth();
        int height = getHeight();
        int roundX = Math.min(width, topRight);
        int roundY = Math.min(height, topRight);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double(0, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, (double) roundY / 2, width, height - (double) roundY / 2)));
        return area;
    }

    private Shape createRoundBottomLeft() {
        int width = getWidth();
        int height = getHeight();
        int roundX = Math.min(width, bottomLeft);
        int roundY = Math.min(height, bottomLeft);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double((double) roundX / 2, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, 0, width, height - (double) roundY / 2)));
        return area;
    }

    private Shape createRoundBottomRight() {
        int width = getWidth();
        int height = getHeight();
        int roundX = Math.min(width, bottomRight);
        int roundY = Math.min(height, bottomRight);

        Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, roundX, roundY));
        area.add(new Area(new Rectangle2D.Double(0, 0, width - (double) roundX / 2, height)));
        area.add(new Area(new Rectangle2D.Double(0, 0, width, height - (double) roundY / 2)));
        return area;
    }

}
