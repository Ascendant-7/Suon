package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class Silhouette extends Entity {

    Random random = new Random();
    public int actionLockCounter = 0;
    String[] directions = {"up", "down", "left", "right"};
    String[] accessible = new String[4];
    int accessIndex = 0;
    public Silhouette (GamePanel gp) {
        super(gp);

        name = "Silhoutte";
        direction = "down";
        speed = 2;
        damage = 25F;

        getMonsterImage();
    }
    public void getMonsterImage() {
        String suffix = "/monsters/";
        down1 = gp.uTool.setup(suffix+"monster");
        down2 = gp.uTool.setup(suffix+"monster_1");
    }

    @Override
    public void setAction() {

        pixelCounter += speed;

        // CHANGE DIRECTION ONCE COUNTER IS UP
        if (pixelCounter == 48) {
            // CHECK FOR ACCESSIBLE TILES
            for (String d : directions) {
                direction = d;
                collisionOn = gp.cChecker.checkOverallCollision(this);

                if (!collisionOn) {
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
            pixelCounter = 0;
        }
        
    }
}
