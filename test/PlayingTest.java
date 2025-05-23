package gamestates;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import entities.Player;
import main.Game;


public class PlayingTest {

    private static class DummyComponent extends java.awt.Component {}

    private Playing playing;
    private Game game;

    @Before
    public void setUp() {
        game = new Game(); // Necesitarás un constructor vacío o mockear esta clase
        playing = new Playing(game);
    }

    @Test
    public void testInitialState() {
        assertFalse("El juego no debe estar pausado al inicio", playing.isPaused());
        assertFalse("No debe haber game over al inicio", playing.isGameOver());
        assertFalse("No debe estar completado el nivel al inicio", playing.isLevelCompleted());
    }

    @Test
    public void testPlayerInitialization() {
        Player player = playing.getPlayer();
        assertNotNull("El jugador debe estar inicializado", player);
        assertTrue("La salud del jugador debe ser mayor que 0", player.getCurrentHealth() > 0);
    }

    @Test
    public void testPauseFunctionality() {
        // Simular presionar ESC para pausar
        playing.keyPressed(new KeyEvent(new DummyComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, KeyEvent.CHAR_UNDEFINED));
        assertTrue("El juego debe estar pausado", playing.isPaused());

        // Simular presionar ESC nuevamente para despausar
        playing.keyPressed(new KeyEvent(new DummyComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, KeyEvent.CHAR_UNDEFINED));
        assertFalse("El juego no debe estar pausado", playing.isPaused());
    }

    @Test
    public void testPlayerMovementKeys() {
        Player player = playing.getPlayer();

        // Simular presionar tecla A (izquierda)
        playing.keyPressed(new KeyEvent(new DummyComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED));
        assertTrue("El jugador debe moverse a la izquierda", player.isLeft());

        // Simular soltar tecla A
        playing.keyReleased(new KeyEvent(new DummyComponent(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED));
        assertFalse("El jugador debe dejar de moverse a la izquierda", player.isLeft());

        // Simular presionar tecla D (derecha)
        playing.keyPressed(new KeyEvent(new DummyComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, KeyEvent.CHAR_UNDEFINED));
        assertTrue("El jugador debe moverse a la derecha", player.isRight());
    }

    @Test
    public void testJumpFunctionality() {
        Player player = playing.getPlayer();

        // Simular presionar SPACE (saltar)
        playing.keyPressed(new KeyEvent(new DummyComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED));
        assertTrue("El jugador debe estar saltando", player.isJump());
    }

    @Test
    public void testGameOverWhenPlayerHealthZero() {
        Player player = playing.getPlayer();
        player.changeHealth(-player.getCurrentHealth()); // Reducir salud a 0

        playing.update(); // Actualizar el estado del juego

        assertTrue("Debería activarse game over cuando la salud del jugador llega a 0",
                playing.isGameOver());
    }

    @Test
    public void testResetAll() {
        // Cambiar varios estados
        playing.setGameOver(true);
        playing.getPlayer().changeHealth(-2);

        playing.resetAll();

        assertFalse("resetAll() debe resetear game over", playing.isGameOver());
        assertEquals("resetAll() debe restaurar la vida del jugador",
                playing.getPlayer().getMaxHealth(),
                playing.getPlayer().getCurrentHealth());
    }

    @Test
    public void testCheckEnemyHit() {
        Rectangle2D.Float attackBox = new Rectangle2D.Float(100, 100, 50, 50);
        playing.checkEnemyHit(attackBox);
    }

}