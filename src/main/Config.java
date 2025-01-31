package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import entity.Entity;
import enums.ID;
import monster.Silhouette;
import object.ChestObject;
import object.DoorObject;
import object.KeyObject;

public class Config {

    GamePanel gp;
    boolean saved;

    public Config(GamePanel gp) {

        this.gp = gp;
    }

    public void saveConfig() {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // FULLSCREEN
            bw.write(gp.fullscreen ? "On" : "Off");
            bw.newLine();

            // MUSIC VOLUME
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            // SFX VOLUME
            bw.write(String.valueOf(gp.sfx.volumeScale));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("game.txt"));

            System.out.println("saving game...");
            bw.write("yes");
            bw.newLine();

            bw.write("keys: ");
            for (Integer key : gp.player.getKeyList()) {
                bw.write(Integer.toString(key)+" ");
            }
            bw.newLine();
            bw.write("player-position: "+gp.player.getWorldX()/gp.tileSize+" "+gp.player.getWorldY()/gp.tileSize);
            bw.newLine();
            System.out.println("player-position: ("+gp.player.getWorldX()/gp.tileSize+" "+gp.player.getWorldY()/gp.tileSize+")");
            
            bw.write("player-health: "+gp.player.getHealth());
            bw.newLine();
            System.out.println("player-health: "+gp.player.getHealth());

            bw.write("player-stamina: "+gp.player.getStamina());
            bw.newLine();
            System.out.println("player-health: "+gp.player.getStamina());

            bw.write("seed: "+gp.tileM.algorithm.getSeed());
            System.out.println("player-health: "+gp.tileM.algorithm.getSeed());
            bw.newLine();
            
            for (Entity entity : AssetSetter.entities) {
                ID id = entity.getID();
                if (id == ID.PLAYER) continue;
                int x = entity.getWorldX()/gp.tileSize;
                int y = entity.getWorldY()/gp.tileSize;

                switch (id) {
                    case CHEST:
                        bw.write("C "+x+" "+y+" ");
                        bw.write(((ChestObject)entity).isEnabled() ? "E" : "D");
                        bw.newLine();
                        break;
                    case DOOR:
                        bw.write("D "+x+" "+y);
                        bw.newLine();
                        break;
                    case KEY:
                        bw.write("K "+x+" "+y+" ");
                        bw.write(((KeyObject)entity).isEnabled() ? "E" : "D");
                        bw.write(" "+((KeyObject)entity).getKeyId());
                        bw.newLine();
                        break;
                    case MONSTER:
                        bw.write("M "+x+" "+y);
                        bw.newLine();
                        break;
                    default:
                        break;
                    
                }
            }
            
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void loadSavedGame() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("game.txt"));
            String line = br.readLine();
            if (line == null || !line.equals("yes")) {
                saved = false;
                br.close();
                return;
            }
            saved = true;
            AssetSetter.entities.clear();
            AssetSetter.entities.add(gp.player);
            System.out.println("loading game...");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "keys:":
                        for (int i = 1; i < parts.length; i++) {
                            gp.player.loadKey(Integer.parseInt(parts[i]));
                            System.out.println("loaded key #"+parts[i]);
                        }
                        break;
                    case "player-position:":
                        if (parts.length >= 3) gp.player.updateLocation(Integer.parseInt(parts[1])*gp.tileSize, Integer.parseInt(parts[2])*gp.tileSize);
                        System.out.println("player-position: ("+Integer.parseInt(parts[1])/gp.tileSize+" "+Integer.parseInt(parts[2])/gp.tileSize+")");
                        break;
                    case "player-health:":
                        if (parts.length >= 2) gp.player.loadHealth(Float.parseFloat(parts[1]));
                        System.out.println("player-health: "+Float.parseFloat(parts[1]));
                        break;
                    case "player-stamina:":
                        if (parts.length >= 2) gp.player.loadStamine(Float.parseFloat(parts[1]));
                        System.out.println("player-stamina: "+Float.parseFloat(parts[1]));
                        break;
                    case "seed:":
                        if (parts.length >= 2) gp.tileM.algorithm.setSeed(Long.parseLong(parts[1]));
                        System.out.println("seed: "+Float.parseFloat(parts[1]));
                        gp.tileM.mapTileNum = gp.tileM.algorithm.generateMap();
                        break;
                    case "M":
                        Silhouette monster = new Silhouette(gp);
                        if (parts.length >= 3) monster.updateLocation(Integer.parseInt(parts[1])*gp.tileSize, Integer.parseInt(parts[2])*gp.tileSize);
                        gp.aSetter.addEntity(monster);
                        break;
                    case "C":
                        ChestObject chest = new ChestObject(gp);
                        if (parts.length >= 3) chest.updateLocation(Integer.parseInt(parts[1])*gp.tileSize, Integer.parseInt(parts[2])*gp.tileSize);
                        if (parts.length >= 4 && parts[3].equals("D")) chest.disable();
                        gp.aSetter.addEntity(chest);
                        break;
                    case "D":
                        DoorObject door = new DoorObject(gp);
                        if (parts.length >= 3) door.updateLocation(Integer.parseInt(parts[1])*gp.tileSize, Integer.parseInt(parts[2])*gp.tileSize);
                        gp.tileM.algorithm.door.x = Integer.parseInt(parts[1]);
                        gp.tileM.algorithm.door.y = Integer.parseInt(parts[2]);
                        gp.aSetter.addEntity(door);
                        break;
                    case "K":
                        KeyObject key = new KeyObject(gp);
                        if (parts.length >= 3) key.updateLocation(Integer.parseInt(parts[1])*gp.tileSize, Integer.parseInt(parts[2])*gp.tileSize);
                        if (parts.length >= 4 && parts[3].equals("D")) key.disable();
                        if (parts.length >= 5) key.setId(Integer.parseInt(parts[4]));
                        gp.aSetter.addEntity(key);
                        break;

                }

            }
            System.out.println("loading completed.\n");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearGameFiles() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("game.txt"));
            System.err.println("clearing game files...");
            bw.write("");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    public void loadConfig() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

            // FULLSCREEN
            String s = br.readLine();
            gp.fullscreen = s.equals("On");

            // MUSIC VOLUME
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            // SFX VOLUME
            s = br.readLine();
            gp.sfx.volumeScale = Integer.parseInt(s);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
