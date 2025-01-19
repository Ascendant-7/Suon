package main;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	// SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 768 px
    public final int screenHeight = maxScreenRow * tileSize; // 576 px

    // WORLD SETTINGS
    public final int mapWorldCol = 51;
    public final int mapWorldRow = 51;

    // RENDER SETTINGS
    int FPS = 60;

    // GAME COMPONENTS
    // thread and managers
    Thread gameThread;
    KeyHandler keyH = new KeyHandler(this);
    TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Sound music = new Sound();
    public Sound sfx = new Sound();
    public UI ui = new UI(this);
    //players and objects
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10];
    // game states
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;

    // CONSTRUCTOR
    public GamePanel() {
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setUpGame() {

        aSetter.setObject();
        playMusic(0);
        gameState = titleState;
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

        if (gameState == playState) {
            player.update();
        }
        if (keyH.escapePressed) {
            System.exit(0);
        }
    }
    // PAINT COMPONENT FUNCTION
    public void paintComponent(Graphics g) {
 
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        }
        // OTHERS
        else {
            // TILE
            tileM.draw(g2);

            // OBJECTS
            for (SuperObject object : obj) {
                if (object != null) {
                    object.draw(g2, this);
                }
            }

            // PLAYER
            player.draw(g2);

            // UI
            ui.draw(g2);
        }

        // DEBUG
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            System.out.println("Draw Time: "+passed);
        }
        

        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSFX(int i) {
        sfx.setFile(i);
        sfx.play();
    }
}
