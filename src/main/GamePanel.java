package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/** JPanel nos va a permitir dibujar en la pantalla del juego y es parte de JFrame
 * Basicamente la
 */
public class GamePanel extends JPanel{
    public GamePanel() {

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    /** PaintComponent se va a llamar cuando demos al play button
     * Se utiliza para dibujar algo
     * @param g decribimos Graphics como un paintbush
     *          O sea que lo usamos para dibujar
     * Jpanel le indicara a Graphics donde puede dibujar
     */
    public void paintComponent(Graphics g) {
        super.paintComponents(g); //LLama a la super clase JComponent y tmb limpia la pantalla para nuevas imagenes
        g.fillRect(100,100,200,50);
    }
}
