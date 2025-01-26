package main;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	// SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile

    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 960 px
    public final int screenHeight = maxScreenRow * tileSize; // 576 px

    // FULLSCREEN SETTING
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // WORLD SETTINGS
    public final int mapWorldCol = 51;
    public final int mapWorldRow = 51;

    // RENDER SETTINGS
    int FPS = 60;

    // GAME COMPONENTS
    Thread gameThread;
    public UtilityTool uTool = new UtilityTool(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Player player = new Player(this);
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Sound music = new Sound();
    public Sound sfx = new Sound();
    public UI ui = new UI(this);
    
    // GAME OBJECTS
    // public Player player = new Player(this);
    // public Entity[] obj = new Entity[10];
    // public ArrayList<Entity> monsters = new ArrayList<>();
    // ArrayList<Entity> entityList = new ArrayList<>();
    // game states
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int loadingState = 3;
    public final int optionState = 4;

    // CONSTRUCTOR
    public GamePanel() {
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setUpGame() {
        gameState = loadingState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
        setFullScreen();
    }

    public void setFullScreen() {
        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);
        
        // GET FULLSCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
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
                drawToTempScreen();
                drawToScreen();
                delta = 0;
            }
        }
        
    }

    // UPDATE FUNCTION
    public void update() {

        if (gameState == playState && ui.gameSubState == 0) {
            for (Entity e : AssetSetter.entities) {
                e.update();
            }
        }
    }
    public void drawToTempScreen() {
        // TITLE SCREEN
        if (gameState == titleState || gameState == loadingState) {
            ui.draw(g2);
        }
        // OTHERS
        else {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            // TILE
            tileM.draw(g2);

            AssetSetter.sortEntities();
            for (Entity e : AssetSetter.entities) {
                e.draw(g2);
            }
            // UI
            ui.draw(g2);
        }
    }

    public void drawToScreen() {

        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(boolean pause) {
        if (pause)
            music.pause();
        else
            music.stop();
    }

    public void playSFX(int i) {
        sfx.setFile(i);
        sfx.play();
    }
}
