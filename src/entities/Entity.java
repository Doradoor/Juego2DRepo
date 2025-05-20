package entities;

/** Esta clase sirve como un modelo para nuestras entidades
 * les asigna psoicion y tamaño
 * Puede tener clases derivadas que hereden su estructura
 */
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    /** Constructor
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
