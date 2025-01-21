package entity;

import java.awt.AlphaComposite;
// import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    int standCounter = 0;

    long fatigueT1;
    long fatigueT2;

    
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

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

        // PLAYER STATUS
        maxLife = 100;
        life = maxLife;
        maxStamina = 100;
        stamina = maxStamina;
    }
    public void getPlayerImage() {
        String suffix = "/player/";
        up1 = gp.uTool.setup(suffix+"player_up1");
        up2 = gp.uTool.setup(suffix+"player_up2");
        down1 = gp.uTool.setup(suffix+"player_down1");
        down2 = gp.uTool.setup(suffix+"player_down2");
        left1 = gp.uTool.setup(suffix+"player_left1");
        left2 = gp.uTool.setup(suffix+"player_left2");
        right1 = gp.uTool.setup(suffix+"player_right1");
        right2 = gp.uTool.setup(suffix+"player_right2");
    }

    @Override
    public void update() {
        // CODE FOR PLAYER'S IDLE STATE
        if (idle) {
            // CHECK FOR MOVEMENT INPUTS
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
            }

            // RESET COLLISION FLAGS
            collisionOn = false;
            tileCollided = false;
            objCollided = false;
            // CHECK TILE COLLISION
            gp.cChecker.checkTile(this);
    
            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this);
            pickUpObject(objIndex);

            // MINOR ANIMATION FIXES
            if (idle) {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 2;
                    standCounter = 0;
                }
            }
        }
        else {
            // SUMMARIZE BOTH COLLISION CHECKS
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
            
            // ANIMATION
            spriteCounter++;
            
            if (spriteCounter > 12) { // limit the change to ten frames per second, instead of 60
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            // PLAYER TILE-BASED MOVEMENT CHANGES
            pixelCounter += speed;

            if (pixelCounter == 48) {
                idle = true;
                pixelCounter = 0;
            }
        }
        if (fatigued) {
            if (speed != 2 && pixelCounter == 0) {
                speed = 2;
            }
            if (!keyH.shiftPressed) {
                fatigueT2 = System.currentTimeMillis();
                if (fatigueT2-fatigueT1 > 3000 && stamina > 30) {
                    fatigued = false;
                }
                else {
                    stamina += 0.2;
                }
            }
        }
        else {
            if (keyH.shiftPressed && !idle) {
                if (stamina <= 0) {
                    fatigued = true;
                    fatigueT1 = System.currentTimeMillis();
                }
                else {
                    if (speed != 6  && pixelCounter == 0) {
                        speed = 6;
                    }
                    stamina-=0.4;
                }
            }
            else if (speed != 4  && pixelCounter == 0) {
                speed = 4;
            }
            else if (stamina < 100) {
                stamina+=0.2;
            }
        }

        
        gp.cChecker.checkMonster(this, gp.monsters);
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
                    gp.ui.gameSubState = 1;
                    gp.stopMusic(false);
                    gp.playSFX(3);
                    break;
            }
        }
    }

    @Override
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

        if (hurtTime > 0) {
            // TRANSPARENT
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            hurtTime--;
        }
        g2.drawImage(image, screenX, screenY, null);

        // RESET TRANSPARENCY
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    

        // FOR DEBUGGING COLLIDER AREA (ENABLE COLLIDER VISUAL)
        // g2.setColor(Color.red);
        // g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}
