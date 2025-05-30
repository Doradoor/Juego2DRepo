package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.CanMoveHere;

/** Esta clase sirve como un modelo para nuestras entidades
 * les asigna psoicion y tamaño
 * Puede tener clases derivadas que hereden su estructura
 */
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    int maxHealth;
    int currentHealth = maxHealth;
    protected Rectangle2D.Float attackBox; //para la espada

    protected float walkSpeed = 1.0f * Game.SCALE;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;



    /** Constructor
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }


    protected void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.RED);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }
    /** Este metodo lo usaremos para iniciar un hitbox como un rectangulo
     */
    protected void initHitbox( int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }
    /** Este metodo se usa para actualizar la posicion de la hitbox para que coincida con el objeto
     */
   // protected void updateHitbox(){
    //    hitbox.x = (int)x;
   //     hitbox.y = (int)y;
   // }
    /** Para crear el rectangulo que va a representar la hitbox
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getState() {
        return state;

    }
    public int getAniIndex() {
        return aniIndex;
    }


    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }



}
