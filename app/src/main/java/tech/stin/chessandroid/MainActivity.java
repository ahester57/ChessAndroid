package tech.stin.chessandroid;

import tech.stin.chessandroid.entities.*;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TextView> cells;
    private World world;

    private GridLayout gameboard;
    private Button nextTurn;
    private ToggleButton autoAdvance;
    private Timer timer;
    private boolean stopTime;
    private Spinner colorPicker;

    private boolean aggressive;
    private boolean autoPlay;
    private boolean endOnKing;
    //for timer,,.,

    private final int NUM_ROWS = 10;
    private final int NUM_COLS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorPicker = (Spinner) findViewById(R.id.spinner);
        List<String> colors = new ArrayList<>();
        colors.add("Blue");
        colors.add("Black");
        colors.add("Green");
        colors.add("Red");
        colors.add("Purple");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorPicker.setAdapter(dataAdapter);

        this.runOnUiThread(initialize);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public Runnable initialize = new Runnable() {
        @Override
        public void run() {

            gameboard = (GridLayout) findViewById(R.id.gameboard);
            nextTurn = (Button) findViewById(R.id.nextTurn);
            ToggleButton mood = (ToggleButton) findViewById(R.id.mood);
            autoAdvance = (ToggleButton) findViewById(R.id.AutoPlayButton);
            Button reset = (Button) findViewById(R.id.resetGame);
            Switch endOnKingSwitch = (Switch) findViewById(R.id.endOnKing);


            aggressive = false;
            autoPlay = false;
            endOnKing = true;
            stopTime = false;

            initGameboard();
            initWorld();
            updateGameboard();

            //Next button
            nextTurn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    world.run();
                    updateGameboard();
                    if(world.isOver()) {
                        nextTurn.setEnabled(false);
                        autoAdvance.setEnabled(false);
                        stopTime = true;
                    }
                }
            });

            //Reset button
            reset.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    reset();
                }
            });

            //Mood button
            mood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    aggressive = isChecked;
                    world.setAggressive(aggressive);
                }
            });

            //auto button
            autoAdvance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (!stopTime)
                            Timer_Run();
                    }
                };
                private boolean first = true;
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    autoPlay = isChecked;
                    if(first){
                        first = false;
                        stopTime = false;
                        timer = new Timer();
                        timer.schedule(task, 500, 1000);
                    }
                    if(autoPlay)
                        nextTurn.setEnabled(false);
                    else
                        nextTurn.setEnabled(true);
                }
            });

            //End of King switch
            endOnKingSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    endOnKing = isChecked;
                    world.setEndOnKing(endOnKing);
                    if (!endOnKing){
                        nextTurn.setEnabled(true);
                        autoAdvance.setEnabled(true);
                        stopTime = false;
                    } else if(world.isOver()){
                        nextTurn.setEnabled(false);
                        autoAdvance.setEnabled(false);
                    }
                }
            });
            endOnKingSwitch.setChecked(true);

            //Color picker




            colorPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String col = parent.getItemAtPosition(position).toString();
                    switch (col) {
                        case "Blue":
                            updateColor(Color.BLUE);
                            break;
                        case "Black":
                            updateColor(Color.BLACK);
                            break;
                        case "Green":
                            updateColor(Color.parseColor("#248a38"));
                            break;
                        case "Red":
                            updateColor(Color.parseColor("#ff0e0e"));
                            break;
                        case "Purple":
                            updateColor(Color.parseColor("#732493"));
                            break;
                        default:
                            updateColor(Color.BLUE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    updateColor(Color.BLACK);
                }
            });


            world.setOptions(aggressive, endOnKing);

        }
    };

    private void reset(){
        autoPlay = false;
        stopTime = false;

        autoAdvance.setEnabled(true);
        autoAdvance.setChecked(false);
        //mood.setChecked(false);
        nextTurn.setEnabled(true);
        world = new World(NUM_ROWS, NUM_COLS);
        //endOnKingSwitch.setChecked(true);
        initWorld();
        updateGameboard();
        world.setOptions(aggressive, endOnKing);
    }

    private void Timer_Run() {
        if (autoPlay)
            this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            nextTurn.callOnClick();
        }
    };

    private void initWorld(){
        world = new World(NUM_ROWS, NUM_COLS);
        int[] dimensions = {NUM_ROWS, NUM_COLS};

        //The players need to know the dimensions
        //of the board in order to place themselves
        //correctly.
        world.addPlayer(new King(dimensions, 0));
        world.addPlayer(new Queen(dimensions, 0));

        world.addPlayer(new Bishop(dimensions, 0));
        world.addPlayer(new Bishop(dimensions, 0));
        world.addPlayer(new Knight(dimensions, 0));
        world.addPlayer(new Knight(dimensions, 0));
        world.addPlayer(new Rook(dimensions, 0));
        world.addPlayer(new Rook(dimensions, 0));
        for(int i = 0; i < 8; i++) {
            world.addPlayer(new Pawn(dimensions, 0));
        }

        world.addPlayer(new King(dimensions, 1));
        world.addPlayer(new Queen(dimensions, 1));

        world.addPlayer(new Bishop(dimensions, 1));
        world.addPlayer(new Bishop(dimensions, 1));
        world.addPlayer(new Knight(dimensions, 1));
        world.addPlayer(new Knight(dimensions, 1));
        world.addPlayer(new Rook(dimensions, 1));
        world.addPlayer(new Rook(dimensions, 1));
        for(int i = 0; i < 8; i++) {
            world.addPlayer(new Pawn(dimensions, 1));
        }

    }

    private void initGameboard() {
        gameboard.setColumnCount(NUM_COLS);
        gameboard.setRowCount(NUM_ROWS);

        Log.i("asdf", String.valueOf(gameboard.getChildCount()));

        cells = new ArrayList<>();
        for(int i = 0; i < gameboard.getChildCount(); i++){
            cells.add((TextView) gameboard.getChildAt(i));
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        Typeface font = Typeface.createFromAsset(getAssets(), "CASEFONT.ttf");
        Resources res = getResources();
        TextView temp;
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, width / 10 - 2, res.getDisplayMetrics());

        for(int r = 0; r < gameboard.getRowCount(); r++){
            for(int c = 0; c < gameboard.getColumnCount(); c++){
                temp = getTextCell(r, c);
                temp.setWidth(px);
                temp.setHeight(px);
                temp.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
                temp.setTypeface(font);
            }
        }


    }

     private void updateGameboard() {
        TextView temp;
        for(int r = 0; r < gameboard.getRowCount(); r++){
            for(int c = 0; c < gameboard.getColumnCount(); c++){
                temp = getTextCell(r, c);
                temp.setText(String.valueOf(world.getEntity(r, c).getSymbol()));
            }
        }
    }

    private void updateColor(int color){
        for(TextView view : cells){
            view.setTextColor(color);
        }
    }

    private TextView getTextCell(int r, int c){
        int index = r * 10 + c;
        return cells.get(index);
    }
}
