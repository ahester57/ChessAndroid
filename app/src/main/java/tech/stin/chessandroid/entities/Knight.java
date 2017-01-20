package tech.stin.chessandroid.entities;

/**
 * Created by Austin on 3/27/2016.
 */

public class Knight extends Entity{

    private static int numKnights = 0;

    public Knight(int dim[], int t){
        super("Knight", 'n');


        setTeam(t);

        if(getTeam() == 0){
            setxLoc(dim[0] - 2);

            if(numKnights % 2 == 0) {
                setyLoc(dim[1] / 2 - 3);
            }else{
                setyLoc(dim[1] / 2 + 2);
            }

        }else if(getTeam() == 1){
            setxLoc(1);

            if(numKnights % 2 == 0) {
                setyLoc(dim[1] / 2 - 3);
            }else{
                setyLoc(dim[1] / 2 + 2);
            }
        }

        numKnights++;
        setSymbol(symbols());
    }

    @Override
    public void move(int dir){


        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                xLoc--;
                yLoc-=2;
                break;
            case Dir.UP_LEFT:
                xLoc-=2;
                yLoc--;
                break;
            case Dir.UP:
                xLoc-=2;
                yLoc++;
                break;
            case Dir.UP_RIGHT:
                xLoc--;
                yLoc+=2;
                break;
            case Dir.RIGHT:
                xLoc++;
                yLoc+=2;
                break;
            case Dir.DOWN_RIGHT:
                xLoc+=2;
                yLoc++;
                break;
            case Dir.DOWN:
                xLoc+=2;
                yLoc--;
                break;
            case Dir.DOWN_LEFT:
                xLoc++;
                yLoc-=2;
                break;
            default:
                xLoc++;
                yLoc-=2;

        }

    }

    @Override
    public int[][] getPossibleDirections(){
        int[][] locs = {
                {xLoc-1, yLoc-2},
                {xLoc-2, yLoc-1},
                {xLoc-2, yLoc+1},
                {xLoc-1, yLoc+2},
                {xLoc+1, yLoc+2},
                {xLoc+2, yLoc+1},
                {xLoc+2, yLoc-1},
                {xLoc+1, yLoc-2}
        };

        return locs;
    }

    //add custom direction towards

    @Override
    public char symbols(){
        if(getTeam() == 0) {

            if ((xLoc + yLoc) % 2 == 0) {
                return 'n';
            } else {
                return 'N';
            }

        }else{

            if ((xLoc + yLoc) % 2 == 0) {
                return 'm';
            } else {
                return 'M';
            }

        }
    }
}
