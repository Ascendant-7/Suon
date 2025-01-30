package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enums.EntityState;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    // movement flags
    public boolean upPressed, downPressed, leftPressed, rightPressed, shiftPressed;
    // game flags
    public boolean escapePressed, enterPressed, interactPressed, lightingOn = true;
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
        if (gp.gameState == gp.titleState && gp.ui.gameSubState != 4) {
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
                            System.out.println("loading new game...");
                            gp.setUpNewGame();
                            gp.gameState = gp.loadingState;
                            gp.ui.gameSubState = 1;
                            gp.ui.visibleDuration = 25;
                            gp.ui.commandNum = 0;
                            break;
                        case 1:
                        System.out.println("loading saved game...");
                            // load saved game
                            if (gp.player.getEntityState() == EntityState.DEAD || gp.player.getEntityState() == EntityState.WON) {
                                System.out.println("you died in the last game, creating a new one...");
                                gp.setUpNewGame();
                            }
                            else gp.loadGame(false);
                            gp.gameState = gp.loadingState;
                            gp.ui.gameSubState = 1;
                            gp.ui.visibleDuration = 25;
                            break;
                        case 2:
                            break;
                        case 3:
                            gp.ui.gameSubState = 4;
                            gp.ui.commandNum = 0;
                            gp.ui.optionNum = -1;
                            break;
                        case 4:
                            gp.config.saveGame();
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
        else if (gp.gameState == gp.playState) {
            if (gp.ui.gameSubState == 1) {
                if (code == KeyEvent.VK_ENTER) {
                    gp.gameState = gp.titleState;
                    gp.ui.gameSubState = 0;
                }
                return;
            }
            else if (gp.ui.gameSubState == 2) {
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
            if (code == KeyEvent.VK_E) {
                interactPressed = true;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.pauseState;
                    gp.ui.commandNum = 0;
                    gp.ui.optionNum = -1;
                    gp.stopMusic(true);
                }
            }
        }
        
        // PAUSE / OPTIONS STATE
        else if (gp.gameState == gp.pauseState || (gp.gameState == gp.titleState && gp.ui.gameSubState == 4)) {
            if (code == KeyEvent.VK_M) {
                gp.gameState = gp.titleState;
                gp.ui.gameSubState = 0;
            }
            // ESCAPE RIGHT PANEL
            else if (code == KeyEvent.VK_ESCAPE && gp.ui.optionNum != -1) {
                gp.ui.optionNum = -1;
            }
            // ESCAPE OPTION MENU FROM THE GAME
            else if (code == KeyEvent.VK_ESCAPE && gp.ui.optionNum == -1 && gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
                gp.ui.commandNum = 0;
                gp.playMusic(0);
            }
            // ESCAPE OPTION MENU FROM THE TITLE
            else if (code == KeyEvent.VK_ESCAPE && gp.ui.optionNum == -1 && gp.gameState == gp.titleState) {
                gp.ui.gameSubState = 0;
                gp.ui.commandNum = 0;
            }
            switch (gp.ui.optionNum) {
                case -1:
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        gp.ui.commandNum--;
                        if (gp.ui.commandNum < 0) {
                            gp.ui.commandNum = 3;
                        }
                    }
                    else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                        gp.ui.commandNum++;
                        gp.ui.commandNum %= 4;
                    }
                    if (code == KeyEvent.VK_ENTER) {
                        gp.ui.optionNum = gp.ui.commandNum;
                        if (gp.ui.optionNum == 3) {
                            gp.gameState = gp.titleState;
                            gp.ui.gameSubState = 0;
                            gp.ui.commandNum = 0;
                            return;
                        }
                            
                    }
                    break;
                case 0:
                    if (code == KeyEvent.VK_ENTER)
                        gp.fullscreen = !gp.fullscreen;
                    break;
                case 1:
                    
                    switch (gp.ui.settingNum) {
                        case 0:
                            // MUSIC SETTING
                            if ((code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) && gp.music.volumeScale > 0) {
                                gp.playSFX(1);
                                gp.music.volumeScale--;
                            }
                            else if ((code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && gp.music.volumeScale < 5) {
                                gp.playSFX(1);
                                gp.music.volumeScale++;
                            }
                            if (gp.gameState == gp.pauseState)
                                gp.music.checkVolume();
                            break;
                        case 1:
                            // MUSIC SETTING
                            if ((code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) && gp.sfx.volumeScale > 0) {
                                gp.playSFX(1);
                                gp.sfx.volumeScale--;
                            }
                            else if ((code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && gp.sfx.volumeScale < 5) {
                                gp.playSFX(1);
                                gp.sfx.volumeScale++;
                            }
                            if (gp.gameState == gp.pauseState)
                                gp.sfx.checkVolume();
                            break;
                    }
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        gp.ui.settingNum--;
                        if (gp.ui.settingNum < 0) {
                            gp.ui.settingNum = 1;
                        }
                    }
                    else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                        gp.ui.settingNum =(gp.ui.settingNum + 1) % 2;
                    }

                    break;
                case 3:
                    if (code == KeyEvent.VK_ENTER) {
                        if (gp.gameState == gp.pauseState) {

                            gp.stopMusic(true);
                            gp.gameState = gp.titleState;
                        }
                        gp.ui.gameSubState = 0;
                        gp.ui.commandNum = 0;
                        gp.ui.optionNum = -1;
                        gp.ui.settingNum = 0;
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
        

        // DEBUG
        if (code == KeyEvent.VK_T) {
            debugMode = !debugMode;
            lightingOn = !lightingOn;
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
            if (code == KeyEvent.VK_E) {
                interactPressed = false;
            }
        }
        
    }
}
