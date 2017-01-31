package tech.stin.chessandroid.entities;

/**
 * Created by Austin on 3/27/2016.
 */
public class Queen extends Entity{

    public Queen(){
        super('q');
    }

    public Queen(int dim[], int t){
        super('q');

        setTeam(t);

        if(getTeam() == 0){
            setxLoc(dim[0] - 2);
            setyLoc(dim[1] / 2 - 1);
        }else if(getTeam() == 1){
            setxLoc(1);
            setyLoc(dim[1] / 2 - 1);
        }


        setSymbol(symbols());
    }

    public Queen(int x, int y, int t){
        super('q');

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

            if ((xLoc + yLoc) % 2 == 0) {
                return 'q';
            } else {
                return 'Q';
            }

        }else{

            if ((xLoc + yLoc) % 2 == 0) {
                return 'w';
            } else {
                return 'W';
            }

        }
    }
}
