package object;

import entity.Entity;
import main.GamePanel;

public class KeyObject extends Entity{

    public KeyObject(GamePanel gp) {
        super(gp);
        name = "Key";
        down1 = gp.uTool.setup("/objects/key");
    }
}
