package entities;

import main.Game;
import utilz.LoadSave;


import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.CanMoveHere;
public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 20 * Game.SCALE, 28 * Game.SCALE);
    }

    /**
     * Metodo para actualizar jugador
     * updatePos hara que inicie Moving por default en false
     * setAnimation revisa si moving es o no true.
     */
    public void update() {
        updatePos();
      //  updateHitbox();
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
        g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffset) , (int)(hitbox.y - yDrawOffset), width,height,null);
        drawHitbox(g);
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

        if(!left && !right && !up && !down)
            return;

        float xSpeed = 0, ySpeed = 0; //Velocidad temporal de x y

        if(left && !right){
            xSpeed = -playerSpeed; //si solo estamos presionando izq
        }else if(right && !left){ //si solo estamos presionando derecha
            xSpeed = playerSpeed;
        }
        if(up && !down){
            ySpeed = -playerSpeed; //si solo estamos presionando arriba
        }else if(down && !up){ //si solo estamos presionando abajo
            ySpeed  = playerSpeed;
        }
        /*
        if(CanMoveHere(x + xSpeed, y + ySpeed, width, height, lvlData)){
            this.x += xSpeed;
            this.y += ySpeed;
            moving = true;
        }
*/
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            moving = true;
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
    /** Inicializar o cambiar los datos del nivel en el juego
     */
    public void loadlvlData(int[][] lvlData){
        this.lvlData = lvlData;
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

