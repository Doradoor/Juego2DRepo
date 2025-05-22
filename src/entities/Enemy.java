package entities;

import static utilz.Constants.EnemyConstants.*;
/** Clase abstratca que extiende Entity
 * Esta clase va a representar a un enemigo en el juego y manejar animacion y tipo de enemigo
 */
public abstract class Enemy extends Entity {
    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    /** Metodo para  controlar la actualizacioon del indice de animacion
     */
    private void updateAnimationTick(){
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType, enemyState)){
                aniIndex = 0;
            }
        }
    }

    public void update(){
        updateAnimationTick();
    }

    public int getAniIndex(){
        return aniIndex;
    }

    public int getEnemyState(){
        return enemyState;
    }
}
