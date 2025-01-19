package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import object.KeyObject;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageDuration = 0;
    public boolean gameFinished;
    BufferedImage background;
    public int commandNum = 0;

    public UI(GamePanel gp) {

        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

            background = ImageIO.read(getClass().getResourceAsStream("/backgrounds/background_dark.png"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KeyObject key = new KeyObject(gp);
        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    } 
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        // PLAY STATE
        if (gp.gameState == gp.playState) {

        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
    }
    public void drawTitleScreen() {
        // BACKGROUND
        g2.drawImage(background, 0, 0, null);

        // TITLE NAME
        // Main Title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 86F));
        String text = "SUON";
        int x = getScreenCenterX(text);
        int y = gp.tileSize*3;
        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+5, y+5);
        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        // Subtitle 1
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
        text = "Greetings, player! Welcome to...";
        x = gp.tileSize;
        y = gp.tileSize;
        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+5, y+5);
        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        // Subtitle 2
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
        text = "Maze Game";
        x = gp.tileSize*9;
        y = gp.tileSize*4;
        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+5, y+5);
        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // PLAYER IMAGE
        x += gp.tileSize;
        y = gp.tileSize + 10;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        text = "NEW GAME";
        x = getScreenCenterX(text);
        y = gp.tileSize*6;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        text = "LOAD GAME";
        x = getScreenCenterX(text);
        y += gp.tileSize + 10;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-gp.tileSize, y);
        }
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        text = "RULES & TUTORIAL";
        x = getScreenCenterX(text);
        y += gp.tileSize + 10;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x-gp.tileSize, y);
        }
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        text = "OPTIONS";
        x = getScreenCenterX(text);
        y += gp.tileSize + 10;
        g2.drawString(text, x, y);
        if (commandNum == 3) {
            g2.drawString(">", x-gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        text = "QUIT";
        x = getScreenCenterX(text);
        y += gp.tileSize + 10;
        g2.drawString(text, x, y);
        if (commandNum == 4) {
            g2.drawString(">", x-gp.tileSize, y);
        }
    }
    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int y = gp.screenHeight/2;
        int x = getScreenCenterX(text);
        g2.drawString(text, x, y);
    }

    public int getScreenCenterX(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2; 
    }
}
