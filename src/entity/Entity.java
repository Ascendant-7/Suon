package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enums.ID;
import main.GamePanel;

// SUPER CLASS
public class Entity {

    // BORROWED COMPONENTS
    protected GamePanel gp;
    
    // WORLD POSITION
    protected int worldX, worldY, screenX, screenY;

    // SPRITES
    protected String suffix = "";
    protected String[] imagePath = new String[10];
    protected ArrayList<BufferedImage> sprites = new ArrayList<>();
    protected int spriteIndex = 0;
    protected int spriteSwitch = 0;

    // MAIN ENTITY TRACKERS
    protected int cSpriteCooldown = 0;  // cooldown for changing sprite

    // COLLISION FOR MAIN ENTITY CLASS: USED FOR CHECKING SELF-COLLISION OR SETTING COLLISION TO ONESELF
    protected boolean collided;
    protected Rectangle collider = new Rectangle(1, 1, 46, 46);

    // ENTITY FIELD
    protected ID id = ID.NONE;

    // STATUS POINTS
    protected float damage;

    // DRAW SETTING
    protected boolean onScreen; 

    public Entity(GamePanel gp) {

        this.gp = gp;
        getImage();
    }
    
    public void getImage() {
        for (String path : imagePath) {
            if (path == null) continue;
            sprites.add(gp.uTool.setup(suffix+path));
        }
    }

    public void update() {
        if (id == ID.PLAYER)
            onScreen = true;
        else {
            onScreen = screenX + gp.tileSize > 0 && 
            screenY + gp.tileSize > 0 && 
            screenX - gp.tileSize < gp.player.screenX*2 && 
            screenY - gp.tileSize < gp.player.screenY*2;
        }
        if (id == ID.DOOR) {
            collider.x = worldX;
            collider.y = worldY;
        }
        else {
            collider.x = worldX + 1;
            collider.y = worldY + 1;
        }
            
    }

    public void draw(Graphics2D g2) {

        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (onScreen) g2.drawImage(sprites.get(spriteIndex), screenX, screenY, null);
    }

    public void updateLocation(int x, int y) {
        worldX = x;
        worldY = y;
    }

    public int getWorldX() { return worldX; }
    public int getWorldY() {return worldY; }
    public int getScreenX() { return screenX; }
    public int getScreenY() {return screenY; }

    public int getColliderLeftBound() { return collider.x; }
    public int getColliderRightBound() { return collider.x + collider.width; }
    public int getColliderTopBound() { return collider.y; }
    public int getColliderBottomBound() { return collider.y + collider.height; }

    public boolean getColliderStatus() { return collided; }
    public void setColldierStatus(boolean collided) { this.collided = collided; }
    public Rectangle getCollider() { return collider; }
    public ID getID() {return id; }
    public void setSpriteIndex(int index) { spriteIndex = index; }
}
