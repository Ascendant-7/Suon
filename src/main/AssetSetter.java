package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import entity.Entity;
import monster.Silhouette;
import object.ChestObject;
import object.DoorObject;
import object.KeyObject;
import tile.EllersAlgorithm.Vector2D;
import tile.EllersAlgorithm;

public class AssetSetter {

    GamePanel gp;
    EllersAlgorithm algorithm;
    int row, col;
    int randomIndex = 0;

    // EXISTENCE
    protected static ArrayList<Entity> entities = new ArrayList<>();

    public AssetSetter(GamePanel gp) {

        this.gp = gp;
        algorithm = gp.tileM.algorithm;
        
        spawnEntities();
    }

    public void spawnEntities() {
        
        entities.add(gp.player);
        spawnObjectives();
        spawnObstacles();
    }

    public void spawnObstacles() {
        for (Vector2D v : algorithm.monsterLocations) {
            Silhouette mon = new Silhouette(gp);
            mon.updateLocation(v.x * gp.tileSize, v.y * gp.tileSize);
            entities.add(mon);
        }
    }

    public void spawnObjectives() {
        Entity obj;
        int size = algorithm.chestNExit.size();
        for (int i = 0; i < size; i++) {
            if (i < size-2) 
                obj = new KeyObject(gp);
            else if (i < size-1)
                obj = new ChestObject(gp);
            else
                obj = new DoorObject(gp);
            obj.updateLocation(algorithm.chestNExit.get(i).x * gp.tileSize, algorithm.chestNExit.get(i).y * gp.tileSize);
            entities.add(obj);
        }
    }

    static public void sortEntities() {
        Collections.sort(entities, new Comparator<Entity>() {

            @Override
            public int compare(Entity e1, Entity e2) {

                int result = Integer.compare(e1.getWorldY(), e2.getWorldY());
                return result;
            }
            
        });
    }
    static public Entity getEntityListAt(int index) { return entities.get(index); }
    static public int getEntityListSize() { return entities.size(); }
}
