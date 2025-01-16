package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ChestObject extends SuperObject {

    public ChestObject() {
        name = "Chest";
        try {
            
            image = ImageIO.read(getClass().getResourceAsStream("/objects/treasure_chest.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
