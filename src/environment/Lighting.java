package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Shape;
import main.GamePanel;

public class Lighting {

    GamePanel gp;
    BufferedImage darknessFilter;

    public Lighting(GamePanel gp, int circleSize) {

        // CREATE A BUFFERED IMAGE
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();

        // CREATE AN AREA OBJECT TO EDIT
        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

        // INITIALIZE THE CENTER COORDINATES
        int centerX = gp.screenWidth/2;
        int centerY = gp.screenHeight/2;

        // FIND THE DRAW COORDINATES OF THE LIGHT CIRCLE
        double x = centerX - circleSize/2;
        double y = centerY - circleSize/2;

        // CREATE THE LIGHT CIRCLE SHAPE
        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);

        // CREATE THE LIGHT CIRCLE AREA
        Area lightArea = new Area(circleShape);

        // SHAPE SUBTRACTION
        screenArea.subtract(lightArea);

        // CREATE GRADATION EFFECT
        Color[] colors = new Color[12];
        float[] fractions = new float[12];

        float alpha = 0.1f, degradation = 0f;
        for (int i = 0; i < colors.length; i++) {
            if (alpha > 1) alpha = 1;
            colors[i] = new Color(0, 0, 0, alpha);
            if (i == 0)
                alpha += 0.32f;
            else if (i < colors.length-2) {
                alpha += 0.1 - degradation;
                degradation+=0.01;
            }
            else 
                alpha += 0.02;
        }

        alpha = 0f;
        for (int i = 0; i < fractions.length; i++) {
            if (alpha > 1) alpha = 1;
            fractions[i] = alpha;
            if (i == 0)
                alpha += 0.4f;
            else if (i < 3)
                alpha += 0.1f;
            else 
                alpha += 0.05f;
        }

        // GRADIENT PAINT SETTING
        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, circleSize/2, fractions, colors);

        // SET THE GRADIENT ON THE GRAPHICS
        g2.setPaint(gPaint);

        // DRAW THE LIGHT CIRCLE GRADIENT
        g2.fill(lightArea);

        // DRAW THE SUBTRACTED AREA TO THE GRAPHICS
        // g2.setColor(new Color(0, 0, 0, 0.95f));
        g2.fill(screenArea);
        g2.dispose();
    }

    public void draw(Graphics2D g2) {
        
        g2.drawImage(darknessFilter, 0, 0, null);
    }
}
