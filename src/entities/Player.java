package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;


import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // Saltando / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // status bar ui
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);

    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int)(4 * Game.SCALE);
    private int healthBarXStart = (int)(34 * Game.SCALE);
    private int healthBarYStart = (int)(14 * Game.SCALE);

    private int maxHealth = 10;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    private Rectangle2D.Float attackBox; //para la espada
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitbox(x, y, (int) (20 * Game.SCALE), (int)(27 * Game.SCALE));
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    /**
     * Metodo para actualizar jugador
     * updatePos hara que inicie Moving por default en false
     * setAnimation revisa si moving es o no true.
     */
    public void update() {
        updateHealthBar();

        if (currentHealth <= 0){
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        
        updatePos();
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);

    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    /**
     * Renderiza al jugador en pantalla con sus animaciones y posición actual.
     * Si el jugador da la vuelta, el sprite y las animaciones tambien se voltean horizontalmente.
     *
     * @param g El contexto grafico donde se dibujará el jugador.
     * @param lvlOffset Desplazamiento del nivel en el eje X para ajustar la posición relativa del jugador.
     *
     * flipW invierte el ancho del sprite cuando se multiplica por -1 cuando el jugador cambia de direccion haciendo que se dibuje el sprite volteado en el eje horizontal
     * flipX ajusta la posicion horizontal del sprite, corrigiendo el desplazamiento generado al voltear
     */

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex],
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset),
                width * flipW, height, null);
        //drawHitbox(g);
       // drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }


    /**
     * Este metodo maneja la animacion del player para que las animaciones sean fluidas
     * Anitick es para el tiempo acumulado antes de un cambio de frame
     * aniSpeed la velocidad antes de cambiar al siguiente fram
     * aniIndex fotograma dentro de la animaicion (El sprite esta en un array por cual se va desplazando)
     * playerAction para determinar cual conjunto usar
     * getsprite va a devolver el numero de fotogramas para la accion que realiza
     */
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteCount(playerAction)) { //si es mas grande que el tamaño del array de animacion se resetea y tmb dependiendo de la acccion del jugador obtenemos una cantidad de index
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    //Si nos estamos moviendo entonces se toma como running, si no como Idle
    private void setAnimation() {
        int startAni = playerAction;
        if(moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if(inAir){
            if(airSpeed <0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }
        if(attacking) {
            playerAction = ATTACK;
            if(startAni != ATTACK){
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if(startAni != playerAction)
            resetAniTick();
    }

    //metodo para resetear la animacion
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Metodo para que se actualice la posicion del jugador segun la entrada o condiciones como la gravedad/colisiones
     * Sirve para controlar los movimientos e interacciones del jugador
     */
    private void updatePos() {
        //por default para que quede en idle
        moving = false; //no hay movimiento

        if (jump)
            jump();
       // if(!left && !right && !inAir) //no hay movimiento horizontal
          //  return;
        if(!inAir)
            if((!left && !right) || (right && left))
                return;

        float xSpeed = 0; //Velocidad temporal de x y

        if(left) {
            xSpeed -= playerSpeed; //si solo estamos presionando izq
            flipX = width;
            flipW = -1;
        }
        if(right) {//si solo estamos presionando derecha
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
        }
        // movimiento en el aire
        if (inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else{
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }else
            updateXPos(xSpeed);

        moving = true;
    }



    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
        }else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value){
        currentHealth += value;
        if(currentHealth <= 0) {
            currentHealth = 0;
            //gameover
        }else if(currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    /** Este metodo va a obtener la imagen del jugador desde loadsave y cargarla animaciones
     */
    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            animations = new BufferedImage[7][8];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j* 40, 64, 40);

            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        }
    /** Inicializar o cambiar los datos del nivel en el juego
     */
    public void loadlvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }


    public void resetDirBooleans(){
        left = false;
        up = false;
        right = false;
        down = false;
    }
    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
    public void setJump(boolean jump){
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}

