package main;

import inputs.Keyboardinputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;


/** JPanel nos va a permitir dibujar en la pantalla del juego y es parte de JFrame
 *
 */
public class GamePanel extends JPanel{

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {

        mouseInputs = new MouseInputs(this);
        this.game = game;

        setPanelSize();
        addKeyListener(new Keyboardinputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    /**
     * Las imagenes sera 32 px x 32 px para que nada se salga del borde
     */
    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void updateGame(){

    }
    /** PaintComponent se va a llamar cuando demos al play button
     * Se utiliza para dibujar algo
     * @param g decribimos Graphics como un paintbush
     *          O sea que lo usamos para dibujar
     * Jpanel le indicara a Graphics donde puede dibujar
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //LLama a la super clase JComponent y tmb limpia la pantalla para nuevas imagenes

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
