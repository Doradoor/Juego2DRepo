package inputs;

import gamestates.Gamestate;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static utilz.Constants.Directions.*;

public class Keyboardinputs implements KeyListener {

    private GamePanel gamePanel;

    /**
     * @param gamePanel  Acceso al Gamepanel dentro de los keyboardinputs
     */
    public Keyboardinputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().keyPressed(e);
                break;
            default:
                break;
        }
    }

    /**
     * Metodo que para cuando presionamos una tecla estamos
     * seteando la direccion y cuando arroja que no hay direcciones
     *
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            default:
                    break;
        }
    }
}
