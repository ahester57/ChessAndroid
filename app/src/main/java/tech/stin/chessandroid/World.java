package tech.stin.chessandroid;

import tech.stin.chessandroid.entities.*;

import java.util.ArrayList;
import java.util.Random;


class World {


    private ArrayList<ArrayList<Entity>> entities;
    private ArrayList<ArrayList<Entity>> teams;
    private ArrayList<Entity> target;

    private boolean aggressive;
    private boolean endOnKing;
    private boolean over;

    private Random rand;
    private static int cycles = 0;

    World(int r, int c){
        rand = new Random();

        teams = new ArrayList<>();
        teams.add(new ArrayList<Entity>());
        teams.add(new ArrayList<Entity>());


        //Ensure capactity for adding kings
        target = new ArrayList<>();
        target.add(null);
        target.add(null);

        endOnKing = true;
        aggressive = false;
        over = false;

        makeEntityGrid(r, c);
    }

    void run(){

        if(teams.get(0).isEmpty() || teams.get(1).isEmpty())
            over = true;

        if(!over || !endOnKing) {
            int team = cycles % 2;
            int direction;
            int distance = 1;

            Entity e = chooseAttacker(team);

            //why do pawns have to be such asses
            //picks a random player from the current team if no attacker available
            if (e instanceof OpenSpace) {
                e = teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size()));
                if (aggressive)
                    direction = e.getDirectionToward(target.get(team));
                else
                    direction = (int) (rand.nextDouble() * 8) * 45;
            } else {
                direction = e.getAttackDir();
            }

            if (e instanceof Queen){
                distance = 3;
            }

            move(e, direction, distance);
        }

    }

    private void run(Entity avoid){

        if(teams.get(0).isEmpty() || teams.get(1).isEmpty())
            over = true;

        if(!over || !endOnKing) {
            int team = cycles % 2;
            int direction;
            int distance = 1;

            Entity e = chooseAttacker(team, avoid);

            //why do pawns have to be such asses
            //picks a random player from the current team if no attacker available
            if (e instanceof OpenSpace) {
                e = teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size()));
                if (aggressive)
                    direction = e.getDirectionToward(target.get(team));
                else
                    direction = (int) (rand.nextDouble() * 8) * 45;
            } else {
                direction = e.getAttackDir();
            }

            if (e instanceof Queen){
                distance = 3;
            }

            move(e, direction, distance);
        }

    }

    void setOptions(boolean agg, boolean endOK) {
        aggressive = agg;
        endOnKing = endOK;
    }

    void setAggressive(boolean agg) {
        aggressive = agg;
    }

    void setEndOnKing(boolean eok) {
        endOnKing = eok;
    }

    /************Move Stuff*************/

    private void move(Entity e, int direction, int distance){

        Entity oldPrev = e.getPrevEnt();
        int oldX = e.getX(), oldY = e.getY();

        if (distance == 1)
            e.move(direction);
        else
            e.move(direction, distance);

        //This section uses recursion and move checking
        // to make sure the team got a turn.
        try {
            try {
                Entity newLoc = getEntity(e.getX(), e.getY());
                if (newLoc != null) {
                    if (!(newLoc instanceof Border)) {
                        if (newLoc instanceof OpenSpace) {

                            actuallyMove(e, oldPrev, oldX, oldY);

                        } else if (e.getTeam() != newLoc.getTeam()) {
                            //kill someone

                            if (!(e instanceof Pawn)) {
                                kill(newLoc);
                                actuallyMove(e, oldPrev, oldX, oldY);
                            } else if (e.getAttackDir() != Entity.Dir.UP && e.getAttackDir() != Entity.Dir.DOWN) {
                                //make sure pawn doesn't attack forward randomly
                                kill(newLoc);
                                actuallyMove(e, oldPrev, oldX, oldY);
                            } else {
                                //pawn attacking straight
                                put(e, oldX, oldY);
                                run(e);
                            }

                        } else {
                            //if its not an open space or able to attack
                            put(e, oldX, oldY);
                            run(e);
                        }
                    } else {
                        //trying to move into a border
                        put(e, oldX, oldY);
                        run(e);
                    }
                } else {
                    //to an invalid location
                    put(e, oldX, oldY);
                    run(e);
                }
            } catch (IndexOutOfBoundsException iobe) {
                //trying to move to an invalid location
                over = true;
            }
        }catch (StackOverflowError soe){
            //can't find anybody
            over = true;
        }
    }

    private void actuallyMove(Entity e, Entity oldPrev, int oldX, int oldY){
        insert(e);
        e.setSymbol(e.symbols());

        insert(oldPrev, oldX, oldY);

        cycles++;
    }

    private void insert(Entity e){
        int x = e.getX(), y = e.getY();

        if(e instanceof Pawn){
            if(e.getTeam() == 0 && e.getX() == 1){
                ((Pawn) e).makeQueen();
            }else if(e.getTeam() == 1 && e.getX() == 8){
                ((Pawn) e).makeQueen();
            }
        }

        e.setPrevEnt(getEntity(x, y));


        entities.get(x).remove(y);
        entities.get(x).add(y, e);
    }

    private void insert(Entity e, int x, int y){
        e.setxLoc(x);
        e.setyLoc(y);

        if(e instanceof Pawn){
            if(e.getTeam() == 0 && e.getX() == 1){
                ((Pawn) e).makeQueen();
            }else if(e.getTeam() == 1 && e.getX() == 8){
                ((Pawn) e).makeQueen();
            }
        }

        e.setPrevEnt(getEntity(x, y));


        entities.get(x).remove(y);
        entities.get(x).add(y, e);
    }

    private void put(Entity e, int x, int y){
        e.setxLoc(x);
        e.setyLoc(y);
    }


    /************Move-choosing Stuff*************/


    //@TODO change this to allow searching extended range

    private Entity chooseAttacker(int team){
        Entity e = new OpenSpace(' ');
        Entity temp, temp2;

        ArrayList<Entity> closeEntities;

        for(int i = 0; i < teams.get(team).size(); i++){

            temp = teams.get(team).get(i);

            closeEntities = new ArrayList<>();

            for(int[] coords : temp.getPossibleDirections()){
                temp2 = getEntity(coords[0], coords[1]);
                if(temp2 != null)
                    closeEntities.add(temp2);
            }

            if(temp.canAttack(closeEntities)){
                e = temp;
                return e;
            }

        }

        return e;
    }

    private Entity chooseAttacker(int team, Entity avoid){
        Entity e = new OpenSpace(' ');
        Entity temp, temp2;

        ArrayList<Entity> closeEntities;

        for(int i = 0; i < teams.get(team).size(); i++){

            temp = teams.get(team).get(i);

            closeEntities = new ArrayList<>();

            for(int[] coords : temp.getPossibleDirections()){
                temp2 = getEntity(coords[0], coords[1]);
                if(temp2 != null)
                    closeEntities.add(temp2);
            }

            if(temp.canAttack(closeEntities)){
                if(temp == avoid){
                    continue;
                }
                e = temp;
                return e;
            }

        }

        return e;
    }

    /************Player Stuff*************/

    //Adds a player to the gameboard
    void addPlayer(Entity e){

        teams.get(e.getTeam()).add(e);

        if(e instanceof King){
            if(e.getTeam() == 0){

                target.set(1, e);
            }else{
                target.set(0, e);
            }
        }

        int x = e.getX(), y = e.getY();

        e.setSymbol(e.symbols());
        e.setPrevEnt(getEntity(x, y));
        entities.get(x).remove(y);
        entities.get(x).add(y, e);
    }

    //Kills an entity
    private void kill(Entity prey) {

        if(prey instanceof King){
            over = true;
        }

        //
        if(prey == target.get(0)){
            target.set(0, teams.get(1).get(0));
        }else if(prey == target.get(1)){
            target.set(1, teams.get(0).get(0));
        }


        teams.get(prey.getTeam()).remove(prey);

        if(teams.get(0).isEmpty() || teams.get(1).isEmpty())
            over = true;

        entities.get(prey.getX()).remove(prey.getY());
        entities.get(prey.getX()).add(prey.getY(), prey.getPrevEnt());
    }


    /********Grid Stuff*****************/

    //This method builds the original chess board, containing
    //only borders and open spaces.
    private void makeEntityGrid(int rows, int cols){

        entities = new ArrayList<>();

        int maxRow = rows-1, maxCol = cols-1;

        for(int x = 0; x < rows; x++){

            entities.add(new ArrayList<Entity>());

            for(int y = 0; y < cols; y++){

                if(x == 0) {
                    //top border
                    if(y == 0) {
                        entities.get(x).add(new Border('!'));
                    }else if(y == maxCol) {
                        entities.get(x).add(new Border('#'));
                    }else{
                        entities.get(x).add(new Border('"'));

                    }

                }else if(x == maxRow) {
                    //bottom border
                    if(y == 0) {
                        entities.get(x).add(new Border('/'));
                    }else if(y == maxCol) {
                        entities.get(x).add(new Border(')'));
                    }else{
                        entities.get(x).add(new Border('('));
                    }

                }else {
                    if(y == 0) {
                        //Numbered borders on left
                        switch(x){
                            case 1:
                                entities.get(x).add(new Border('à'));
                                break;
                            case 2:
                                entities.get(x).add(new Border('á'));
                                break;
                            case 3:
                                entities.get(x).add(new Border('â'));
                                break;
                            case 4:
                                entities.get(x).add(new Border('ã'));
                                break;
                            case 5:
                                entities.get(x).add(new Border('ä'));
                                break;
                            case 6:
                                entities.get(x).add(new Border('å'));
                                break;
                            case 7:
                                entities.get(x).add(new Border('æ'));
                                break;
                            case 8:
                                entities.get(x).add(new Border('ç'));
                                break;
                            default:
                                entities.get(x).add(new Border('$'));
                        }

                    }else if(y == maxCol) {
                        //right border
                        entities.get(x).add(new Border('%'));

                    }else{
                        //Open space
                        char ch;
                        if ((y + x) % 2 == 0) {
                            ch = ' ';
                        } else {
                            ch = '+';
                        }
                        entities.get(x).add(new OpenSpace(ch));
                    }
                }
            }
            //outside inner loop
        }
    }

    Entity getEntity(int r, int c){
        try{
            return entities.get(r).get(c);
        }catch (IndexOutOfBoundsException iobe){
            return null;
        }

    }

    boolean isOver() {
//        if(endOnKing)
//            return over;
//        else
//            return false;
        return endOnKing && over;
    }


}
