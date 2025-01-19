package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class KeyObject extends SuperObject{

    public KeyObject(GamePanel gp) {
        
        name = "Key";
        try {
            
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
