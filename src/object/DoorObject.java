package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class DoorObject extends SuperObject{

    public DoorObject(GamePanel gp) {
        
        name = "Door";
        try {
            
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
