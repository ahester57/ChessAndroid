package tech.stin.chessandroid.entities;


/**
 * Created by Austin on 3/27/2016.
 */

public class Bishop extends Entity {



    private static int numBishops = 0;

    public Bishop(int dim[], int t){
        super("Bishop", 'b');


        setTeam(t);

        if(getTeam() == 0){
            setxLoc(dim[0] - 2);

            if(numBishops % 2 == 0) {
                setyLoc(dim[1] / 2 - 2);
            }else{
                setyLoc(dim[1] / 2 + 1);
            }

        }else if(getTeam() == 1){
            setxLoc(1);

            if(numBishops % 2 == 0) {
                setyLoc(dim[1] / 2 - 2);
            }else{
                setyLoc(dim[1] / 2 + 1);
            }
        }

        numBishops++;
        setSymbol(symbols());
    }

    @Override
    public void move(int dir){

        switch (dir) {
            case Dir.STAY:
                break;
            case Dir.LEFT:
                xLoc--;
                yLoc--;
                break;
            case Dir.UP_LEFT:
                xLoc--;
                yLoc--;
                break;
            case Dir.UP:
                xLoc--;
                yLoc++;
                break;
            case Dir.UP_RIGHT:
                xLoc--;
                yLoc++;
                break;
            case Dir.RIGHT:
                xLoc++;
                yLoc++;
                break;
            case Dir.DOWN_RIGHT:
                xLoc++;
                yLoc++;
                break;
            case Dir.DOWN:
                xLoc++;
                yLoc--;
                break;
            case Dir.DOWN_LEFT:
                xLoc++;
                yLoc--;
                break;
            default:
                xLoc++;
                yLoc--;

        }

    }

    @Override
    public int[][] getPossibleDirections(){
        int[][] locs = {
                {xLoc-1, yLoc-1},
                {xLoc-1, yLoc+1},
                {xLoc+1, yLoc-1},
                {xLoc+1, yLoc+1}
        };

        return locs;
    }

    @Override
    public char symbols(){
        if(getTeam() == 0) {

            if ((xLoc + yLoc) % 2 == 0) {
                return 'b';
            } else {
                return 'B';
            }

        }else{

            if ((xLoc + yLoc) % 2 == 0) {
                return 'v';
            } else {
                return 'V';
            }

        }
    }
}
