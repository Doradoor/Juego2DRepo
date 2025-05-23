package levels;

import entities.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objects.Cannon;
import utilz.HelpMethods;


import static utilz.HelpMethods.*;

/** Clase para manejar datos de un nivel del juego, que se almacenan en una matriz biudimensional
 * cada entero representa un tipo de elemento
 */
public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Cannon> cannons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;


    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffsets();
        createCannons();
        calcPlayerSpawn();
    }
    private void createCannons() {
        cannons = HelpMethods.GetCannons(img);
    }
    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }


    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);        
    }

    // Identificador para ver en que coordenada se encuentra una tile u objeto especifica del nivel
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }
    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }
}
