package main;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 768 px
    public final int screenHeight = maxScreenRow * tileSize; // 576 px

    // WORLD SETTINGS
    public final int mapWorldCol = 50;
    public final int mapWorldRow = 50;
    public final int worldWidth = mapWorldCol * tileSize;
    public final int worldHeight = mapWorldRow * tileSize;

    // RENDER SETTINGS
    int FPS = 60;

    // GAME COMPONENTS
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);

    // CONSTRUCTOR
    public GamePanel() {
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    // START FUNCTION
    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    // RUN FUNCTION
    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; // drawInterval -> second per frame (0.016s)
        double delta = 0;
        long lastTime = System.nanoTime(); 
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                
                update();
                repaint();
                delta = 0;
            }
        }
        
    }

    // UPDATE FUNCTION
    public void update() {

        player.update();
    }
    // PAINT COMPONENT FUNCTION
    public void paintComponent(Graphics g) {
 
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}
