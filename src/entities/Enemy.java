package entities;

import gamestates.Playing;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

/** Clase abstratca que extiende Entity
 * Esta clase va a representar a un enemigo en el juego y manejar animacion y tipo de enemigo
 */
public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected int attackBoxOffsetX;



    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }


    protected void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    protected void updateAttackBoxFlip() {
        if (walkDir == RIGHT)
            attackBox.x = hitbox.x + hitbox.width;
        else
            attackBox.x = hitbox.x - attackBoxOffsetX;

        attackBox.y = hitbox.y;
    }

    protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
        this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
    }

    /**
     * Verifica si es necesario realizar la inicialización inicial del enemigo.
     * Si el enemigo no está sobre el suelo, se establece que está en el aire.
     * Marca la primera actualización como completada.
     *
     * @param lvlData Matriz bidimensional que representa los datos del nivel actual,
     *                incluyendo los obstáculos y plataformas
     */

    protected void firstUpdateCheck(int[][] lvlData){
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;

    }


    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
            if (IsEntityInWater(hitbox, lvlData))
                hurt(maxHealth);
        }
    }

    /**
     * Actualiza el comportamiento del enemigo cuando está en el aire.
     * Aplica gravedad y movimiento horizcontal
     *
     * Verifica si el enemigo ha alcanzado el suelo o algún otro límite de colision
     *
     * @param lvlData Matriz 2d que representa los datos del nivel actual,
     *                incluyendo los obstáculos y plataformas
     */

    protected void updateInAir(int[][] lvlData){
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed); //manejar caida del enemigo
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }
    /**
     * Controla el movimiento horizontal del enemigo. Verifica si puede moverse
     * hacia la dirección indicada y si el suelo está disponible en esa dirección.
     * Cambia de direccion cuando colisiona
     *
     * @param lvlData Matriz 2d que representa los datos del nivel actual,
     *                incluyendo los obstáculos y plataformas.
     */

    protected void move(int[][] lvlData){
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }
    /**
     * Ajusta la direccion del movimiento para que apunte hacia el jugador
     * @param player Referencia al jugador, usada para obtener su posicion
     */

    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }
    /**
     * Verifica si la entidad tiene linea de vision hacia el jugador
     * Esto incluye comprobar que el jugador esta en la misma fila de tiles
     * y que no hay obstaculos entre ellos
     *
     * @param lvlData Matriz de datos del nivel indicando los tiles solidos
     * @param player Referencia al jugador, usada para obtener su posicion
     * @return true si la entidad puede ver al jugador, {@code false} de lo contrario
     */

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }

        return false;
    }

    /**
     * Comprueba si el jugador esta dentro del rango de vision de la entidad.
     * @param player Referencia al jugador, usada para obtener su posicion.
     * @return true si el jugador esta dentro del rango, {@code false} de lo contrario.
     */

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    /**
     * Verifica si el jugador esta lo suficientemente cerca como para que la entidad realice un ataque
     *
     * @param player Referencia al jugador, usada para obtener su posicion
     * @return true si el jugador esta dentro del rango de ataque, false de lo contrario
     */

    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        switch (enemyType) {
            case CRABBY -> {
                return absValue <= attackDistance;
            }
            case SHARK -> {
                return absValue <= attackDistance * 2;
            }
        }
        return false;
    }



    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(DEAD);
        else {
            newState(HIT);
            if (walkDir == LEFT)
                    pushBackDir = RIGHT;
			else
            pushBackDir = LEFT;
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType), this);
		else {
                if (enemyType == SHARK)
                    return;
        }
        attackChecked = true;
    }

    /** Metodo para controlar la actualizacioon del indice de animacion
     */

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                if (enemyType == CRABBY || enemyType == SHARK) {
                    aniIndex = 0;

                    switch (state) {
                        case ATTACK, HIT -> state = IDLE;
                        case DEAD -> active = false;
                    }
                } else if (enemyType == PINKSTAR) {
                    if (state == ATTACK)
                        aniIndex = 3;
                    else {
                        aniIndex = 0;
                        if (state == HIT) {
                            state = IDLE;

                        } else if (state == DEAD)
                            active = false;
                    }
                }
            }
        }
    }


    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
        pushDrawOffset = 0;
    }

    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }



    public boolean isActive(){
        return active;
    }

    public float getPushDrawOffset() {
        return pushDrawOffset;
    }


}
