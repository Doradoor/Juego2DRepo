package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/** Esta clase sirve como un modelo para nuestras entidades
 * les asigna psoicion y tamaño
 * Puede tener clases derivadas que hereden su estructura
 */
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    /** Constructor
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics g){
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }
    /** Este metodo lo usaremos para iniciar un hitbox como un rectangulo
     */
    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
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
}
