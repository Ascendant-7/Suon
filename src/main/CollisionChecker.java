package main;

import java.util.ArrayList;

import entity.Entity;
import entity.Player;

public class CollisionChecker {

    GamePanel gp;
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1 = 0, tileNum2 = 0;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                break;
            case "left":
                entityLeftCol= (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                break;
        }
        
        entity.tileCollided = gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision;
    }

    public int checkObject(Player player) {

        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            
            if (gp.obj[i] != null) {

                // get entity's solid area position
                player.solidArea.x += player.worldX;
                player.solidArea.y += player.worldY;

                // get the object's solid area position
                gp.obj[i].solidArea.x += gp.obj[i].worldX;
                gp.obj[i].solidArea.y += gp.obj[i].worldY;

                switch (player.direction) {
                    case "up":
                        player.solidArea.y -= player.speed;
                        break;
                    case "down":
                        player.solidArea.y += player.speed;
                        break;
                    case "left":
                        player.solidArea.x -= player.speed;
                        break;
                    case "right":
                        player.solidArea.x += player.speed;
                        break;
                }
                if (player.solidArea.intersects(gp.obj[i].solidArea)) {
                    player.objCollided = gp.obj[i].collisionOn;
                    index = i;
                }

                // RESET
                player.solidArea.x = player.solidAreaDefaultX;
                player.solidArea.y = player.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public void checkObject(Entity entity) {

        for (int i = 0; i < gp.obj.length; i++) {
            
            if (gp.obj[i] != null) {

                // get entity's solid area position
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                // get the object's solid area position
                gp.obj[i].solidArea.x += gp.obj[i].worldX;
                gp.obj[i].solidArea.y += gp.obj[i].worldY;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    entity.objCollided = gp.obj[i].collisionOn;
                }

                // RESET
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }
    }
    public boolean checkOverallCollision(Entity e) {
        e.tileCollided = false;
        e.objCollided = false;

        checkTile(e);
        checkObject(e);
        return e.tileCollided || e.objCollided;

    }

    public void checkMonster(Player entity, ArrayList<Entity> target) {

        for (Entity e : target) {
            
            if (e != null) {

                // get entity's solid area position
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                // get the target's solid area position
                e.solidArea.x += e.worldX;
                e.solidArea.y += e.worldY;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(e.solidArea)) {
                    if (e.attackCooldown <= 0 && entity.hurtTime <= 0) {
                        entity.life -= e.damage;
                        e.attackCooldown = 120;
                        entity.hurtTime = 100;
                    }
                }

                // RESET
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                e.solidArea.x = e.solidAreaDefaultX;
                e.solidArea.y = e.solidAreaDefaultY;
            }
        }
    }
}
