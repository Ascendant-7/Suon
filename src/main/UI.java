package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageDuration = 0;
    BufferedImage background;
    public int commandNum = 0;
    public int gameSubState = 0;

    public UI(GamePanel gp) {

        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

            background = ImageIO.read(getClass().getResourceAsStream("/backgrounds/artwork_fullscreen.png"));
            keyImage = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    } 
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawGameScreen();
        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
    }
    public void drawTitleScreen() {
        // MAIN MENU
        if (gameSubState == 0) {
            // BACKGROUND
            g2.drawImage(background, 0, 0, null);
    
            // MAIN TITLE
            int x = 0, y = gp.tileSize*3;
            gp.uTool.drawText(g2, "SUON", x, y, Font.BOLD, 86F, Color.white, true, true, false);

            // Subtitle 1
            x = gp.tileSize;
            y = gp.tileSize;
            gp.uTool.drawText(g2, "Greetings, player! Welcome to...", x, y, Font.BOLD, 25F, Color.white, true, false, false);

            // Subtitle 2
            x = gp.tileSize*9;
            y = gp.tileSize*4;
            gp.uTool.drawText(g2, "Maze Game", x, y, Font.BOLD, 25F, Color.white, true, false, false);
    
            // PLAYER IMAGE
            // x += gp.tileSize;
            // y = gp.tileSize + 10;
            // g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
    
            // MENU
            y = gp.tileSize*6;
            gp.uTool.drawText(g2, "NEW GAME", x, y, Font.BOLD, 40F, Color.white, true, true, commandNum == 0);

            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "LOAD GAME", x, y, Font.BOLD, 40F, Color.white, true, true, commandNum == 1);
            
            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "RULES & TUTORIAL", x, y, Font.BOLD, 40F, Color.white, true, true, commandNum == 2);
            
            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "OPTIONS", x, y, Font.BOLD, 40F, Color.white, true, true, commandNum == 3);
    
            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "QUIT", x, y, Font.BOLD, 40F, Color.white, true, true, commandNum == 4);
        }
        else if (gameSubState == 1) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            int x = 0, y = gp.tileSize*3;
            gp.uTool.drawText(g2, "Select your character.", x, y, Font.BOLD, 42, Color.white, false, true, false);

            y += gp.tileSize*3;
            gp.uTool.drawText(g2, "Don", x, y, Font.BOLD, 42F, Color.white, false, true, commandNum == 0);
            
            y += gp.tileSize;
            gp.uTool.drawText(g2, "Emma", x, y, Font.BOLD, 42F, Color.white, false, true, commandNum == 1);
            
            y += gp.tileSize*2;
            gp.uTool.drawText(g2, "Back", x, y, Font.BOLD, 42F, Color.white, false, true, commandNum == 2);
        }
        
    }
    public void drawGameScreen() {
        if (gameSubState == 0) {

            // CONTAINER AND BAR DIMENSIONS
            int contX, contY, contW, contH;
            int barX, barY, barW, barH;

            // HEALTH BAR
            // health container
            g2.setColor(Color.gray);
            contX = gp.tileSize/2;
            contY = gp.tileSize/2;
            contW = gp.tileSize*4+10;
            contH = gp.tileSize/2+10;
            g2.fillRect(contX, contY, contW, contH);
            // health container fill = health bar
            g2.setColor(Color.darkGray);
            barX = contX+5;
            barY = contY+5;
            barW = contW-10;
            barH = contH-10;
            g2.fillRect(barX, barY, barW, barH);
            // health points
            g2.setColor(Color.red);
            g2.fillRect(barX, barY, (int)(barW * (gp.player.life/100)), barH);

            // STAMINA BAR
            // stamina container
            g2.setColor(Color.gray);
            contY = gp.tileSize+20;
            g2.fillRect(contX, contY, contW, contH);
            // stamina container fill = stamina bar
            g2.setColor(Color.darkGray);
            barX = contX+5;
            barY = contY+5;
            barW = contW-10;
            barH = contH-10;
            g2.fillRect(barX, barY, barW, barH);
            // stamina points
            g2.setColor(Color.yellow);
            g2.fillRect(barX, barY, (int)(barW * (gp.player.stamina/100)), barH);
            // KEYS
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));
            g2.drawImage(keyImage, gp.tileSize/2, gp.screenHeight - gp.tileSize, gp.tileSize, gp.tileSize, null);
            g2.drawString("x "+gp.player.hasKey, gp.tileSize+20, gp.screenHeight-10);
    
            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
                messageDuration++;
                if (messageDuration > 120) {
                    messageDuration = 0;
                    messageOn = false;
                } 
            }

            // DEBUG
            if (gp.keyH.debugMode) {
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString("Speed: "+gp.player.speed, gp.screenWidth/2, gp.screenHeight-10);
                g2.drawString("World("+gp.player.worldX/48+", "+gp.player.worldY/48+")", gp.screenWidth/2, gp.screenHeight-40);
                g2.drawString("fatigued: "+gp.player.fatigued, gp.screenWidth/2, gp.screenHeight-70);
                g2.drawString("idle: "+gp.player.idle, gp.screenWidth/2, gp.screenHeight-100);
            }
        }
        else if (gameSubState == 1) {

            int x = 0, y = gp.screenHeight/2 - (gp.tileSize*3);
            gp.uTool.drawText(g2, "You found the final key!", x, y, Font.PLAIN, 40F, Color.white, false, true, false);
            
            y += gp.tileSize*5;
            gp.uTool.drawText(g2, "Congratulations!", x, y, Font.BOLD, 80F, Color.yellow, false, true, false);
    
            y += gp.tileSize*2;
            gp.uTool.drawText(g2, "---Press ENTER to go back to menu---", x, y, Font.PLAIN, 32F, Color.white, false, true, false);
        }
    }
    public void drawPauseScreen() {
        gp.uTool.drawText(g2, "PAUSED", 0, gp.screenHeight/2, Font.PLAIN, 80F, Color.white, false, true, false);
    }
}
