package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    public int commandNum = 0, optionNum = -1, settingNum = 0;
    public int gameSubState = 0;
    
    int visibleDuration = 60;
    int loadedMessages = 0;
    int loopCount = 0;
    float transparency = 0f;
    float progress = 0f;
    int dots = 0;

    String[] options = {"Fullscreen", "Sounds", "Control", "Back to Menu"};

    boolean fullscreen = true;

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

        // LOADING STATE
        if (gp.gameState == gp.loadingState) {
            drawLoadingScreen();
        }
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
            drawOptionsScreen();
        }
    }
    public void drawLoadingScreen() {
        // FIRST LOADING SCREEN - ENTERING THE GAME
        if (gameSubState == 0) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            switch (loadedMessages) {
                case 0:
                    gp.uTool.drawText(g2, "SUON", 0, 0, Font.BOLD, 100F, Color.white, false, 1, true, false, false);
                    fade();
                    break;
                case 1:
                    gp.uTool.drawText(g2, "Made by Group 5 OOP", 0, 0, Font.BOLD, 60F, Color.white, false, 1, true, false, false);
                    fade();
                    break;
                case 2:
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    gp.gameState = gp.titleState;
                    loadedMessages = 0;
                    return;
            }
        }
        else if (gameSubState == 1) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            String text = "Loading Map";
            if (visibleDuration <= 0) {
                dots = (dots+1) % 4;
                visibleDuration = 25;
            }
            else {
                visibleDuration--;
            }
            for (int i = 0; i < dots; i++) {
                text+=".";
            }
            gp.uTool.drawText(g2, text, 0, 0, Font.BOLD, 100F, Color.white, false, 1, true, false, false);

            g2.setColor(Color.green);
            g2.fillRect(0, gp.screenHeight-50, (int)(gp.screenWidth*progress), 50);
            if (progress < 1) {
                progress+=0.01;
            }
            else {
                gp.gameState = gp.playState;
                gameSubState = 0;
                progress = 0f;
                gp.playMusic(0);
                return;
            }
        }
    }
    public void fade() {
        switch (loopCount) {
            case 0:
                if (transparency < 1f)
                    transparency+=0.05;
                if (transparency >= 1f) {
                    transparency = 1f;
                    visibleDuration--;
                }
                if (visibleDuration < 0) {
                    visibleDuration = 60;
                    loopCount++;
                }
                break;
            case 1:
                transparency-=0.05;
                if (transparency <= 0f) {
                    transparency = 0f;
                    loopCount++;
                }
                break;
            case 2:
                loadedMessages++;
                loopCount = 0;
                break;
        }
    }
    public void drawTitleScreen() {
        // MAIN MENU
        if (gameSubState == 0) {
            // BACKGROUND
            g2.drawImage(background, 0, 0, null);
    
            // MAIN TITLE
            int x = 0, y = gp.tileSize*3;
            gp.uTool.drawText(g2, "SUON", x, y, Font.BOLD, 86F, Color.white, true, 1, false, false, false);

            // Subtitle 1
            x = gp.tileSize;
            y = gp.tileSize;
            gp.uTool.drawText(g2, "Greetings, player! Welcome to...", x, y, Font.BOLD, 25F, Color.white, true, 0, false, false, false);

            // Subtitle 2
            y = gp.tileSize*4;
            gp.uTool.drawText(g2, "Maze Game", x, y, Font.BOLD, 25F, Color.white, true, 1, false, false, false);
    
            // PLAYER IMAGE
            // x += gp.tileSize;
            // y = gp.tileSize + 10;
            // g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
    
            // MENU
            y = gp.tileSize*6;
            gp.uTool.drawText(g2, "NEW GAME", x, y, Font.BOLD, 40F, Color.white, true, 1, false, commandNum == 0, false);

            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "LOAD GAME", x, y, Font.BOLD, 40F, Color.white, true, 1, false, commandNum == 1, false);
            
            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "RULES & TUTORIAL", x, y, Font.BOLD, 40F, Color.white, true, 1, false, commandNum == 2, false);
            
            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "OPTIONS", x, y, Font.BOLD, 40F, Color.white, true, 1, false, commandNum == 3, false);
    
            y += gp.tileSize + 10;
            gp.uTool.drawText(g2, "QUIT", x, y, Font.BOLD, 40F, Color.white, true, 1, false, commandNum == 4, false);
        }
        else if (gameSubState == 1) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            int x = 0, y = gp.tileSize*3;
            gp.uTool.drawText(g2, "Select your character.", x, y, Font.BOLD, 42, Color.white, false, 1, false, false, false);

            y += gp.tileSize*3;
            gp.uTool.drawText(g2, "Don", x, y, Font.BOLD, 42F, Color.white, false, 1, false, commandNum == 0, false);
            
            y += gp.tileSize;
            gp.uTool.drawText(g2, "Emma", x, y, Font.BOLD, 42F, Color.white, false, 1, false, commandNum == 1, false);
            
            y += gp.tileSize*2;
            gp.uTool.drawText(g2, "Back", x, y, Font.BOLD, 42F, Color.white, false, 1, false, commandNum == 2, false);
        }
        else if (gameSubState == 4) {
            drawOptionsScreen();
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
            g2.fillRect(barX, barY, (int)(barW * (gp.player.getHealth()/100)), barH);

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
            g2.fillRect(barX, barY, (int)(barW * (gp.player.getStamina()/100)), barH);
            // KEYS
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));
            g2.drawImage(keyImage, gp.tileSize/2, gp.screenHeight - gp.tileSize, gp.tileSize, gp.tileSize, null);
            g2.drawString("x "+gp.player.getKeys(), gp.tileSize+20, gp.screenHeight-10);
    
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
                int y = gp.screenHeight-100;
                String[] measures = {
                    // "Speed: "+gp.player.getSpeed(),
                    "World("+gp.player.getWorldX()/48+", "+gp.player.getWorldY()/48+")",
                    "Grid("+gp.player.getWorldX()+", "+gp.player.getWorldY()+")",
                    // "fatigued: "+gp.player.getStaminaStatus(),
                    // "idle: "+gp.player.getEntityState(),
                    "direction: "+gp.player.getDirection(),
                    "collided: "+gp.player.getColliderStatus(),
                    // "pixel counter: "+gp.player.getPixelTracker(),
                    // "tileCollided: "+gp.player.getTileCollided(),
                    // "entityCollided: "+gp.player.getEntityCollided(),
                    // "collider/WP("+gp.player.getCollider().x+", "+gp.player.getCollider().y+")"
                    "E pressed: "+gp.keyH.interactPressed
                };
                for (String measure : measures) {
                    g2.drawString(measure, 0, y);
                    y -= 30;
                }
            }
        }
        else if (gameSubState == 1) {

            int x = 0, y = gp.screenHeight/2 - (gp.tileSize*3);
            gp.uTool.drawText(g2, "You escaped!", x, y, Font.PLAIN, 40F, Color.white, false, 1, false, false, false);
            
            y += gp.tileSize*5;
            gp.uTool.drawText(g2, "Congratulations!", x, y, Font.BOLD, 80F, Color.yellow, false, 1, false, false, false);
    
            y += gp.tileSize*2;
            gp.uTool.drawText(g2, "---Press ENTER to go back to menu---", x, y, Font.PLAIN, 32F, Color.white, false, 1, false, false, false);
        }
        else if (gameSubState == 2) {
            gp.uTool.drawText(g2, "Game Over!", 0, 0, Font.BOLD, 80F, Color.red, false, 1, true, false, false);
            int y = gp.screenHeight/2+100;
            gp.uTool.drawText(g2, "---Press ENTER to go back to menu---", 0, y, Font.PLAIN, 32F, Color.white, false, 1, false, false, false);
        }
    }
    public void drawOptionsScreen() {
        int x = 0, y = 0, w = gp.screenWidth, h = gp.screenHeight;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // BACKGROUND
        g2.setColor(Color.black);
        if (gp.gameState == gp.optionState)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.fillRect(x, y, w, h);
        if (gp.gameState == gp.optionState)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // OPTION WINDOW
        g2.setColor(Color.white);
        x = gp.screenWidth/2-300;
        y = gp.screenHeight/2-200;
        w = 600;
        h = 400;
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, w, h, 10, 10);
        g2.setStroke(new BasicStroke(1));

        // LEFT PANEL
        g2.setColor(Color.gray);
        x += 10;
        y += 10;
        w -= 400;
        h -= 20;
        g2.fillRoundRect(x, y, w, h, 10, 10);

        // RIGHT PANEL
        x += w+10;
        w += 170;
        g2.fillRoundRect(x, y, w, h, 10, 10);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        x = -(gp.screenWidth/4)+50;
        for (int i = 0; i < options.length; i++) {
            y += 40;
            gp.uTool.drawText(g2, options[i], x, y, Font.PLAIN, 20, Color.white, false, 2, false, (commandNum == i && optionNum == -1), optionNum == i);
        }

        switch (optionNum) {
            case 0:
                x = 50;
                y = gp.screenHeight/2;
                gp.uTool.drawText(g2, "Enable", x, y, Font.PLAIN, 20, Color.white, false, 2, false, false, false);
                g2.setColor(Color.white);
                if (fullscreen)
                    g2.drawRect(gp.screenWidth/2+100, y-20, 20, 20);
                else
                    g2.fillRect(gp.screenWidth/2+100, y-20, 20, 20);
                break;
            case 1:
                x = 50;
                y = gp.screenHeight/2-50;
                gp.uTool.drawText(g2, "Music", x, y, Font.PLAIN, 20, Color.white, false, 2, false, settingNum == 0, false);
                g2.setColor(Color.white);
                g2.drawRect(gp.screenWidth/2+120, y-20, 100, 20);
                g2.fillRect(gp.screenWidth/2+120, y-20, 20*gp.music.volumeScale, 20);
                y = gp.screenHeight/2+50;
                gp.uTool.drawText(g2, "SFX", x, y, Font.PLAIN, 20, Color.white, false, 2, false, settingNum == 1, false);
                g2.drawRect(gp.screenWidth/2+120, y-20, 100, 20);
                g2.fillRect(gp.screenWidth/2+120, y-20, 20*gp.sfx.volumeScale, 20);
                break;
            case 2:
                x = gp.screenWidth/2-50;
                y = gp.screenHeight/4;
                gp.uTool.drawText(g2, "Move", x, y, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "Confirm (Settings)", x, y+50, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "Back (Settings)", x, y+100, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "Pause/Resume/Options", x, y+150, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "WASD", x+200, y, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "ENTER", x+200, y+50, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "A", x+200, y+100, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                gp.uTool.drawText(g2, "ESC", x+200, y+150, Font.PLAIN, 20, Color.white, false, 0, false, false, false);
                break;
            case 3:
                if (gp.gameState == gp.optionState) {
                    x = 50;
                    y = gp.screenHeight/2;
                    gp.uTool.drawText(g2, "Are you sure?", x, y, Font.PLAIN, 20, Color.white, false, 2, false, false, false);
                }
                break;
            case -1:
                break;
        }
    }
}
