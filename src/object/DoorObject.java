package object;

import entity.Entity;
import main.GamePanel;

public class DoorObject extends Entity{

    public DoorObject(GamePanel gp) {
        
        super(gp);
        name = "Door";
        down1 = gp.uTool.setup("/objects/door");
        collisionOn = true;
    }
}
