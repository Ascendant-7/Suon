package main;

import monster.Silhouette;
import object.ChestObject;
import object.DoorObject;
import object.KeyObject;
// import object.ChestObject;
// import object.DoorObject;
// import object.KeyObject;
import tile.EllersAlgorithm.Vector2D;
import tile.TileManager;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(TileManager tileM) {
        gp.obj[0] = new ChestObject(gp);
        int col = tileM.algorithm.chestNExit.get(0).x;
        int row = tileM.algorithm.chestNExit.get(0).y;
        gp.obj[0].worldX = tileM.algorithm.chestNExit.get(0).x * gp.tileSize;
        gp.obj[0].worldY = tileM.algorithm.chestNExit.get(0).y * gp.tileSize;
        System.out.println("chest at: "+col+" "+row);
        
        gp.obj[1] = new KeyObject(gp);
        col = tileM.algorithm.chestNExit.get(1).x;
        row = tileM.algorithm.chestNExit.get(1).y;
        gp.obj[1].worldX = col * gp.tileSize;
        gp.obj[1].worldY = row * gp.tileSize;
        System.out.println("key at: "+col+" "+row);
        
        // gp.obj[2] = new KeyObject(gp);
        // gp.obj[2].worldX = 13 * gp.tileSize;
        // gp.obj[2].worldY = 5 * gp.tileSize;
        
        // gp.obj[3] = new DoorObject(gp);
        // gp.obj[3].worldX = 27 * gp.tileSize;
        // gp.obj[3].worldY = 26 * gp.tileSize;
        
        // gp.obj[4] = new KeyObject(gp);
        // gp.obj[4].worldX = 37 * gp.tileSize;
        // gp.obj[4].worldY = 47 * gp.tileSize;
        
        // gp.obj[5] = new DoorObject(gp);
        // gp.obj[5].worldX = 49 * gp.tileSize;
        // gp.obj[5].worldY = 42 * gp.tileSize;
        
        // gp.obj[6] = new ChestObject(gp);
        // gp.obj[6].worldX = 45 * gp.tileSize;
        // gp.obj[6].worldY = 31 * gp.tileSize;
    }

    public void spawnMonsters(TileManager tileM) {
        for (Vector2D v : tileM.algorithm.monsterLocations) {
            Silhouette mon = new Silhouette(gp);
            mon.worldX = v.x * gp.tileSize;
            mon.worldY = v.y * gp.tileSize;
            gp.monsters.add(mon);
        }
    }
}
