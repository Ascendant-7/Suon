package main;

import javax.swing.JFrame;

public class Main {

    public static JFrame window;
    public static void main(String[] args) throws Exception {

        window = new JFrame("Suon");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        gamePanel.config.loadConfig();
        if (gamePanel.fullscreen)
            window.setUndecorated(true);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}
