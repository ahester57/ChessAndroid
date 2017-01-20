package tech.stin.chessandroid.entities;


/**
 * Created by Austin on 3/27/2016.
 *
 * King is currently able to target enemies
 */

public class King extends Entity {

    public King(){
        super('k');

    }

    public King(int dim[], int t){
        super('k');

        setTeam(t);

        if(getTeam() == 0){
            setxLoc(dim[0] - 2);
            setyLoc(dim[1] / 2);
        }else if(getTeam() == 1){
            setxLoc(1);
            setyLoc(dim[1] / 2);
        }


        setSymbol(symbols());
    }

    public King(int x, int y, int t){
        super('k');

        setTeam(t);

        setxLoc(x);
        setyLoc(y);

        setSymbol(symbols());
    }

    @Override
    public void move(int dir){
        super.move(dir);
    }

    @Override
    public char symbols(){
        if(getTeam() == 0) {

            if((xLoc + yLoc) % 2 == 0){
                return 'k';
            }else{
                return 'K';
            }

        }else{

            if ((xLoc + yLoc) % 2 == 0) {
                return 'l';
            } else {
                return 'L';
            }

        }
    }


}
