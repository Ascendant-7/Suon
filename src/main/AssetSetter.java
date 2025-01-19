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
        gp.obj[0] = new KeyObject(gp);
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 25 * gp.tileSize;
        
        gp.obj[1] = new DoorObject(gp);
        gp.obj[1].worldX = 27 * gp.tileSize;
        gp.obj[1].worldY = 22 * gp.tileSize;
        
        gp.obj[2] = new KeyObject(gp);
        gp.obj[2].worldX = 13 * gp.tileSize;
        gp.obj[2].worldY = 5 * gp.tileSize;
        
        gp.obj[3] = new DoorObject(gp);
        gp.obj[3].worldX = 27 * gp.tileSize;
        gp.obj[3].worldY = 26 * gp.tileSize;
        
        gp.obj[4] = new KeyObject(gp);
        gp.obj[4].worldX = 37 * gp.tileSize;
        gp.obj[4].worldY = 47 * gp.tileSize;
        
        gp.obj[5] = new DoorObject(gp);
        gp.obj[5].worldX = 49 * gp.tileSize;
        gp.obj[5].worldY = 42 * gp.tileSize;
        
        gp.obj[6] = new ChestObject(gp);
        gp.obj[6].worldX = 45 * gp.tileSize;
        gp.obj[6].worldY = 31 * gp.tileSize;
    }
}
