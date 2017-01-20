package tech.stin.chessandroid.entities;


import java.util.ArrayList;

/**
 * Created by Austin on 3/23/2016.
 *
 * Entity class is extended by the types of entities.
 *
 */

public abstract class Entity {

    int xLoc, yLoc;
    private String name;
    private char symbol;

    private int team;
    private Entity prevEnt;
    private int attackDir;



    Entity() {
        name = "";
        symbol = 'e';
    }

    Entity(String n, char s) {
        name = n;
        symbol = s;

    }



    //Default movement, any direction, 1 square
    public void move(int dir) {

        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                yLoc--;
                break;
            case Dir.UP_LEFT:
                xLoc--;
                yLoc--;
                break;
            case Dir.UP:
                xLoc--;
                break;
            case Dir.UP_RIGHT:
                xLoc--;
                yLoc++;
                break;
            case Dir.RIGHT:
                yLoc++;
                break;
            case Dir.DOWN_RIGHT:
                xLoc++;
                yLoc++;
                break;
            case Dir.DOWN:
                xLoc++;
                break;
            case Dir.DOWN_LEFT:
                xLoc++;
                yLoc--;
                break;
            default:
                xLoc--;

        }


    }

    //Default movement, any direction, for multiple squares
    public void move(int dir, int distance) {

        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                yLoc-=distance;
                break;
            case Dir.UP_LEFT:
                xLoc-=distance;
                yLoc-=distance;
                break;
            case Dir.UP:
                xLoc-=distance;
                break;
            case Dir.UP_RIGHT:
                xLoc-=distance;
                yLoc+=distance;
                break;
            case Dir.RIGHT:
                yLoc+=distance;
                break;
            case Dir.DOWN_RIGHT:
                xLoc+=distance;
                yLoc+=distance;
                break;
            case Dir.DOWN:
                xLoc+=distance;
                break;
            case Dir.DOWN_LEFT:
                xLoc+=distance;
                yLoc-=distance;
                break;
            default:
                xLoc-=distance;

        }

    }

    //@TODO fix this for range
    public boolean canAttack(ArrayList<Entity> around) {
        boolean flag = false;
        Entity temp;


        for(int i = 0; i < around.size(); i++){

            temp = around.get(i);
            if(!(temp instanceof Border) && !(temp instanceof OpenSpace)){

                if(temp.getTeam() != this.getTeam()){

                    flag = true;

                    //make this better steal from clowns

                    setAttackDir(getDirectionToward(temp));

                }
            }

        }

        return flag;
    }

    public int[][] getPossibleDirections(){
        return new int[][]{
                {xLoc-1, yLoc-1},
                {xLoc-1, yLoc},
                {xLoc-1, yLoc+1},
                {xLoc, yLoc-1},
                {xLoc, yLoc+1},
                {xLoc+1, yLoc-1},
                {xLoc+1, yLoc},
                {xLoc+1, yLoc+1}
        };
    }

    //this method gives the direction toward the chosen entity
    public int getDirectionToward(Entity enemy) {

        int direction;


        int[] enemyLoc = {enemy.getX(), enemy.getY()};
        int x = getX(), y = getY();

        if (x > enemyLoc[0]) {
            if (y > enemyLoc[1]) {
                direction = Dir.UP_LEFT;
            } else if (y < enemyLoc[1]) {
                direction = Dir.UP_RIGHT;
            } else {
                direction = Dir.UP;
            }
        } else if (x < enemyLoc[0]) {
            if (y > enemyLoc[1]) {
                direction = Dir.DOWN_LEFT;
            } else if (y < enemyLoc[1]) {
                direction = Dir.DOWN_RIGHT;
            } else {
                direction = Dir.DOWN;
            }
        } else {
            if (y > enemyLoc[1]) {
                direction = Dir.LEFT;
            } else if (y < enemyLoc[1]) {
                direction = Dir.RIGHT;
            } else {
                direction = Dir.RIGHT;
            }
        }

        return direction;
    }


    //Use this later
//    ArrayList<Node> getAttackLocs(ArrayList<ArrayList<Entity>> grid){
//        ArrayList<Node> list = new ArrayList<>();
//
//        return list;
//    }

    public void setPrevEnt(Entity e) {
        prevEnt = e;
    }

    public void setxLoc(int x) {
        xLoc = x;
    }

    public void setyLoc(int y) {
        yLoc = y;
    }

    public void setSymbol(char s) {
        symbol = s;
    }

    void setTeam(int t) {
        team = t;
    }

    void setAttackDir(int aD) {
        attackDir = aD;
    }

    public Entity getPrevEnt() {
        return prevEnt;
    }

    public int getX() {
        return xLoc;
    }

    public int getY() {
        return yLoc;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getTeam() {
        return team;
    }

    public int getAttackDir() {
        return attackDir;
    }

    public char symbols() {
        return 'e';
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    public static class Dir {
        public final static int UP = 90;
        public final static int RIGHT = 180;
        public final static int LEFT = 0;
        public final static int DOWN = 270;
        public final static int UP_RIGHT = 135;
        public final static int UP_LEFT = 45;
        public final static int DOWN_RIGHT = 225;
        public final static int DOWN_LEFT = 315;
        public final static int STAY = -1;
    }

}