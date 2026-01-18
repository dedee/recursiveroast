package org.dedee.recursiveroast.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;


public abstract class BaseCanvas extends Canvas {


    public abstract void paint(GeneralPath gp);

    public abstract void paintLSystemText(Graphics2D g2);

    public abstract void paintRecursionLevel(Graphics2D g2);

    private static final double BORDER_PERCENTAGE = 0.05;
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);

        g2.setColor(Color.LIGHT_GRAY);
        paintLSystemText(g2);
        paintRecursionLevel(g2);
        g2.setColor(Color.BLACK);

        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        paint(gp);

        // Resize to fit
        Rectangle2D boundsPath = gp.getBounds2D();
        Rectangle boundsCanvas = getBounds();

        double bx = boundsPath.getWidth() * BORDER_PERCENTAGE;
        double by = boundsPath.getHeight() * BORDER_PERCENTAGE;

        double zoomX = boundsCanvas.getWidth()
                / (boundsPath.getWidth() + bx * 2);
        double zoomY = boundsCanvas.getHeight()
                / (boundsPath.getHeight() + by * 2);
        double zoom = Math.min(zoomX, zoomY);

        double dx = -boundsPath.getMinX() + bx;
        double dy = -boundsPath.getMinY() + by;

        gp.transform(AffineTransform.getTranslateInstance(dx, dy));
        g2.scale(zoom, zoom);
        g2.draw(gp);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
