package tile;

import java.awt.Graphics2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public EllersAlgorithm algorithm;
    public Tile[] tile;
    public int mapTileNum[][];
    public TileManager(GamePanel gp) {
        
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.mapWorldCol][gp.mapWorldRow];
        
        getTileImage();
        // loadMap("/maps/test_map.txt");
        algorithm = new EllersAlgorithm(gp);
    }

    public void getTileImage() {
        
        setup(0, "floor", false);
        setup(1, "wall_mid", true);
        setup(2, "wall_top", true);
    }

    public void setup(int index, String imagePath, boolean collision) {

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+imagePath+".png"));
            tile[index].image = gp.uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
            tile[index].name = imagePath;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0, row = 0;

            while (col < gp.mapWorldCol && row < gp.mapWorldRow) {
                String line = br.readLine();

                while (col < gp.mapWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.mapWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        int playerScreenX = gp.screenWidth/2 - gp.tileSize/2;
        int playerScreenY = gp.screenHeight/2 - gp.tileSize/2;

        while (worldCol < gp.mapWorldCol && worldRow < gp.mapWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.getWorldX() + playerScreenX;
            int screenY = worldY - gp.player.getWorldY() + playerScreenY;

            if (screenX + gp.tileSize > 0 && 
                screenX - gp.tileSize < playerScreenX*2 && 
                screenY + gp.tileSize > 0 && 
                screenY - gp.tileSize < playerScreenY*2)
            // if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            //     worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            //     worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            //     worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            worldCol++;

            if (worldCol == gp.mapWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
