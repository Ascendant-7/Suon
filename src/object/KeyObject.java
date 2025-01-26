package object;

import java.awt.Graphics2D;

import entity.Entity;
import enums.ID;
import main.GamePanel;

public class KeyObject extends Entity{

    static int record = 0;
    int key_id;
    boolean enabled = true;
    public KeyObject(GamePanel gp) {

        super(gp);
        id = ID.KEY;
        key_id = record;
        record++;
    }
    @Override
    public void getImage() {
        suffix = "/objects/";
        imagePath[0] = "key";
        super.getImage();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (enabled) super.draw(g2);
    }
    public int getKeyId() { return key_id; }
    public void disable() {enabled = false; }
    public boolean isEnabled() {return enabled; }
}
