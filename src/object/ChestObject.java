package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class ChestObject extends SuperObject {

    public ChestObject(GamePanel gp) {

        name = "Chest";
        try {
            
            image = ImageIO.read(getClass().getResourceAsStream("/objects/treasure_chest.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
