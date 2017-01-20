package tech.stin.chessandroid.entities;

import java.util.ArrayList;

/**
 * Created by Austin on 3/27/2016.
 */
public class Rook extends Entity{

    private static int numRooks = 0;

    public Rook(int dim[], int t){
        super('r');


        setTeam(t);

        if(getTeam() == 0){
            setxLoc(dim[0] - 2);

            if(numRooks % 2 == 0) {
                setyLoc(dim[1] / 2 - 4);
            }else{
                setyLoc(dim[1] / 2 + 3);
            }

        }else if(getTeam() == 1){
            setxLoc(1);

            if(numRooks % 2 == 0) {
                setyLoc(dim[1] / 2 - 4);
            }else{
                setyLoc(dim[1] / 2 + 3);
            }
        }

        numRooks++;
        setSymbol(symbols());
    }

    @Override
    public void move(int dir){

        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                yLoc--;
                break;
            case Dir.UP_LEFT:
                yLoc--;
                break;
            case Dir.UP:
                xLoc--;
                break;
            case Dir.UP_RIGHT:
                xLoc--;
                break;
            case Dir.RIGHT:
                yLoc++;
                break;
            case Dir.DOWN_RIGHT:
                yLoc++;
                break;
            case Dir.DOWN:
                xLoc++;
                break;
            case Dir.DOWN_LEFT:
                xLoc++;
                break;
            default:
                xLoc--;

        }

    }

    @Override
    public int[][] getPossibleDirections(){
        return new int[][]{
                {xLoc-1, yLoc},
                {xLoc, yLoc-1},
                {xLoc, yLoc+1},
                {xLoc+1, yLoc},
        };

    }


    @Override
    public char symbols(){
        if(getTeam() == 0) {

            if((xLoc + yLoc) % 2 == 0){
                return 'r';
            }else{
                return 'R';
            }

        }else{

            if ((xLoc + yLoc) % 2 == 0) {
                return 't';
            } else {
                return 'T';
            }

        }
    }

}
