package object;

import entity.Entity;
import enums.ID;
import main.GamePanel;

public class DoorObject extends Entity{

    public static int record = -1;
    int door_id;

    public DoorObject(GamePanel gp) {
        
        super(gp);
        id = ID.DOOR;
        door_id = record;
        record++;
        collided = true;
        collider.width = 48;
        collider.height = 48;
    }
    @Override
    public void getImage() {
        suffix = "/objects/";
        imagePath[0] = "door";
        imagePath[1] = "door_2";
        super.getImage();
    }
    public int getDoorId() { return door_id; }
}
