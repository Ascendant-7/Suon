package entity;

import java.awt.AlphaComposite;
// import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;

import enums.Direction;
import enums.EntityState;
import enums.ID;
import enums.Speed;
import main.GamePanel;
import main.KeyHandler;
import object.ChestObject;
import object.DoorObject;
import object.KeyObject;

public class Player extends LivingEntity {

    // BORROWED COMPONENTS
    private KeyHandler keyH;

    // SCREEN POSITION (CONSTANT)
    protected final int screenX;
    protected final int screenY;
    
    // STATUS FIELDS
    private boolean fatigued; // FOR PLAYER ENTITY ONLY
    private Speed staminaStatus = Speed.NORMAL;

    // STATUS POINTS
    private float maxStamina, stamina; // FOR PLAYER ENTITY ONLY

    // INVENTORY
    private HashSet<Integer> keys = new HashSet<>();

    // INTERACTION

    // CONSTANTS
    private static final float FATIGUE_THRESHOLD = 30f;
    private static final int FATIGUED_SPEED = 2;
    private static final int NORMAL_SPEED = 4;
    private static final int SPRINTING_SPEED = 6;
    private static final float STAMINA_RECOVERY_RATE = 0.25f;
    private static final float STAMINA_DEPLETION_RATE = 0.75f;

    public Player(GamePanel gp) {

        super(gp);
        this.keyH = gp.keyH;
        
        id = ID.PLAYER;

        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
        setDefaultValues();

    }

    public void setDefaultValues() {
        
        // DEFAULT LOCATION
        worldX = (gp.mapWorldCol-1)*(gp.tileSize/2);
        worldY = (gp.mapWorldRow-1)*(gp.tileSize/2);

        // DEFAULT SPEED
        speed = 4;

        // DEFAULT STATUS POINTS
        maxHealth = 100;
        health = maxHealth;
        maxStamina = 100;
        stamina = maxStamina;
        keys.clear();

        state = EntityState.IDLE;
        spriteIndex = 2;
    }

    @Override
    public void getImage() {
        suffix = "/player/player_";
        // SPRITES
        imagePath[UP_1] = "up1";
        imagePath[UP_2] = "up2";
        imagePath[DOWN_1] = "down1";
        imagePath[DOWN_2] = "down2";
        imagePath[LEFT_1] = "left1";
        imagePath[LEFT_2] = "left2";
        imagePath[RIGHT_1] = "right1";
        imagePath[RIGHT_2] = "right2";
        imagePath[DEAD_1] = "dead1";
        imagePath[DEAD_2] = "dead2";
        super.getImage();
    }
    @Override
    public void movingUpdate() {
        
        super.movingUpdate();
    }

    @Override
    public void idleUpdate() {

        checkPlayerInput();
        super.idleUpdate();
        checkStatus();
        adjustSpeed();
    }

    public void checkStatus() {

        handleFatigue();
        handleSprinting();
    }

    public void handleFatigue() {

        if (!fatigued) return;

        // DECREASED SPEED ON FATIGUED IF HAVEN'T
        staminaStatus = Speed.FATIGUED;
        // RECOVER STAMINA IF 'SHIFT' ISN'T PRESSED
        stamina += STAMINA_RECOVERY_RATE;
        // IF STAMINA IS OVER 30, PLAYER IS NO LONGER FATIGUED
        if (stamina > FATIGUE_THRESHOLD) fatigued = false;
    }

    public void handleSprinting() {

        if (fatigued) return;

        // PRESSING 'SHIFT' AND MOVEMENT KEYS
        if (keyH.shiftPressed && state == EntityState.MOVING) {

            // SWITCH TO SPRINTING
            staminaStatus = Speed.SPRINT;
            // DECREASE STAMINA
            stamina -= STAMINA_DEPLETION_RATE;
        }
        else {

            // NORMALIZE SPEED IF NOT SPRINTING
            staminaStatus = Speed.NORMAL;
            // RECOVER STAMINA IF NOT FULL
            if (stamina < maxStamina) stamina += STAMINA_RECOVERY_RATE;
        }
        // BECOME FATIGUED IF STAMINA DROPPED TO 0
        if (stamina <= 0) fatigued = true;
    }

    public void adjustSpeed() {
        switch (staminaStatus) {
            case FATIGUED:
                if (speed != FATIGUED_SPEED) speed = FATIGUED_SPEED;
                break;
            case NORMAL:
                if (speed != NORMAL_SPEED) speed = NORMAL_SPEED;
                break;
            case SPRINT:
                if (speed != SPRINTING_SPEED) speed = SPRINTING_SPEED;
                break;
            default:
                break;
            
        }
    }

    public void interactWith(Entity e) { 
        switch (e.id) {
            case KEY: interactWithKey((KeyObject) e); break;
            case DOOR: interactWithDoor((DoorObject) e); break;
            case CHEST:interactWithChest((ChestObject) e); break;
            case MONSTER: interactWithMonsters((LivingEntity) e); break;
            default:
                break;
        }
    }

    public void interactWithKey(KeyObject key) {

        if (!key.isEnabled()) return;
        int id = ((KeyObject)key).getKeyId();
        keys.add(id);
        gp.ui.showMessage("You found a key!");
        key.disable();
    }

    public void interactWithDoor(DoorObject door) {
        for (Integer ID : keys) {
            if (ID == -1) {
                door.collided = false;
                door.spriteIndex = 1;
                gp.ui.gameSubState = 1;
                state = EntityState.WON;
                gp.stopMusic();
                gp.playSFX(3);
                return;
            }
        }
        if (keys.isEmpty())
            gp.ui.showMessage("You don't have any keys.");
        else
            gp.ui.showMessage("You don't have the right key. The door is still locked.");
        
    }

    public void interactWithChest(ChestObject chest) {
        if (!chest.isEnabled()) return;
        for (Integer ID : keys) {
            if (ID == 0) {
                keys.remove(0);
                keys.add(-1);
                gp.ui.showMessage("You got the final key! Find the exit!");
                chest.disable();
                return;
            }
        }
        if (keys.isEmpty())
            gp.ui.showMessage("You don't have any keys.");
        else
            gp.ui.showMessage("You don't have the right key. The chest is still locked.");
        
        
    }

    public void interactWithMonsters(LivingEntity monster) {
        monster.setState(EntityState.ATTACK);
        if (hurtCooldown <= 0) {
            health -= monster.damage;
            hurtCooldown = 60;
        }
    }

    public void checkPlayerInput() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = Direction.UP;
                spriteIndex = 0 + spriteSwitch;
            }
            else if (keyH.downPressed) {
                direction = Direction.DOWN;
                spriteIndex = 2 + spriteSwitch;
            }
            else if (keyH.leftPressed) {
                direction = Direction.LEFT;
                spriteIndex = 4 + spriteSwitch;
            }
            else if (keyH.rightPressed) {
                direction = Direction.RIGHT ;
                spriteIndex = 6 + spriteSwitch;
            }
            checkCollision();
            if (!collided)
                state = EntityState.MOVING;
        }
        else {
            state = EntityState.IDLE;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        
        // INCREASE TRANSPARENCY FOR PLAYER'S IMAGE
        if (hurtCooldown > 0) 
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

        // DRAW PLAYER
        g2.drawImage(sprites.get(spriteIndex), screenX, screenY, null);

        // DECREASE TRANSPARENCY AFTER DRAWN
        if (hurtCooldown > 0) 
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // FOR DEBUGGING COLLIDER AREA (ENABLE COLLIDER VISUAL)
        // g2.setColor(Color.red);
        // g2.drawRect(screenX + 1, screenY + 1, 46, 46);
    }

    public float getStamina() { return stamina; }
    public int getKeys() { return keys.size(); }
    public HashSet<Integer> getKeyList() {return keys; }
    public Speed getStaminaStatus() {  return staminaStatus; }
    public EntityState getEntityState() { return state; }
    public void loadKey(int key) { keys.add(key); }
    public void loadHealth(float health) { this.health = health; }
    public void loadStamine(float stamina) {this.stamina = stamina; }
    public void clearKeys() { keys.clear();}
}
