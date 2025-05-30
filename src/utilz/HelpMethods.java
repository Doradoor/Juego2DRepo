package utilz;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.Projectile;
import static utilz.Constants.ObjectConstants.*;

import objects.GameContainer;
import objects.Potion;



import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objects.Spike;



import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.ObjectConstants.CANNON_LEFT;
import static utilz.Constants.ObjectConstants.CANNON_RIGHT;

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

        return IsTileSolid((int)xIndex, (int)yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
        int value = lvlData[yTile][xTile];
        switch (value) {
            case 11, 48, 49:
                return false;
            default:
                return true;
        }
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);

    }

    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Will only check if entity touch top water. Can't reach bottom water if not
        // touched top water.
        if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
            if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
                return false;
        return true;
    }

    private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / Game.TILES_SIZE);
        int yCord = (int) (yPos / Game.TILES_SIZE);
        return lvlData[yCord][xCord];
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

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }
    /** Verificar si hay un piso debajo de una hitbox en un nivel del juego
     *
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData); //arreglo para reconocer bordes
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    /**
     * Comprueba si todos los tiles en un rango horizontal y
     * una fila especifica pueden ser caminables para el jugador
     *
     * @param xStart Comienzo del rango en coordenadas x
     * @param xEnd Fin del rango en coordenadas x
     * @param y Coordenada y (fila) en la que se realiza la comprobación.
     * @param lvlData Array bidimensional con los datos del nivel
     *                que determinan qué tiles son sólidos.
     * @return {@code true} si todos los tiles en el rango son transitables
     *         en las filas y e y+1, {@code false} en caso contrario.
     */

    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }
        return true;
    }

    /**
     * Verifica si la linea de vision entre dos hitboxes está despejada,
     * comprobando si los tiles entre ambas son caminables para el player.
     *
     * @param lvlData Matriz de datos del nivel indicando los tiles solidos
     * @param yTile Fila en la que se realiza la comprobacion
     */

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
        int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);
        int secondXTile;

        if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
            secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
        else
            secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsSightClear_OLD(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
    }
}


