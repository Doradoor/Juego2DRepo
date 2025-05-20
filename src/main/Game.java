package main;

import entities.Player;

import java.awt.*;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private Game game;

    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop(); //tiene q ser la unica cosa en cargar
    }

    private void initClasses() {
        player = new Player(200, 200);
    }

    /**
     * Metodo para iniciar el gameLoop en un thread diferente
     * de esta forma se separa del main thread y el juego pueda ir mucho mas fluido
     * No tendra tanto lag y no habra trafico
     * Al thread le pasamos un metodo llamado runnable es para pasar el codigo que queremos correr
     *
     */
    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Metodo para actualizar
     */
    public void update(){
        player.update();
    }

    public void render(Graphics g){
        player.render(g);
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while(true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate; //para saber que es hora de actualizar, de esta forma no perdemos tiempo en el gameloop
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

           if(deltaF >= 1){
               gamePanel.repaint();
               frames++;
               deltaF--;
           }

            /*
            if(now - lastFrame >= timePerFrame){
                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }
             */
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "| UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
    //getter para player
    public Player getPlayer() {
        return player;
    }
}
