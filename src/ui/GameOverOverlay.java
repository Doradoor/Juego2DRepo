package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {

    private Playing playing;
    public GameOverOverlay(Playing playing){
        this.playing = playing;
    }
    /**
     * Dibuja la imagen de "Game Over" en la pantalla.
     *
     * @param g objeto graphics para dibujar
     *
     * Este metodo crea un fondo semitransparente para mostrar el texto de "Game Over"
     * al centro de la pantalla y un mensaje indicando que el jugador debe
     * presionar la tecla Esc para volver al menu principal
     */


    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Game.GAME_WIDTH/2 , 150);
        g.drawString("Presiona Esc para volver al menu principal", Game.GAME_WIDTH/2, 300);
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
