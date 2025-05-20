package entities;

import utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.Directions.*;
import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
    }

    /**
     * Metodo para actualizar jugador
     * updatePos hara que inicie Moving por default en false
     * setAnimation revisa si moving es o no true.
     */
    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    /**
     * Se utiliza para dibujar al jugador
     * @param g decribimos Graphics como un paintbush
     *          O sea que lo usamos para dibujar
     * Jpanel le indicara a Graphics donde puede dibujar
     */
    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, width,height,null);

    }




    private void updateAnimationTick() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteCount(playerAction)) { //si es mas grande que el tamaño del array de animacion se resetea y tmb dependiendo de la acccion del jugador obtenemos una cantidad de index
                aniIndex = 0;
                attacking = false;
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
        if(attacking)
            playerAction = ATTACK_1;
        if(startAni != playerAction)
            resetAniTick();
    }

    //metodo para resetear la animacion
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Metodo para que la imagen se mueva
     */
    private void updatePos() {
        //por default para que quede en idle
        moving = false; //se manda a set animation
        if(left && !right){
            x -= playerSpeed; //si solo estamos presionando izq
            moving = true; //se manda a set animation
        }else if(right && !left){ //si solo estamos presionando derecha
            x += playerSpeed;
            moving = true; //se manda a set animation
        }

        if(up && !down){
            y -= playerSpeed; //si solo estamos presionando arriba
            moving = true; //se manda a set animation
        }else if(down && !up){ //si solo estamos presionando abajo
            y+=playerSpeed;
            moving = true; //se manda a set animation

        }

    }

    /** Este metodo va a obtener la imagen del jugador desde loadsave y cargarla animaciones
     */
    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            animations = new BufferedImage[9][6];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j* 40, 64, 40);

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
}

