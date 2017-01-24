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
        setPrevAttackDir(dir);

        int[] dist = getAttackDist();

        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                yLoc-=dist[1];
                break;
            case Dir.UP_LEFT:
                yLoc-=dist[1];
                break;
            case Dir.UP:
                xLoc-=dist[0];
                break;
            case Dir.UP_RIGHT:
                xLoc-=dist[0];
                break;
            case Dir.RIGHT:
                yLoc+=dist[1];
                break;
            case Dir.DOWN_RIGHT:
                yLoc+=dist[1];
                break;
            case Dir.DOWN:
                xLoc+=dist[0];
                break;
            case Dir.DOWN_LEFT:
                xLoc+=dist[0];
                break;
            default:
                xLoc-=dist[0];

        }

    }

    @Override
    public int[][] getPossibleDirections(){
        return new int[][]{
                {xLoc-1, yLoc},
                {xLoc-2, yLoc},
                {xLoc-3, yLoc},

                {xLoc, yLoc-1},
                {xLoc, yLoc-2},
                {xLoc, yLoc-3},

                {xLoc, yLoc+1},
                {xLoc, yLoc+2},
                {xLoc, yLoc+3},

                {xLoc+1, yLoc},
                {xLoc+2, yLoc},
                {xLoc+3, yLoc}
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
