package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private ObjectManager objectManager;

    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH); //Distancia desde el inicio de la camara hasta el borde derecho
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean lvlCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

        calcLvlOffset();
        loadStartLevel();
    }
    public void loadNextLevel(){
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }
    private void loadStartLevel() {

        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());

    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    /**
     * Metodo para ininciar las clases para el juego
     * levelManager - gestor de niveles que controla los mapas
     * Inicia la instancia de jugador definiendo su posicion inical y el tamaño de hitbox
     * Datos del nivel actual
     */
    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadlvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    //getter para player
    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (!gameOver) {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);

            checkCloseToBorder();
        }
    }

    /**
     * Metodo para ajustar el desplazamiento del nivel de forma horizontal
     * dependiendo la posicion del jugador y no se pierda de un lugar visible
     * <p>
     * Se calcula usando la dif entre eljugador y el desplazamiento del nivel
     * si diff es mayor a los bordes que estan permitidos signfica que se debe ajustar xLvlOFFSET
     */
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x; //hitbox obtiene la posicion x para la proximidad de los bordes visibles
        int diff = playerX - xLvlOffset; //lvloffset es el desplazamiento del nivel en la camara, controla el desplazamiento

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;

    }

    /**
     * Metodo para dibujar durante el juego
     */
    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);


        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    /**
     * Metodo para dibujar las nubes varias veces por el mapa
     * Se utiliza xLvlOffset para dar un aspecto como que se estuvieran moviendo mientras avanza el jugador
     * Tmb la posicion de las nubes pequeñas utiliza un metodo que devuelve una posicion random
     *
     * @param g
     */
    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();

    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if(e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void setLevelCompleted(boolean levelCompleted){
            this.lvlCompleted = levelCompleted;
        }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }


    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isLevelCompleted() {
        return lvlCompleted;
    }
}
