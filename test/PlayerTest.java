package entities;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gamestates.Playing;
import java.awt.Point;

public class PlayerTest {

    private Player player;
    private Playing playing;

    @Before
    public void setUp() {
        // Mock del objeto Playing
        playing = new Playing(null); // Se pasa null porque no se usará el Game en estos tests
        player = new Player(100, 100, 64, 40, playing);
    }

    @Test
    public void vidaInicial() {
        assertEquals("La vida inicial debe ser igual a la salud máxima",
                player.maxHealth, player.currentHealth);
    }

    @Test
    public void vidaAumentando() {
        int healAmount = 3;
        player.currentHealth = 5; // Establecemos una salud menor al máximo
        player.changeHealth(healAmount);
        assertEquals("La vida debe aumentar correctamente",
                5 + healAmount, player.currentHealth);
    }

    @Test
    public void vidaDisminuyendo() {
        int damage = 3;
        player.changeHealth(-damage);
        assertEquals("La salud debe disminuir correctamente",
                player.maxHealth - damage, player.currentHealth);
    }

    @Test
    public void vidaBajoCero() {
        player.changeHealth(-player.maxHealth * 2); // Daño mayor que la salud máxima
        assertEquals("La vida no puede ser menor a cero",
                0, player.currentHealth);
    }

    @Test
    public void excederVidaMaxima() {
        player.changeHealth(player.maxHealth * 2); // Curación excesiva
        assertEquals("La vida no se pasa de la vida maxima",
                player.maxHealth, player.currentHealth);
    }



    @Test
    public void probarBarraDeVida() {
        int initialHealthWidth = player.healthWidth;
        player.changeHealth(-2); // Reduce la salud
        player.updateHealthBar();
        assertTrue("Barra de vida debe ser menor que antes",
                player.healthWidth < initialHealthWidth);
    }


}