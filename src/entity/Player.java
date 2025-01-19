package entity;

// import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    int standCounter = 0;
    int pixelCounter = 0;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

        solidArea = new Rectangle();
        solidArea.x = 1;
        solidArea.y = 1;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 46;
        solidArea.height = 46;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        
        worldX = gp.tileSize * gp.mapWorldCol/2 - gp.tileSize/2;
        worldY = gp.tileSize * gp.mapWorldRow/2 - gp.tileSize/2;
        speed = 4;
        direction = "down";
    }
    public void getPlayerImage() {
        up1 = setup("player_up1");
        up2 = setup("player_up2");
        down1 = setup("player_down1");
        down2 = setup("player_down2");
        left1 = setup("player_left1");
        left2 = setup("player_left2");
        right1 = setup("player_right1");
        right2 = setup("player_right2");
    }

    public BufferedImage setup(String imageName) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(getClass().getResourceAsStream("/player/"+imageName+".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void update() {
        if (idle) {
            if (keyH.upPressed) {
                direction = "up";
                idle = false;
            }
            else if (keyH.downPressed) {
                direction = "down";
                idle = false;
            }
            else if (keyH.leftPressed) {
                direction = "left";
                idle = false;
            }
            else if (keyH.rightPressed) {
                direction = "right";
                idle = false;
            } else
                idle = true;
    
            // RESET COLLISION FLAGS
            collisionOn = false;
            tileCollided = false;
            objCollided = false;
            // CHECK TILE COLLISION
            gp.cChecker.checkTile(this);
    
            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            if (idle) {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 2;
                    standCounter = 0;
                }
            }
        }
        else {
            collisionOn = tileCollided || objCollided;

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            
            spriteCounter++;
            
            if (spriteCounter > 12) { // limit the change to ten frames per second, instead of 60
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            pixelCounter += speed;

            if (pixelCounter == 48) {
                idle = true;
                pixelCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            
            String objName = gp.obj[i].name;

            switch (objName) {
                case "Key":
                    gp.playSFX(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a key!");
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.playSFX(2);
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("You open a door!");
                    }
                    else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSFX(3);
                    break;
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        
        switch (direction) {
            case "up":
                if (spriteNum == 1)
                    image = up1;
                if (spriteNum == 2)
                    image = up2;
                break;
            case "down":
                if (spriteNum == 1)
                    image = down1;
                if (spriteNum == 2)
                    image = down2;
                break;
            case "left":
                if (spriteNum == 1)
                    image = left1;
                if (spriteNum == 2)
                    image = left2;
                break;
            case "right":
                if (spriteNum == 1)
                    image = right1;
                if (spriteNum == 2)
                    image = right2;
                break;
        }
        g2.drawImage(image, screenX, screenY, null);

        // FOR DEBUGGING COLLIDER AREA (ENABLE COLLIDER VISUAL)
        // g2.setColor(Color.red);
        // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}
