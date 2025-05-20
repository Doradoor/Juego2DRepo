package levels;

/** Clase para manejar datos de un nivel del juego, que se almacenan en una matriz biudimensional
 * cada entero representa un tipo de elemento
 */
public class Level {

    private int[][] lvlData;

    public Level(int[][] lvlData){
        this.lvlData = lvlData;
    }

    // Identificador para ver en que coordenada se encuentra una tile u objeto especifica del nivel
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }
}
