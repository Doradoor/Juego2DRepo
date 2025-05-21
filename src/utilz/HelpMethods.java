package utilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

    /** Metodo para evaluar si la entidad puede moverse a una posicion
     * Si es solido puede moverse, si no es falso
     */
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if(!IsSolid(x + width, y + height, lvlData))
                if(!IsSolid(x + width, y, lvlData))
                    if(!IsSolid(x, y + height, lvlData))
                        return true;
        return false; //si todos son falsos entonces nos podemos mover
    }

    /** Para determinar si una posicion esta sobre una estrutura solida
     * Utilizando como parametros x, y para comprobar que las estructuras estab eb kis kunutes
     */
    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if(value >= 48 || value < 0 || value != 11) {
            return true;
        }
        return false;
    }

    /** Calcula en x en donde debe estar la entidad debe colocarse al lado de una pared
     * dependiendo de su velocidad horizontal
     * Este metodo sirve para prevenir que el jugador atraviese paredes
     * ajustando la posicion del personaje a los bordes
     */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE); // diviendo posicion x por el tamaño del tile para buscar el tile mas cercano
        if (xSpeed > 0){ // si x speed es 0 no habria colisiones
            //Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }
    /** Para enocntrar la posicion en la que la entidad debe colocarse pero en Y
     * Si esta debajo de un techo o del sulo
     * Para que no atraviese el suelo ni los techos en los saltos
     */
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0){
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            //jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    /** Verificar que la entidad este en el  suelo
     * Usa las esquinas izquierdas y derechas de la hitbox para verificar que esten en tiles solidas
     *
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        //Revisar los pixeles izq abajo y derecha abajo, si no estan solid entonces esta en el aire
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if(!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }
}

