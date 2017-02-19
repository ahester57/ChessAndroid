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
    private char symbol;

    private int team;
    private Entity prevEnt;
    private int attackDir;
    private int prevAttackDir;
    private int xDist, yDist;


    Entity() {
        symbol = 'e';
    }

    Entity(char s) {
        symbol = s;
        xDist = yDist = 1;

    }

    //@TODO Generalize this
    //Default movement, any direction, for multiple squares
    public void move(int dir) {
        setPrevAttackDir(dir);

        if (this instanceof King)
            setAttackDist(1,1);

        int[] dist = getAttackDist();

        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                yLoc-=dist[1];
                break;
            case Dir.UP_LEFT:
                xLoc-=dist[0];
                yLoc-=dist[1];
                break;
            case Dir.UP:
                xLoc-=dist[0];
                break;
            case Dir.UP_RIGHT:
                xLoc-=dist[0];
                yLoc+=dist[1];
                break;
            case Dir.RIGHT:
                yLoc+=dist[1];
                break;
            case Dir.DOWN_RIGHT:
                xLoc+=dist[0];
                yLoc+=dist[1];
                break;
            case Dir.DOWN:
                xLoc+=dist[0];
                break;
            case Dir.DOWN_LEFT:
                xLoc+=dist[0];
                yLoc-=dist[1];
                break;
            default:
                xLoc-=dist[0];

        }


    }

    // @TODO fix this for range
    // @TODO fix jumping over people
    public boolean canAttack(ArrayList<Entity> around) {
        boolean flag = false;
        Entity temp;


        for(int i = 0; i < around.size(); i++){

            temp = around.get(i);
            if(!(temp instanceof Border) && !(temp instanceof OpenSpace) && temp != null){

                if(temp.getTeam() != this.getTeam()){

                    flag = true;

                    //make this better steal from clowns

                    setAttackDir(getDirectionToward(temp));

                    setAttackDist(temp);

                }else{
                    setAttackDist(temp);

                }
            }

        }

//        if(!flag) {
//
//            Random rand = new Random();
//            int[][] dirs = getPossibleDirections();
//            int[] choice = dirs[rand.nextInt(dirs.length)];
//            setAttackDist(choice[0], choice[1]);
//
//        }
        return flag;
    }

    // @TODO change for range extension
    // @TODO given loc, scan for row

    public int[][] getPossibleDirections(){
        return new int[][]{
                {xLoc-1, yLoc-1},
                {xLoc-2, yLoc-2},
                {xLoc-3, yLoc-3},

                {xLoc-1, yLoc},
                {xLoc-2, yLoc},
                {xLoc-3, yLoc},

                {xLoc-1, yLoc+1},
                {xLoc-2, yLoc+2},
                {xLoc-3, yLoc+3},

                {xLoc, yLoc-1},
                {xLoc, yLoc-2},
                {xLoc, yLoc-3},


                {xLoc, yLoc+1},
                {xLoc, yLoc+2},
                {xLoc, yLoc+3},

                {xLoc+1, yLoc-1},
                {xLoc+2, yLoc-2},
                {xLoc+3, yLoc-3},

                {xLoc+1, yLoc},
                {xLoc+2, yLoc},
                {xLoc+3, yLoc},


                {xLoc+1, yLoc+1},
                {xLoc+2, yLoc+2},
                {xLoc+3, yLoc+3}
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

//    private int getDistance(Entity toward){
//        // Taxicab!
//        return (Math.abs(xLoc - toward.getX()) + Math.abs(yLoc - toward.getY()));
//        //return (int) Math.sqrt(Math.pow( ((double) xLoc - (double) toward.getX()) , 2)
//                               // + Math.pow( ((double) yLoc - (double) toward.getY()) , 2));
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

    public void setAttackDir(int aD) {
        attackDir = aD;
    }

    void setPrevAttackDir(int dir) {
        prevAttackDir = dir;
    }

    public int getPrevAttackDir() {
        return prevAttackDir;
    }

    public void setAttackDist(Entity to) {
        xDist = Math.abs(xLoc - to.getX());
        yDist = Math.abs(yLoc - to.getY());
    }

    public void setAttackDist(int x, int y) {
        xDist = x;
        yDist = y;
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

    int[] getAttackDist() { return new int[]{xDist, yDist}; }

    public char symbols() {
        return 'e';
    }

    @Override
    public String toString() { return String.valueOf(symbol); }

    public String getInfo() {
        return String.valueOf(symbol) + " - " + String.valueOf(attackDir)
                + ": " + String.valueOf(xDist) + ", " + String.valueOf(yDist);
    }

    public static class Dir {
        public final static int UP = 90;
        final static int RIGHT = 180;
        final static int LEFT = 0;
        public final static int DOWN = 270;
        final static int UP_RIGHT = 135;
        final static int UP_LEFT = 45;
        final static int DOWN_RIGHT = 225;
        final static int DOWN_LEFT = 315;
        final static int STAY = -1;
    }

}