package org.dedee.wopper3.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;


public abstract class BaseCanvas extends Canvas {


    public abstract void paint(GeneralPath gp);

    public abstract void paintLSystemText(Graphics2D g2);

    public abstract void paintRecursionLevel(Graphics2D g2);

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);

        g2.setColor(Color.lightGray);
        paintLSystemText(g2);
        paintRecursionLevel(g2);
        g2.setColor(Color.black);

        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        paint(gp);

        // Resize to fit
        Rectangle2D boundsPath = gp.getBounds2D();
        Rectangle boundsCanvas = getBounds();

        double borderPerCent = 0.05;
        double bx = boundsPath.getWidth() * borderPerCent;
        double by = boundsPath.getHeight() * borderPerCent;

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
        return new Dimension(500, 400);
    }
}
