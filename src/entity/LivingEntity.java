package entity;

import enums.Direction;
import enums.EntityState;
import main.GamePanel;

public class LivingEntity extends Entity {


    // MOVEMENT-RELATED FIELDS
    protected int speed;
    protected Direction direction = Direction.DOWN;

    // SUB ENTITY TRACKERS
    protected int pixelTracker = 48;     // cooldown for changing directions
    protected int attackCooldown = 0;   // cooldown for attacking
    protected int hurtCooldown = 0;     // cooldown for invicibility
    protected int deadDuration = 120;   // dead animation duration
    protected int standCooldown = 20;      // normalize idle state to the first sprite

    // SPRITE INDICES
    protected static final int UP_1 = 0;
    protected static final int UP_2 = 1;
    protected static final int DOWN_1 = 2;
    protected static final int DOWN_2 = 3;
    protected static final int LEFT_1 = 4;
    protected static final int LEFT_2 = 5;
    protected static final int RIGHT_1 = 6;
    protected static final int RIGHT_2 = 7;
    protected static final int DEAD_1 = 8;
    protected static final int DEAD_2 = 9;

    // COLLSION FLAGS FOR MOVEMENT-RELATED COLLISIONS
    protected boolean tryToMove, tileCollided, entityCollided;

    // STATUS FIELDS
    protected EntityState state = EntityState.IDLE;

    // CHARACTER STATUS POINTS
    protected float maxHealth, health;

    public LivingEntity(GamePanel gp) {

        super(gp);
    }

    @Override
    public void update() {

        switch (state) {
            case DEAD:
                if (deadDuration > 0) {
                    if (deadDuration == 0) gp.ui.gameSubState = 2;
                    spriteIndex = deadDuration > 100 ? DEAD_1 : DEAD_2;
                    deadDuration--;
                }
                else {
                    gp.ui.gameSubState = 2;
                    gp.stopMusic();
                    gp.playSFX(4);
                }
                return;
            case IDLE:
                super.update();
                idleUpdate();
                break;
            case MOVING:
                super.update();
                movingUpdate();
                break;
            case ATTACK:
                if (attackCooldown > 0) attackCooldown--;
                else {
                    attackCooldown = 60;
                    state = EntityState.IDLE;
                }
            default:
                break;
        }

        if (hurtCooldown > 0) hurtCooldown--;
        if (health <= 0) {
            System.err.println("You died");
            state = EntityState.DEAD;
        }
    }

    public void movingUpdate() {
        resetIdleTrackers();
        checkCollision();
        switchSprite();
        move();
        trackPixels();
    }

    public void idleUpdate() {
        normalizeSprite();
    }
    
    public void switchSprite() {

        // TRACK COOLDOWN
        if (cSpriteCooldown > 0) cSpriteCooldown--;
        // RESET AND MODIFY
        else {
            spriteSwitch = spriteSwitch == 0 ? 1 : 0;
            cSpriteCooldown = 12;
        }
    }

    public void trackPixels() {
        if (pixelTracker == 0) {
            state = EntityState.IDLE;
            pixelTracker = 48;
        }
    }

    public void normalizeSprite() {
        if (spriteIndex % 2 == 1) return;
        if (standCooldown > 0) {
            standCooldown--;
            return;
        }
        spriteIndex++;
    }

    public void resetIdleTrackers() {
        if (standCooldown != 20) standCooldown = 20;
    }

    public void checkCollision() {
        collided = false;
        tileCollided = false;
        entityCollided = false;
        // CHECKERS FROM THE COLLISION CHECKER CLASS MODIFIES THE ENTITY'S COLLISION FLAG
        tileCollided = gp.cChecker.checkTile(this); // MODIFY TILE COLLISION FLAG
        entityCollided = gp.cChecker.checkEntity(this);
        collided = tileCollided || entityCollided;
        
    }

    public void move() {
        if (!collided) {
            switch (direction) {
                case UP: 
                    worldY -= speed; 
                    pixelTracker -= speed;
                    break;
                case DOWN: 
                    worldY += speed; 
                    pixelTracker -= speed;
                    break;
                case LEFT: 
                    worldX -= speed; 
                    pixelTracker -= speed;
                    break;
                case RIGHT: 
                    worldX += speed; 
                    pixelTracker -= speed;
                    break;
            }
        }
    }

    public Direction getDirection() { return direction; }
    public int getSpeed() { return speed; }
    public void setState(EntityState state) { this.state = state; }
    public float getHealth() { return health; }
    public int getPixelTracker() { return pixelTracker; }
    public boolean getTileCollided() { return tileCollided; }
    public boolean getEntityCollided() { return entityCollided; }
}
