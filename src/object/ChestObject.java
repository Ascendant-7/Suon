package object;

import java.awt.Graphics2D;

import entity.Entity;
import enums.ID;
import main.GamePanel;

public class ChestObject extends Entity {

    boolean enabled = true;
    public ChestObject(GamePanel gp) {

        super(gp);

        id = ID.CHEST;
    }

    @Override
    public void getImage() {
        suffix = "/objects/";
        imagePath[0] = "treasure_chest";
        super.getImage();
    }
    @Override
    public void draw(Graphics2D g2) {
        if (enabled) super.draw(g2);
    }
    public void disable() {enabled = false; }
    public boolean isEnabled() {return enabled; }
}
