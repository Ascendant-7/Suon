package main;

import object.ChestObject;
import object.DoorObject;
import object.KeyObject;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0] = new KeyObject();
        gp.obj[0].worldX = 6 * gp.tileSize;
        gp.obj[0].worldY = 11 * gp.tileSize;

        gp.obj[1] = new KeyObject();
        gp.obj[1].worldX = 1 * gp.tileSize;
        gp.obj[1].worldY = 19 * gp.tileSize;
        
        gp.obj[2] = new KeyObject();
        gp.obj[2].worldX = 23 * gp.tileSize;
        gp.obj[2].worldY = 25 * gp.tileSize;
        
        gp.obj[3] = new DoorObject();
        gp.obj[3].worldX = 27 * gp.tileSize;
        gp.obj[3].worldY = 26 * gp.tileSize;
        
        gp.obj[4] = new ChestObject();
        gp.obj[4].worldX = 31 * gp.tileSize;
        gp.obj[4].worldY = 27 * gp.tileSize;
    }
}
