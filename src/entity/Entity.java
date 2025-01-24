package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Entity {

    // BORROWED COMPONENTS
    public GamePanel gp;
    
    // WORLD POSITION
    public int worldX, worldY;
    // MOVEMENT SETTING
    public int speed;

    // SPRITES
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, dead1, dead2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int pixelCounter = 0;

    // COLLISION
    public Rectangle solidArea = new Rectangle(1, 1, 46, 46);
    public int solidAreaDefaultX = solidArea.x;
    public int solidAreaDefaultY = solidArea.y;
    public boolean collisionOn, tileCollided, objCollided;

    // CHARACTER STATUS
    public boolean idle = true, fatigued;
    public float maxLife, life, maxStamina, stamina, damage;
    public int attackCooldown = 0, hurtTime = 0, deadDuration = 120;
    public String name;

    public Entity(GamePanel gp) {

        this.gp = gp;
    }

    public void setAction() {
    }

    public void update() {

        setAction();

        collisionOn = false;
        tileCollided = false;

        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this);
        collisionOn = tileCollided || objCollided;
        if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = down1;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (screenX + gp.tileSize > 0 && 
            screenX - gp.tileSize < gp.player.screenX*2 && 
            screenY + gp.tileSize > 0 && 
            screenY - gp.tileSize < gp.player.screenY*2)
            g2.drawImage(image, screenX, screenY, null);
    }
}
