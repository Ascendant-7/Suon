package monster;

import java.util.Random;

import entity.LivingEntity;
import enums.Direction;
import enums.ID;
import main.GamePanel;

public class Silhouette extends LivingEntity {

    Direction[] directions = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
    Random random = new Random();
    Direction[] accessible = new Direction[4];
    int steps;
    int accessIndex = 0;
    public Silhouette (GamePanel gp) {

        super(gp);

        id = ID.MONSTER;
        speed = 2;
        damage = 25F;
        steps = random.nextInt(4);
    }
    @Override
    public void getImage() {
        suffix = "/monsters/";
        imagePath[0] = "monster";
        imagePath[1] = "monster_1";
        super.getImage();
    }

    @Override
    public void update() {
        setAction();
        super.update();
    }
    public void setAction() {

        // CHANGE DIRECTION ONCE COUNTER IS UP
        if (pixelTracker > 0) return;
        if (steps > 0) {
            steps--;
            return;
        }
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
        int index = random.nextInt(accessIndex); // ACCESS BOUND IS < 1
        accessIndex = 0;
        direction = accessible[index];
        for (int i = 0; i < accessible.length; i++) {
            accessible[i] = null;
        }
        steps = random.nextInt(4);
        
    }
}
