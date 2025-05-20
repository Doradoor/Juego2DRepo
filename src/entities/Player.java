package entities;

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
    private boolean moving = false;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    /**
     * Metodo para actualizar jugador
     * updatePos hara que inicie Moving por default en false
     * setAnimation revisa si moving es o no true.
     */    public void update() {
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
        g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 256,160,null);

    }




    private void updateAnimationTick() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteCount(playerAction)) { //si es mas grande que el tamaño del array de animacion se resetea y tmb dependiendo de la acccion del jugador obtenemos una cantidad de index
                aniIndex = 0;
            }
        }
    }

    //Si nos estamos moviendo entonces se toma como running, si no como Idle
    private void setAnimation() {
        if(moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
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

    /** Este metodo va a obtener la imagen y asignarla a img.
     * dos try catch, uno usa close() para liberar recursos y evitar problemas
     * Luego carga las frames a un array para hacer la animacion en un ciclo
     */
    private void loadAnimations() {

        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            BufferedImage img = ImageIO.read(is);
            animations = new BufferedImage[9][6];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j* 40, 64, 40);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }



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

