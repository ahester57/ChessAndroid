package tech.stin.chessandroid.entities;

import java.util.ArrayList;

/**
 * Created by Austin on 3/27/2016.
 *
 * Pawn is currently able to target enemies.
 */

public class Pawn extends Entity{

    private static int numPawns = 0;
    private boolean isQueen;


    public Pawn(int dim[], int t){
        super("Pawn", 'p');

        isQueen = false;

        setTeam(t);

        if(getTeam() == 0){
            setxLoc(dim[0] - 3);

            setyLoc(dim[1] / 2 - 4 + (numPawns % 8));

            setAttackDir(Dir.UP);
        }else if(getTeam() == 1){
            setxLoc(2);

            setyLoc(dim[1] / 2 - 4 + (numPawns % 8));
            setAttackDir(Dir.DOWN);
        }


        numPawns++;
        setSymbol(symbols());
    }

    @Override
    public void move(int dir){
        if(!isQueen)
            pawnMove(dir);
        else
            super.move(dir);
    }

    private void pawnMove(int dir){
        switch(getAttackDir()){
            case 45:
                xLoc--;
                yLoc--;
                break;
            case 90:
                xLoc--;
                break;
            case 135:
                xLoc--;
                yLoc++;
                break;
            case 225:
                xLoc++;
                yLoc++;
                break;
            case 270:
                xLoc++;
                break;
            case 315:
                xLoc++;
                yLoc--;
                break;
            default:
                if(getTeam() == 0){
                    yLoc--;
                }else{
                    yLoc++;
                }
        }

    }


    //they attack backwards!~
    public boolean canAttack(ArrayList<Entity> grid){
        if(!isQueen) {
            boolean flag = false;
            Entity temp;

            if (getTeam() == 0) {
                setAttackDir(Dir.UP);
            } else if (getTeam() == 1) {
                setAttackDir(Dir.DOWN);
            }

            for (int i = 0; i < grid.size(); i++) {

                temp = grid.get(i);
                if (!(temp instanceof Border) && !(temp instanceof OpenSpace)) {

                    if (temp.getTeam() != this.getTeam()) {

                        flag = true;

                        //make this better steal from clowns

                        setAttackDir(getDirectionToward(temp));

                    }
                }

            }
            return flag;
        }else{
            return super.canAttack(grid);
        }
    }

    @Override
    public int[][] getPossibleDirections(){
        if(!isQueen) {
            int[][] locs = new int[2][2];

            if (getTeam() == 0) {
                locs[0][0] = xLoc - 1;
                locs[0][1] = yLoc - 1;
                locs[1][0] = xLoc - 1;
                locs[1][1] = yLoc + 1;
            } else {
                locs[0][0] = xLoc + 1;
                locs[0][1] = yLoc - 1;
                locs[1][0] = xLoc + 1;
                locs[1][1] = yLoc + 1;
            }


            return locs;
        }else{
            return super.getPossibleDirections();
        }
    }

    public void makeQueen(){
        isQueen = true;
    }


    @Override
    public char symbols(){
        if(!isQueen) {
            if (getTeam() == 0) {

                if ((xLoc + yLoc) % 2 == 0) {
                    return 'p';
                } else {
                    return 'P';
                }

            } else {

                if ((xLoc + yLoc) % 2 == 0) {
                    return 'o';
                } else {
                    return 'O';
                }

            }
        }else {
            if (getTeam() == 0) {

                if ((xLoc + yLoc) % 2 == 0) {
                    return 'q';
                } else {
                    return 'Q';
                }

            } else {

                if ((xLoc + yLoc) % 2 == 0) {
                    return 'w';
                } else {
                    return 'W';
                }

            }
        }
    }
}
