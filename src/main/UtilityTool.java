package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UtilityTool {

    GamePanel gp;
    public UtilityTool (GamePanel gp) {
        this.gp = gp;
    }
    // Image Scaling
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }
    public BufferedImage setup(String imagePath) {

        BufferedImage image = null;

        try {

            image = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
            image = scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public int getScreenCenterX(Graphics2D g2, String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2; 
    }
    public int getScreenCenterY(Graphics2D g2, String text) {
        // int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
        return gp.screenHeight/2; 
    }

    public void drawText(Graphics2D g2, String text, int X, int Y, int style, float size, Color color, boolean shadow, boolean centerX, boolean centerY, boolean drawSelection) {
        g2.setFont(g2.getFont().deriveFont(style, size));
        int x = X;
        int y = Y;
        if (centerX) 
            x = getScreenCenterX(g2, text);
        if (centerY) 
            y = getScreenCenterY(g2, text);
        // draw shadow if required
        if (shadow) {
            g2.setColor(Color.black);
            g2.drawString(text, (int)(x*1.011), (int)(y*1.011));
        }
        g2.setColor(color);
        g2.drawString(text, x, y);

        if (drawSelection) {
            g2.drawString(">", x-gp.tileSize, y);
            g2.drawString("<", x+(int)g2.getFontMetrics().getStringBounds(text, g2).getWidth()+gp.tileSize-20, y);
        }

    }
}
