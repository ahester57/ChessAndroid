package tech.stin.chessandroid;

import android.os.Debug;

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
    private int count;

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
        count = 0;

        makeEntityGrid(r, c);
    }

    void run(){
        //Debug.startMethodTracing("run");
        if(teams.get(0).isEmpty() || teams.get(1).isEmpty()) {
            over = true;
            return;
        }

        if(!over || !endOnKing) {
            int team = cycles % 2;
            count = 0;


            ArrayList<Entity> attackers = chooseAttacker(team);

            //why do pawns have to be such asses
            //picks a random player from the current team if no attacker available

            // @TODO add old function back if list empty

            if (attackers.size() == 0) {
                Entity en = teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size()));
                en.setAttackDir(en.getDirectionToward(target.get(team)));
                en.setAttackDist(1, 1);
                move(en, en.getAttackDir());
            } else {
                for (Entity e : attackers) {
                    if (e instanceof OpenSpace) {
                        e = teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size()));
                        attackers.set(0, e);

                        for (int i = 0; i < teams.get(team).size(); i++)
                            attackers.add(teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size())));

                        if (!(e instanceof Pawn)) {
                            if (aggressive) {
                                e.setAttackDir(e.getDirectionToward(target.get(team)));
                                //e.setAttackDist()
                            } else {
                                e.setAttackDir((int) (rand.nextDouble() * 8) * 45);

                                //direction = (int) (rand.nextDouble() * 8) * 45;
                            }
                        }
                    } else {


                    }


                }
            }

            // @TODO fix it not workng
            // @TODO fix getting stuck, add *random* move when stuck

            //pick best attacker
            int best = rand.nextInt(attackers.size()); // = chooseBestMove();


//            if (attackers.size() > 1)
//                best = rand.nextDouble() > 0.5 ? 0 : 1; // = chooseBestMove();

            move(attackers.get(best), attackers.get(best).getAttackDir());

            //move(attackers.get(best), direction, distance);
        }
        //Debug.stopMethodTracing();
    }


    // When chosen attacker's move is invalid, try again, skipping previously choosen
    private void run(Entity avoid){
        if(teams.get(0).isEmpty() || teams.get(1).isEmpty()) {
            over = true;
            return;
        }

        if(!over || !endOnKing) {
            int team = cycles % 2;

            ArrayList<Entity> attackers = chooseAttacker(team, avoid);

            //why do pawns have to be such asses
            //picks a random player from the current team if no attacker available

            for (Entity e : attackers){
                if (e instanceof OpenSpace) {
                    e = teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size()));
                    attackers.set(0, e);

                    for (int i = 0; i < teams.get(team).size(); i++)
                        attackers.add(teams.get(team).get((int) (rand.nextDouble() * teams.get(team).size())));

                    if (!(e instanceof Pawn)) {
                        if (aggressive) {
                            e.setAttackDir(e.getDirectionToward(target.get(team)));
                            //e.setAttackDist()
                        }else {
                            e.setAttackDir((int) (rand.nextDouble() * 8) * 45);

                            //direction = (int) (rand.nextDouble() * 8) * 45;
                        }
                    }
                } else {

                }


            }


            //pick best attacker
            int best = rand.nextInt(attackers.size()); // = chooseBestMove();
            count++;

            if (count > 2000) {
                over = true;
                endOnKing = true;

                return;
            }


            move(attackers.get(best), attackers.get(best).getAttackDir());
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

    private void move(Entity e, int direction){

        Entity oldPrev = e.getPrevEnt();
        int oldX = e.getX(), oldY = e.getY();
//

        e.move(direction);
//        if (distance == 1)
//            e.move(direction);
//        else
//            e.move(direction, distance);

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
                            } else if ((e.getAttackDir() != Entity.Dir.UP && e.getAttackDir() != Entity.Dir.DOWN) || ((Pawn) e).isQueen()) {
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
                        run(e); // add only avoid recent move
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
    //@TODO compile

    private ArrayList<Entity> chooseAttacker(int team){
        ArrayList<Entity> attackers = new ArrayList<>();

        Entity source, target;

        ArrayList<Entity> closeEntities;

        for(int i = 0; i < teams.get(team).size(); i++){

            source = teams.get(team).get(i);


            // Change closeEntities to potential targets
            // Consider giving row / column owned
            // Do up / down first, then diagonal

            closeEntities = new ArrayList<>();

            for(int[] coords : source.getPossibleDirections()){
                target = getEntity(coords[0], coords[1]);
                if(target != null)
                    closeEntities.add(target);
            }


            // Instead of returning right away, make list then choose best option


            if(source.canAttack(closeEntities)){
                attackers.add(source);

            }

        }

        if(attackers.isEmpty())
            attackers.add(new OpenSpace(' '));


        return attackers;
    }

    private ArrayList<Entity> chooseAttacker(int team, Entity avoid){
        ArrayList<Entity> attackers = new ArrayList<>();

        Entity source, target;

        ArrayList<Entity> closeEntities;

        for(int i = 0; i < teams.get(team).size(); i++){

            source = teams.get(team).get(i);

            closeEntities = new ArrayList<>();

            for(int[] coords : source.getPossibleDirections()){
                target = getEntity(coords[0], coords[1]);
                if(target != null)
                    closeEntities.add(target);
            }

            if(source.canAttack(closeEntities)){
                if(source.getX() == avoid.getX() && source.getY() == avoid.getY()){
                    continue;
                }
                attackers.add(source);

            }

        }

        if(attackers.isEmpty())
            attackers.add(new OpenSpace(' '));

        return attackers;
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
            if (r < 10 && c < 10)
                return entities.get(r).get(c);
            else
                return null;
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
