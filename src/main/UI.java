package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.KeyObject;

public class UI {

    GamePanel gp;
    Font arial_40, arial_80B;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageDuration = 0;
    public boolean gameFinished;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {

        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        KeyObject key = new KeyObject(gp);
        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    } 
    public void draw(Graphics2D g2) {

        if (gameFinished) {

            g2.setFont(arial_40);
            g2.setColor(Color.white);

            String text;
            int textLength, x, y;

            text = "You found the final key!";
            // get the font metric (font size in px) -> get string bound (string metric in px) -> get its width
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text, x, y);

            text = "Your Time is: "+dFormat.format(playTime) + "!";
            // get the font metric (font size in px) -> get string bound (string metric in px) -> get its width
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*4);
            g2.drawString(text, x, y);
            
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            
            text = "Congratulations!";
            // get the font metric (font size in px) -> get string bound (string metric in px) -> get its width
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text, x, y);

            gp.gameThread = null;
            return;
        }

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x "+gp.player.hasKey, 74, 65);

        // g2.drawString("Coordinates: ("+gp.player.worldX/gp.tileSize+", "+gp.player.worldY/gp.tileSize+")", gp.tileSize/2, gp.screenHeight-25);

        // TIME
        playTime += (double)1/60;
        g2.drawString("Time:"+dFormat.format(playTime), gp.tileSize*11, 65);

        // MESSAGE
        if (messageOn) {

            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize/2, gp.tileSize*5);

            messageDuration++;

            if (messageDuration > 120) {
                messageDuration = 0;
                messageOn = false;
            } 
        }
    }
}
