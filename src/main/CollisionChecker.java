package main;


import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Entity;
import entity.LivingEntity;
import entity.Player;
import enums.ID;

public class CollisionChecker {

    // BOUNDARIES
    int[] entityBounds = new int[4];
    int[] targetBounds = new int[4];
    int entityTopRow, entityBottomRow, entityLeftCol, entityRightCol;
    int tileNum1, tileNum2;
    int speed;

    // CONSTANTS
    final int topBound = 0, botBound = 1, leftBound = 2, rightBound = 3;

    GamePanel gp;
    ArrayList<Entity> entities;
    Rectangle collider;
    public CollisionChecker(GamePanel gp) {

        this.gp = gp;
        entities = AssetSetter.entities;
    }

    public void getEntityCollider(Entity entity, boolean target) {
        if (target) {
            targetBounds[topBound] = entity.getColliderTopBound();
            targetBounds[botBound] = entity.getColliderBottomBound();
            targetBounds[leftBound] = entity.getColliderLeftBound();
            targetBounds[rightBound] = entity.getColliderRightBound();
        }
        else {
            entityBounds[topBound] = entity.getColliderTopBound();
            entityBounds[botBound] = entity.getColliderBottomBound();
            entityBounds[leftBound] = entity.getColliderLeftBound();
            entityBounds[rightBound] = entity.getColliderRightBound();
        }
        
    }

    public boolean checkTile(LivingEntity entity) {

        speed = entity.getSpeed();

        // ENTITY COLLIDER BOUND
        getEntityCollider(entity, false);  // MODIFY BOUND ARRAY

        // ENTITY COLLIDER BOUND ON GRID
        entityLeftCol = entityBounds[leftBound] / gp.tileSize;
        entityRightCol = entityBounds[rightBound] / gp.tileSize;
        entityTopRow = entityBounds[topBound] / gp.tileSize;
        entityBottomRow = entityBounds[botBound] / gp.tileSize;

        switch (entity.getDirection()) {
            case UP:
                entityTopRow = (entityBounds[topBound] - speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                break;
            case DOWN:
                entityBottomRow = (entityBounds[botBound] + speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                break;
            case LEFT:
                entityLeftCol= (entityBounds[leftBound] - speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                break;
            case RIGHT:
                entityRightCol = (entityBounds[rightBound] + speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                break;
        }
        
        return gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision;
    }

    public boolean checkEntity(LivingEntity entity) {

        collider = entity.getCollider();
        switch (entity.getDirection()) {
            case UP:
                collider.y -= entity.getSpeed();
                break;
            case DOWN:
                collider.y += entity.getSpeed();
                break;
            case LEFT:
                collider.x -= entity.getSpeed();
                break;
            case RIGHT:
                collider.x += entity.getSpeed();
                break;
        }
        for (Entity e : entities) {

            if (e == null || e.getID() == entity.getID()) continue;

            if (collider.intersects(e.getCollider())) {
                if (entity.getID() == ID.PLAYER) {
                    ((Player)entity).interactWith(e);
                }
                return e.getColliderStatus();
            }
        }
        return false;

    }
    // public boolean checkEntity(Player player) {

    //     for (Entity e : entities) {

    //         if (e == null) continue;

    //         if (player.getCollider().intersects(e.getCollider())) {
    //             System.out.println("interacted as a player");
    //             player.interactWith(player);
    //             return e.getColliderStatus();
    //         }
    //     }
    //     return false;
    // }

    public boolean intersects() {
        boolean topIntersected = entityBounds[topBound] >= targetBounds[topBound] && entityBounds[topBound] <= targetBounds[botBound];
        boolean botIntersected = entityBounds[botBound] >= targetBounds[topBound] && entityBounds[botBound] <= targetBounds[botBound];
        boolean leftIntersected = entityBounds[leftBound] >= targetBounds[leftBound] && entityBounds[leftBound] <= targetBounds[rightBound];
        boolean rightIntersected = entityBounds[rightBound] >= targetBounds[leftBound] && entityBounds[rightBound] <= targetBounds[rightBound];
        return topIntersected || botIntersected || leftIntersected || rightIntersected;
    }
}
