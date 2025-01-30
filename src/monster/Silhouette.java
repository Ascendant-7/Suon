package monster;

import java.util.Random;

import entity.LivingEntity;
import enums.Direction;
import enums.EntityState;
import enums.ID;
import main.GamePanel;

public class Silhouette extends LivingEntity {

    Direction[] directions = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
    Random random = new Random();
    Direction[] accessible = new Direction[4];
    int steps = 0;
    int idleDuration = 0;
    int accessIndex = 0;
    public Silhouette (GamePanel gp) {

        super(gp);

        id = ID.MONSTER;
        speed = 2;
        damage = 25F;
        health = 100f;
        // steps = random.nextInt(4);
        state = EntityState.IDLE;
    }
    @Override
    public void getImage() {
        suffix = "/monsters/";
        imagePath[0] = "monster";
        imagePath[1] = "monster_1";
        super.getImage();
    }
    
    // @Override 
    // public void switchSprite() {}
    // @Override
    // public void normalizeSprite() {}
    // @Override
    // public void resetIdleTrackers() {}
    @Override
    public void idleUpdate() {
        setAction();
    }
    @Override
    public void checkCollision() {
        super.checkCollision();
        if (collided)
            state = EntityState.IDLE;
    }
    public void setAction() {

        state = EntityState.MOVING;
        if (steps > 0) {
            steps--;
            return;
        }
        if (idleDuration > 0) {
            idleDuration--;
            return;
        }
        // if (steps > 0) {
        //     steps--;
        //     return;
        // }
        // CHECK FOR ACCESSIBLE TILES
        for (Direction d : directions) {
            direction = d;
            collided = gp.cChecker.checkTile(this);

            if (!collided) {
                accessible[accessIndex] = direction;
                accessIndex++;
            }
        }
        // RANDOMLY USE ACCESSIBLE TILES
        int index = random.nextInt(accessIndex);
        accessIndex = 0;
        direction = accessible[index];
        for (int i = 0; i < accessible.length; i++) {
            accessible[i] = null;
        }
        steps = random.nextInt(4)+1;
        idleDuration = random.nextInt(3)*60;
        
    }
}
