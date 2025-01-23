package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    // movement flags
    public boolean upPressed, downPressed, leftPressed, rightPressed, shiftPressed;
    // game flags
    public boolean escapePressed;
    // debug flags
    public boolean debugMode;
    
    public KeyHandler (GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // DEBUG MODE
        // if (debugMode) {
        //     System.out.println("Key Pressed: " + KeyEvent.getKeyText(code));
        // }

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            if (gp.ui.gameSubState == 0) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 4;
                    }
                }
                else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum++;
                    gp.ui.commandNum %= 5;
                }
                if (code == KeyEvent.VK_ENTER) {
                    switch (gp.ui.commandNum) {
                        case 0:
                            gp.ui.gameSubState = 1;
                            break;
                        case 1:
                            // add later
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            System.exit(0);
                            break;
                    }
                }
            }
            else if (gp.ui.gameSubState == 1) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }
                else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum++;
                    gp.ui.commandNum %= 3;
                }
                if (code == KeyEvent.VK_ENTER) {
                    switch (gp.ui.commandNum) {
                        case 0:
                            // change character to Don
                            gp.gameState = gp.loadingState;
                            gp.ui.gameSubState = 1;
                            gp.ui.visibleDuration = 25;
                            break;
                        case 1:
                            // change character to Emma
                            gp.gameState = gp.loadingState;
                            gp.ui.gameSubState = 1;
                            gp.ui.visibleDuration = 25;
                            break;
                        case 2:
                            gp.ui.gameSubState = 0;
                            gp.ui.commandNum = 0;
                            break;
                    }
                }
            }
            
        }
        // PLAY STATE
        if (gp.gameState == gp.playState) {
            if (gp.ui.gameSubState == 1) {
                if (code == KeyEvent.VK_ENTER) {
                    gp.gameState = gp.titleState;
                    gp.ui.gameSubState = 0;
                }
                return;
            }
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_SHIFT) {
                shiftPressed = true;
            }

        }
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
                gp.stopMusic(true);

            }
            else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
        }
        if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_M) {
                gp.gameState = gp.titleState;
                gp.stopMusic(false);
            }
        }
        

        // DEBUG
        if (code == KeyEvent.VK_T) {
            debugMode = !debugMode;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        // DEBUG MODE
        // if (debugMode) {
        //     System.out.println("Key Released: " + KeyEvent.getKeyText(code));
        // }

        if (gp.gameState == gp.playState && gp.ui.gameSubState == 0) {
            if (code == KeyEvent.VK_W) {
                upPressed = false;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = false;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = false;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = false;
            }
            if (code == KeyEvent.VK_SHIFT) {
                shiftPressed = false;
            }
        }
        
    }
}
