package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null); // Empiece en el centro
        jframe.setResizable(false); //Para que no se vea mal la ventana (No hacer resize)
        jframe.pack(); //Le decimos a JFrame que se ajuste a la talla de los componentes
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener(){
            @Override
            public void windowLostFocus (WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e){

            }
        });

    }
}
