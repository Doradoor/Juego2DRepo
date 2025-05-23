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
     * Dibuja la pantalla de Game Over
     *
     * @param g Objeto Graphics utilizado para dibujar.
     */

    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Game.GAME_WIDTH/2 , 150);
        g.drawString("Presiona Esc para volver al menu principal", Game.GAME_WIDTH/2, 300);
    }

    /**
     * Maneja eventos de teclado, permitiendo al jugador regresar al menú principal al presionar "Esc".
     *
     * @param e Evento de teclado.
     */

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
