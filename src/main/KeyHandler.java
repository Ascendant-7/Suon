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

        // TITLE STATE
        if (gp.gameState == gp.titleState && gp.ui.gameSubState != 4) {
            
            if (gp.ui.gameSubState == 0) titleMenu(code);
            else if (gp.ui.gameSubState == 1) characterMenu(code);
            else if (gp.ui.gameSubState == 2) RulesNTuts(code);
            
        }
        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            if (gp.ui.gameSubState == 1 || gp.ui.gameSubState == 2) finishedGame(code);
            else getInGameKeys(code);
        }
        
        // PAUSE / OPTIONS STATE
        else if (gp.gameState == gp.pauseState || (gp.gameState == gp.titleState && gp.ui.gameSubState == 4)) {

            handlePauseNResume(code);
            backOption(code);
            handleOptions(code);
            
        }
        

        // DEBUG
        if (code == KeyEvent.VK_T) {
            debugMode = !debugMode;
            lightingOn = !lightingOn;
        }
    }
    public void titleMenu(int code) {
        handleScrolling(code, 0, 5);
        if (code == KeyEvent.VK_ENTER) {
            switch (gp.ui.commandNum) {
                case 0:
                    handleGameLoad(code, true);
                    break;
                case 1:
                    handleGameLoad(code, false);
                    break;
                case 2:
                    gp.ui.gameSubState = 2;
                    break;
                case 3:
                    gp.ui.gameSubState = 4;
                    gp.ui.commandNum = 0;
                    gp.ui.optionNum = -1;
                    break;
                case 4:
                    System.exit(0);
                    break;
            }
        }
    }

    public void handleGameLoad(int code, boolean newGame) {
        if (newGame) {
            System.out.println("loading new game...");
            gp.loadGame(true);
        }
        else {
            System.out.println("loading saved game...");
            // load saved game
            if (gp.player.getEntityState() == EntityState.DEAD || gp.player.getEntityState() == EntityState.WON) {
                System.out.println("you died in the last game, creating a new one...");
                gp.loadGame(true);
            }
            else {
                gp.loadGame(false);
                System.err.println("loading saved game...");
            }
        }
        gp.gameState = gp.loadingState;
        gp.ui.gameSubState = 1;
        gp.ui.visibleDuration = 25;
        gp.ui.commandNum = 0;
    }


    public void RulesNTuts(int code) {
        if (code == KeyEvent.VK_ESCAPE) gp.ui.gameSubState = 0;
    }

    public void finishedGame(int code) {
        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.titleState;
            gp.ui.gameSubState = 0;
        }
    }

    public void getInGameKeys(int code) {
        handlePauseNResume(code);
        getMovementKeys(code);
        // if (code == KeyEvent.VK_E) {
        //     interactPressed = true;
        // }
        
        
    }

    public void getMovementKeys(int code) {
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

    public void handlePauseNResume(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
                gp.ui.commandNum = 0;
                gp.ui.optionNum = -1;
                gp.PauseMusic();
            }
            else if (gp.gameState == gp.pauseState && gp.ui.optionNum == -1) {
                gp.gameState = gp.playState;
                gp.ui.commandNum = 0;
                gp.playMusic(0);
            }
            else if (gp.gameState == gp.titleState && gp.ui.optionNum == -1) {
                gp.ui.gameSubState = 0;
                gp.ui.commandNum = 0;
            }
        }
    }

    public void backOption(int code) { if (code == KeyEvent.VK_ESCAPE && gp.ui.optionNum != -1) gp.ui.optionNum = -1; }

    public void handleOptions(int code) {
        switch (gp.ui.optionNum) {
            case -1:
                handleMainOptions(code);
                break;
            case 0:
                toggleFullscreen(code);
                break;
            case 1:
                handleSoundOptions(code);
                break;
            default:
                break;
        }
    }

    public void handleMainOptions(int code) {
        handleScrolling(code, 0, 4);
        handleMainOptSelection(code);
    }

    public void handleScrolling(int code, int tracker, int options) {
        switch (tracker) {
            case 0:
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) gp.ui.commandNum= options - 1;
                }
                else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum++;
                    gp.ui.commandNum %= options;
                }
                break;
            case 1:
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    gp.ui.settingNum--;
                    if (gp.ui.settingNum < 0) gp.ui.settingNum= options - 1;
                }
                else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    gp.ui.settingNum++;
                    gp.ui.settingNum %= options;
                }
                break;
        }
    }

    public void handleMainOptSelection(int code) {
        if (code == KeyEvent.VK_ENTER) {
            gp.ui.optionNum = gp.ui.commandNum;
            if (gp.ui.optionNum == 3) returnToMenu();
        }
    }

    public void returnToMenu() {
        if (gp.gameState == gp.pauseState) {
            gp.stopMusic();
            gp.config.saveGame();
            gp.gameState = gp.titleState;
        }
        gp.ui.gameSubState = 0;
        gp.ui.commandNum = 0;
        gp.ui.optionNum = -1;
        gp.ui.settingNum = 0;
    }

    public void toggleFullscreen(int code) { if (code == KeyEvent.VK_ENTER) gp.fullscreen = !gp.fullscreen; }

    public void handleSoundOptions(int code) {
        switch (gp.ui.settingNum) {
            case 0:
                adjustSound(code, gp.music);
                break;
            case 1:
                adjustSound(code, gp.sfx);
                break;
        }
        handleScrolling(code, 1, 2);
    }

    public void adjustSound(int code, Sound sound) {
        // MUSIC SETTING
        if ((code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) && sound.volumeScale > 0) {
            gp.playSFX(1);
            sound.volumeScale--;
        }
        else if ((code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && sound.volumeScale < 5) {
            gp.playSFX(1);
            sound.volumeScale++;
        }
        if (gp.gameState == gp.pauseState)
            sound.checkVolume();
    }

    public void characterMenu(int code) {
        handleScrolling(code, 0, 3);
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
