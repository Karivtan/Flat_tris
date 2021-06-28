package com.example.flattris;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class flattris extends AppCompatActivity {
    private int[][] well = new int[22][12];
    private Point pieceOrigin;
    private int rotation, rcolumn, nLives, maxWell, score;
    private int cRow=0;
    private ImageButton [][] buttons;
    private FlatThread mythread;
    private boolean topreached;
    private Piece cPiece;
    private int delay =1000;
    private boolean pieceFalling, gamerun, gamestarted;
    private ArrayList<Integer> nextPieces = new ArrayList<>();
    private final int[] colors = new int[]{0xFFFFFFFF,0xFF000055, 0xFF0000AA, 0xFF0000FF, 0xFF005500, 0xFF00AA00, 0xFF00FF00, 0xFF550000, 0xFFAA0000, 0xFFFF0000};
    private Bundle savedInstanceState;
    Handler mHandler;
    int [] ButtonImages= new int[10];


    private final Point[][][] FlatrisBlocks = {
            // here all the pieces need to come in
            // 1-piece
            {
                    {new Point(0, 0)}
            },
            // 2-piece
            {
                    {new Point(0, 0), new Point(0, -1)},
                    {new Point(0, 0), new Point(-1, 0)}
            },
            // 3-piece
            {
                    {new Point(0, 0), new Point(0, -1), new Point(0, 1)},
                    {new Point(0, 0), new Point(0, -1), new Point(-1, -1)}
            },
            // 4-piece
            {
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0)},
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, 1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(1, -1), new Point(1, 0)},
                    {new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(0, -1), new Point(1, 0)}
            },
            // 5-piece
            {
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0), new Point(0,-1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0), new Point(1,1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0), new Point(0,1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0), new Point(-1,1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0), new Point(1,-1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(1, -1), new Point(1, 0), new Point(0,-1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(1, -1), new Point(1, 0), new Point(-1,1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(1, -1), new Point(1, 0), new Point(0,1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, -1), new Point(1, 0), new Point(-1,1)},
                    {new Point(0, 0), new Point(-1, 0), new Point(1, -1), new Point(0, -1), new Point(-1,1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(-1, 0), new Point(0,-1)},
            },
            // 6-piece
            {
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,1), new Point(1,0)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(0,1), new Point(1,0)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(-1,1), new Point(1,0)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(0,1), new Point(1,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(-1, 1), new Point(1,-1), new Point(1,0)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(1, 1), new Point(1,-1), new Point(1,0)},
                    {new Point(-1, -1), new Point(0, 1), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,1)},
                    {new Point(-1, -1), new Point(0, 1), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(-1,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(0,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(-1,1)}
            },
            // 7-piece
            {
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0), new Point(-1,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0), new Point(0,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0), new Point(1,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(1, 1), new Point(1,-1), new Point(1,0), new Point(-1,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,1), new Point(1,0), new Point(-1,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,1), new Point(1,0), new Point(0,1)}
            },
            // 8-piece
            {
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0), new Point(-1,1), new Point(0,1)},
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0), new Point(-1,1), new Point(1,1)}
            },
            // 9-piece
            {
                    {new Point(-1, -1), new Point(-1, 0), new Point(0, -1), new Point(0, 0), new Point(1,-1), new Point(1,0), new Point(-1,1), new Point(0,1), new Point(1,1)}
            }
    };

    private TextView mClock;

    /*
     *    TODO: Add speed increment
     *         TODO: add commercials
     *          TODO: add instructions
     *           TODO: add privacy documentation
     *            TODO: add settings
     *             Todo: add challenge mode (speed increment)
     *              todo:add infinity mode (no increment)
     *               todo: add decent construction mode (all levels below need to be connected)
     *                todo: add google play achievements
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        setContentView(R.layout.activity_main);
        initializeButtonImages();
        buttons = fillButtonArray();
        initialize(buttons);
        initializePressButtons();
        gamerun=false;
        gamestarted=false;
        initializeTopButtons();
        mHandler = new Handler();

    }

    public void initializeButtonImages(){
        ButtonImages[0]=R.drawable.ic_f0;
        ButtonImages[1]=R.drawable.ic_f1;
        ButtonImages[2]=R.drawable.ic_f2;
        ButtonImages[3]=R.drawable.ic_f3;
        ButtonImages[4]=R.drawable.ic_f4;
        ButtonImages[5]=R.drawable.ic_f5;
        ButtonImages[6]=R.drawable.ic_f6;
        ButtonImages[7]=R.drawable.ic_f7;
        ButtonImages[8]=R.drawable.ic_f8;
        ButtonImages[9]=R.drawable.ic_f9;
    }

    public void runGame(Bundle savedInstanceState){

        mythread = new FlatThread();
        mythread.start();

    }


    private boolean checkHorizontal(int move){
        // this needs to check is there are already blocks below the current one dropping
        boolean blocked=false;
        Point[] p =cPiece.block;
        //Log.println(Log.DEBUG,"block check at" +cPiece.value,"checking row" +cRow+", col" +(rcolumn+move));
        for (int i=0;i<p.length;i++){
            Point cp=p[i];
            if (cRow>0) {
                if (cRow-1+cp.y>0 && rcolumn+move+cp.x>0 && rcolumn+move+cp.x<12) {
                    if (well[cRow - 1 + cp.y][rcolumn + move + cp.x] > 0) {
                        //Log.println(Log.DEBUG, "blocked at" + cPiece.value, "checking row" + cRow + ", col" + (rcolumn + move));
                        blocked = true;
                    }
                }
            }
        }
        return blocked;
    }

    private void rotateRight(){
        clearPiece();
        Point [] ps = cPiece.block;
        boolean blocked=false;
        Point [] oldps=new Point[ps.length]; // needed to revert back if there is a block somewhere
        for (int i=0;i<ps.length;i++) {
            oldps[i]=new Point(ps[i].x,ps[i].y);
            if (ps[i].x == -1) {
                if (ps[i].y == -1) {
                    ps[i] = new Point(1, -1);
                } else if (ps[i].y == 0) {
                    ps[i] = new Point(0, -1);
                } else if (ps[i].y == 1) {
                    ps[i] = new Point(-1, -1);
                }
            } else if (ps[i].x == 0) {
                if (ps[i].y == -1) {
                    ps[i] = new Point(1, 0);
                } else if (ps[i].y == 1) {
                    ps[i] = new Point(-1, 0);
                }
            } else if (ps[i].x == 1) {
                if (ps[i].y == -1) {
                    ps[i] = new Point(1, 1);
                } else if (ps[i].y == 0) {
                    ps[i] = new Point(0, 1);
                } else if (ps[i].y == 1) {
                    ps[i] = new Point(-1, 1);
                }
            }
        }
        cPiece.block=ps;
        cPiece.calculateMaxMin();
        //Log.println(Log.DEBUG,"rotating" +cPiece.value,"max" +(cPiece.maxX+rcolumn)+", min" +(rcolumn+cPiece.minX));
        if (cPiece.maxX+rcolumn>11 ||cPiece.minX+rcolumn<0){
            blocked=true;
            //Log.println(Log.DEBUG,"blocking turn" +cPiece.value,"max" +(cPiece.maxX+rcolumn)+", min" +(rcolumn+cPiece.minX));
        }
        if (!blocked){
            for (int i=0;i<ps.length;i++) {
                if (ps[i].y+cRow<0||well[ps[i].y+cRow][ps[i].x+rcolumn]>0) {
                    blocked = true;
                    break;
                }
            }
        }
        if (blocked){
            cPiece.block=oldps;
        }
        fillPiece();
    }

    private void rotateLeft(){
        clearPiece();
        Point [] ps = cPiece.block;
        boolean blocked=false;
        Point [] oldps=new Point[ps.length]; // needed to revert back if there is a block somewhere
        for (int i=0;i<ps.length;i++) {
            oldps[i]=new Point(ps[i].x,ps[i].y);
            if (ps[i].x == -1) {
                if (ps[i].y == -1) {
                    ps[i] = new Point(-1, 1);
                } else if (ps[i].y == 0) {
                    ps[i] = new Point(0, 1);
                } else if (ps[i].y == 1) {
                    ps[i] = new Point(1, 1);
                }
            } else if (ps[i].x == 0) {
                if (ps[i].y == -1) {
                    ps[i] = new Point(-1, 0);
                } else if (ps[i].y == 1) {
                    ps[i] = new Point(1, 0);
                }
            } else if (ps[i].x == 1) {
                if (ps[i].y == -1) {
                    ps[i] = new Point(-1, -1);
                } else if (ps[i].y == 0) {
                    ps[i] = new Point(0, -1);
                } else if (ps[i].y == 1) {
                    ps[i] = new Point(1, -1);
                }
            }
        }
        cPiece.block=ps;
        cPiece.calculateMaxMin();
        //Log.println(Log.DEBUG,"rotating" +cPiece.value,"max" +(cPiece.maxX+rcolumn)+", min" +(rcolumn+cPiece.minX));
        if (cPiece.maxX+rcolumn>11 ||cPiece.minX+rcolumn<0){
            blocked=true;
            //Log.println(Log.DEBUG,"blocking turn" +cPiece.value,"max" +(cPiece.maxX+rcolumn)+", min" +(rcolumn+cPiece.minX));
        }
        if (!blocked){
            for (int i=0;i<ps.length;i++) {
                if (ps[i].y+cRow<0||ps[i].y+cRow>21 || well[ps[i].y+cRow][ps[i].x+rcolumn]>0) {
                    blocked = true;
                    break;
                }
            }
        }
        if (blocked){
            cPiece.block=oldps;
        }
        fillPiece();
    }

    private void setGameRun(boolean b){
        gamerun=b;
    }

    private void setGameStarted(boolean b){
        gamestarted=b;
    }

    private void initializeTopButtons(){
        ImageButton startGame =findViewById(R.id.bt1);
        startGame.setBackgroundColor(0xFFEEEEEE);
        startGame.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        startGame.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                //Log.println(Log.DEBUG,"start" ,"Button clicked" );
                if (!gamerun && !gamestarted){
                    // starts the game
                    runGame(savedInstanceState);
                    setGameRun(true);
                    setGameStarted(true);
                    startGame.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                } else if (gamestarted){
                    //Log.println(Log.DEBUG,"pause" ,"attempted pause" );
                    //Log.println(Log.DEBUG,"start" ,""+mythread.pause );
                    // set to pause
                    if (gamerun){
                        startGame.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        setGameRun(false);
                        mythread.pause=true;
                    } else {
                        startGame.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        setGameRun(true);
                        synchronized (mythread) {
                            mythread.pause=false;
                            mythread.notify();
                        }

                    }
                }
            }
        });

        Button Lives =findViewById(R.id.bt3);

        nLives=5;
        Lives.setText(""+nLives);
        Lives.setTextSize(30);
        //Lives.setText("5")


        ImageButton Settings =findViewById(R.id.bt2);
        Settings.setBackgroundColor(0xFFEEEEEE);
        Settings.setImageResource(R.drawable.ic_baseline_settings_24);
        Settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton Instr =findViewById(R.id.bt4);
        Instr.setBackgroundColor(0xFFEEEEEE);
        Instr.setImageResource(R.drawable.ic_baseline_text_snippet_24);
        Instr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //pause game and to to settings
                if(mythread!=null){
                    mythread.pause=true;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(mythread!=null){
                            mythread.pause=true;
                        }
                        new MaterialAlertDialogBuilder(flattris.this)
                                .setTitle("High Score view")
                                .setMessage("You have till now scored "+score+" points.")
                                .setPositiveButton("Go back to game", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (mythread!=null) {
                                            synchronized (mythread) {
                                                mythread.pause = false;
                                                mythread.notify();
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("View Highscore", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // now add to the highscore table:
                                        Intent intent =new Intent(getApplicationContext(),HighScore.class);
                                        intent.putExtra("Score", score);
                                        intent.putExtra("GameMode",10);
                                        startActivity(intent);
                                        if (mythread!=null) {
                                            synchronized (mythread) {
                                                mythread.pause = false;
                                                mythread.notify();
                                            }
                                        }
                                    }
                                })
                                .show();

                    }
                });
            }
        });

        ImageButton Exit =findViewById(R.id.bt5);
        Exit.setBackgroundColor(0xFFEEEEEE);
        Exit.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
        Exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //show message confirm to exit
            }
        });
    }

    private void initializePressButtons(){
        ImageButton moveRight =findViewById(R.id.b4);
        moveRight.setBackgroundColor(0xFF224466);
        moveRight.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
        moveRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (cPiece!=null) {
                    if (rcolumn + cPiece.maxX < 11) {
                        if (!checkHorizontal(1)) {
                            clearPiece();
                            rcolumn += 1;
                            fillPiece();
                        }
                    }
                }
            }
        });

        ImageButton moveLeft =findViewById(R.id.b2);
        moveLeft.setBackgroundColor(0xFF224466);
        moveLeft.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        moveLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (cPiece!=null) {
                    if (rcolumn + cPiece.minX > 0) {
                        if (!checkHorizontal(-1)) {
                            clearPiece();
                            rcolumn -= 1;
                            fillPiece();
                        }
                    }
                }
            }
        });

        ImageButton moveDown =findViewById(R.id.b3);
        moveDown.setBackgroundColor(0xFF224466);
        moveDown.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
        moveDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (cPiece!=null) {
                    delay = 0;
                }
            }
        });

        ImageButton turnLeft =findViewById(R.id.b1);
        turnLeft.setBackgroundColor(0xFF224466);
        turnLeft.setImageResource(R.drawable.ic_baseline_rotate_left_24);
        turnLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (cPiece!=null) {
                    rotateLeft();
                }
            }
        });

        ImageButton turnRight =findViewById(R.id.b5);
        turnRight.setBackgroundColor(0xFF224466);
        turnRight.setImageResource(R.drawable.ic_baseline_rotate_right_24);
        turnRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (cPiece!=null) {
                    rotateRight();
                }
            }
        });
    }

    private void fillPiece(){ // draw current block
        Point [] cp = cPiece.block;
        for (int i=0;i<cPiece.value;i++) {
            Point p = cp[i];
            if (((cRow+p.y-1)>=0)&&(rcolumn+p.x>=0)) {
                //buttons[cRow + p.y - 1][rcolumn + p.x].setBackgroundColor(colors[cPiece.value]);
                buttons[cRow+p.y-1][rcolumn+p.x].setBackgroundResource(ButtonImages[cPiece.value]);
            }
        }
    }

    private void clearPiece(){ // clear current block
        Point [] cp = cPiece.block;
        for (int i=0;i<cPiece.value;i++) {
            Point p = cp[i];
            if (((cRow+p.y-1)>=0)&&(rcolumn+p.x>=0)) {
                //buttons[cRow + p.y - 1][rcolumn + p.x].setBackgroundColor(Color.TRANSPARENT);
                buttons[cRow + p.y - 1][rcolumn+p.x].setBackgroundResource(ButtonImages[0]);
            }
        }
    }

    private Piece getNewPiece(int max){
        Log.println(Log.DEBUG, "getting new piece", "max val" + max);
        int blocksize = (int)Math.floor(Math.random()*max);
        Point [][] sizeblocks = FlatrisBlocks[blocksize];
        int block = (int)Math.floor(Math.random()*sizeblocks.length);
        Point [] currentBlock = sizeblocks[block];
        Piece p = new Piece(currentBlock, blocksize+1);
        return p;
    }

    private boolean checkBelow(Piece piece, int row, int col){
        // this needs to check is there are already blocks below the current one dropping
        boolean blocked=false;
        Point[] p =piece.block;
        //Log.println(Log.DEBUG,"block" +piece.value,"checking row" +row+", col" +col);
        for (int i=0;i<p.length;i++){
            Point cp=p[i];
            if (col+cp.x<0||row+1+cp.y>21||well[row + 1 + cp.y][col + cp.x] > 0) {
                blocked = true;
            }
        }
        if (blocked){
            fixBlock(p);
        }
        return blocked;
    }
    public void initialize(ImageButton [][] buttons){
        topreached=false;
        for (int i=0;i<12;i++){
            for (int j=0; j<22;j++){
                //buttons[j][i].setBackgroundColor(Color.TRANSPARENT);
                buttons[j][i].setBackgroundResource(R.drawable.ic_f0);
                well[j][i]=0;
            }
        }
    }

    public ImageButton[][] fillButtonArray(){
        ImageButton [][] bts =new ImageButton[22][12];
        bts[0][0]=findViewById(R.id.r0c0);
        bts[1][0]=findViewById(R.id.r1c0);
        bts[2][0]=findViewById(R.id.r2c0);
        bts[3][0]=findViewById(R.id.r3c0);
        bts[4][0]=findViewById(R.id.r4c0);
        bts[5][0]=findViewById(R.id.r5c0);
        bts[6][0]=findViewById(R.id.r6c0);
        bts[7][0]=findViewById(R.id.r7c0);
        bts[8][0]=findViewById(R.id.r8c0);
        bts[9][0]=findViewById(R.id.r9c0);
        bts[10][0]=findViewById(R.id.r10c0);
        bts[11][0]=findViewById(R.id.r11c0);
        bts[12][0]=findViewById(R.id.r12c0);
        bts[13][0]=findViewById(R.id.r13c0);
        bts[14][0]=findViewById(R.id.r14c0);
        bts[15][0]=findViewById(R.id.r15c0);
        bts[16][0]=findViewById(R.id.r16c0);
        bts[17][0]=findViewById(R.id.r17c0);
        bts[18][0]=findViewById(R.id.r18c0);
        bts[19][0]=findViewById(R.id.r19c0);
        bts[20][0]=findViewById(R.id.r20c0);
        bts[21][0]=findViewById(R.id.r21c0);

        bts[0][1]=findViewById(R.id.r0c1);
        bts[1][1]=findViewById(R.id.r1c1);
        bts[2][1]=findViewById(R.id.r2c1);
        bts[3][1]=findViewById(R.id.r3c1);
        bts[4][1]=findViewById(R.id.r4c1);
        bts[5][1]=findViewById(R.id.r5c1);
        bts[6][1]=findViewById(R.id.r6c1);
        bts[7][1]=findViewById(R.id.r7c1);
        bts[8][1]=findViewById(R.id.r8c1);
        bts[9][1]=findViewById(R.id.r9c1);
        bts[10][1]=findViewById(R.id.r10c1);
        bts[11][1]=findViewById(R.id.r11c1);
        bts[12][1]=findViewById(R.id.r12c1);
        bts[13][1]=findViewById(R.id.r13c1);
        bts[14][1]=findViewById(R.id.r14c1);
        bts[15][1]=findViewById(R.id.r15c1);
        bts[16][1]=findViewById(R.id.r16c1);
        bts[17][1]=findViewById(R.id.r17c1);
        bts[18][1]=findViewById(R.id.r18c1);
        bts[19][1]=findViewById(R.id.r19c1);
        bts[20][1]=findViewById(R.id.r20c1);
        bts[21][1]=findViewById(R.id.r21c1);

        bts[0][2]=findViewById(R.id.r0c2);
        bts[1][2]=findViewById(R.id.r1c2);
        bts[2][2]=findViewById(R.id.r2c2);
        bts[3][2]=findViewById(R.id.r3c2);
        bts[4][2]=findViewById(R.id.r4c2);
        bts[5][2]=findViewById(R.id.r5c2);
        bts[6][2]=findViewById(R.id.r6c2);
        bts[7][2]=findViewById(R.id.r7c2);
        bts[8][2]=findViewById(R.id.r8c2);
        bts[9][2]=findViewById(R.id.r9c2);
        bts[10][2]=findViewById(R.id.r10c2);
        bts[11][2]=findViewById(R.id.r11c2);
        bts[12][2]=findViewById(R.id.r12c2);
        bts[13][2]=findViewById(R.id.r13c2);
        bts[14][2]=findViewById(R.id.r14c2);
        bts[15][2]=findViewById(R.id.r15c2);
        bts[16][2]=findViewById(R.id.r16c2);
        bts[17][2]=findViewById(R.id.r17c2);
        bts[18][2]=findViewById(R.id.r18c2);
        bts[19][2]=findViewById(R.id.r19c2);
        bts[20][2]=findViewById(R.id.r20c2);
        bts[21][2]=findViewById(R.id.r21c2);

        bts[0][3]=findViewById(R.id.r0c3);
        bts[1][3]=findViewById(R.id.r1c3);
        bts[2][3]=findViewById(R.id.r2c3);
        bts[3][3]=findViewById(R.id.r3c3);
        bts[4][3]=findViewById(R.id.r4c3);
        bts[5][3]=findViewById(R.id.r5c3);
        bts[6][3]=findViewById(R.id.r6c3);
        bts[7][3]=findViewById(R.id.r7c3);
        bts[8][3]=findViewById(R.id.r8c3);
        bts[9][3]=findViewById(R.id.r9c3);
        bts[10][3]=findViewById(R.id.r10c3);
        bts[11][3]=findViewById(R.id.r11c3);
        bts[12][3]=findViewById(R.id.r12c3);
        bts[13][3]=findViewById(R.id.r13c3);
        bts[14][3]=findViewById(R.id.r14c3);
        bts[15][3]=findViewById(R.id.r15c3);
        bts[16][3]=findViewById(R.id.r16c3);
        bts[17][3]=findViewById(R.id.r17c3);
        bts[18][3]=findViewById(R.id.r18c3);
        bts[19][3]=findViewById(R.id.r19c3);
        bts[20][3]=findViewById(R.id.r20c3);
        bts[21][3]=findViewById(R.id.r21c3);

        bts[0][4]=findViewById(R.id.r0c4);
        bts[1][4]=findViewById(R.id.r1c4);
        bts[2][4]=findViewById(R.id.r2c4);
        bts[3][4]=findViewById(R.id.r3c4);
        bts[4][4]=findViewById(R.id.r4c4);
        bts[5][4]=findViewById(R.id.r5c4);
        bts[6][4]=findViewById(R.id.r6c4);
        bts[7][4]=findViewById(R.id.r7c4);
        bts[8][4]=findViewById(R.id.r8c4);
        bts[9][4]=findViewById(R.id.r9c4);
        bts[10][4]=findViewById(R.id.r10c4);
        bts[11][4]=findViewById(R.id.r11c4);
        bts[12][4]=findViewById(R.id.r12c4);
        bts[13][4]=findViewById(R.id.r13c4);
        bts[14][4]=findViewById(R.id.r14c4);
        bts[15][4]=findViewById(R.id.r15c4);
        bts[16][4]=findViewById(R.id.r16c4);
        bts[17][4]=findViewById(R.id.r17c4);
        bts[18][4]=findViewById(R.id.r18c4);
        bts[19][4]=findViewById(R.id.r19c4);
        bts[20][4]=findViewById(R.id.r20c4);
        bts[21][4]=findViewById(R.id.r21c4);

        bts[0][5]=findViewById(R.id.r0c5);
        bts[1][5]=findViewById(R.id.r1c5);
        bts[2][5]=findViewById(R.id.r2c5);
        bts[3][5]=findViewById(R.id.r3c5);
        bts[4][5]=findViewById(R.id.r4c5);
        bts[5][5]=findViewById(R.id.r5c5);
        bts[6][5]=findViewById(R.id.r6c5);
        bts[7][5]=findViewById(R.id.r7c5);
        bts[8][5]=findViewById(R.id.r8c5);
        bts[9][5]=findViewById(R.id.r9c5);
        bts[10][5]=findViewById(R.id.r10c5);
        bts[11][5]=findViewById(R.id.r11c5);
        bts[12][5]=findViewById(R.id.r12c5);
        bts[13][5]=findViewById(R.id.r13c5);
        bts[14][5]=findViewById(R.id.r14c5);
        bts[15][5]=findViewById(R.id.r15c5);
        bts[16][5]=findViewById(R.id.r16c5);
        bts[17][5]=findViewById(R.id.r17c5);
        bts[18][5]=findViewById(R.id.r18c5);
        bts[19][5]=findViewById(R.id.r19c5);
        bts[20][5]=findViewById(R.id.r20c5);
        bts[21][5]=findViewById(R.id.r21c5);

        bts[0][6]=findViewById(R.id.r0c6);
        bts[1][6]=findViewById(R.id.r1c6);
        bts[2][6]=findViewById(R.id.r2c6);
        bts[3][6]=findViewById(R.id.r3c6);
        bts[4][6]=findViewById(R.id.r4c6);
        bts[5][6]=findViewById(R.id.r5c6);
        bts[6][6]=findViewById(R.id.r6c6);
        bts[7][6]=findViewById(R.id.r7c6);
        bts[8][6]=findViewById(R.id.r8c6);
        bts[9][6]=findViewById(R.id.r9c6);
        bts[10][6]=findViewById(R.id.r10c6);
        bts[11][6]=findViewById(R.id.r11c6);
        bts[12][6]=findViewById(R.id.r12c6);
        bts[13][6]=findViewById(R.id.r13c6);
        bts[14][6]=findViewById(R.id.r14c6);
        bts[15][6]=findViewById(R.id.r15c6);
        bts[16][6]=findViewById(R.id.r16c6);
        bts[17][6]=findViewById(R.id.r17c6);
        bts[18][6]=findViewById(R.id.r18c6);
        bts[19][6]=findViewById(R.id.r19c6);
        bts[20][6]=findViewById(R.id.r20c6);
        bts[21][6]=findViewById(R.id.r21c6);

        bts[0][7]=findViewById(R.id.r0c7);
        bts[1][7]=findViewById(R.id.r1c7);
        bts[2][7]=findViewById(R.id.r2c7);
        bts[3][7]=findViewById(R.id.r3c7);
        bts[4][7]=findViewById(R.id.r4c7);
        bts[5][7]=findViewById(R.id.r5c7);
        bts[6][7]=findViewById(R.id.r6c7);
        bts[7][7]=findViewById(R.id.r7c7);
        bts[8][7]=findViewById(R.id.r8c7);
        bts[9][7]=findViewById(R.id.r9c7);
        bts[10][7]=findViewById(R.id.r10c7);
        bts[11][7]=findViewById(R.id.r11c7);
        bts[12][7]=findViewById(R.id.r12c7);
        bts[13][7]=findViewById(R.id.r13c7);
        bts[14][7]=findViewById(R.id.r14c7);
        bts[15][7]=findViewById(R.id.r15c7);
        bts[16][7]=findViewById(R.id.r16c7);
        bts[17][7]=findViewById(R.id.r17c7);
        bts[18][7]=findViewById(R.id.r18c7);
        bts[19][7]=findViewById(R.id.r19c7);
        bts[20][7]=findViewById(R.id.r20c7);
        bts[21][7]=findViewById(R.id.r21c7);

        bts[0][8]=findViewById(R.id.r0c8);
        bts[1][8]=findViewById(R.id.r1c8);
        bts[2][8]=findViewById(R.id.r2c8);
        bts[3][8]=findViewById(R.id.r3c8);
        bts[4][8]=findViewById(R.id.r4c8);
        bts[5][8]=findViewById(R.id.r5c8);
        bts[6][8]=findViewById(R.id.r6c8);
        bts[7][8]=findViewById(R.id.r7c8);
        bts[8][8]=findViewById(R.id.r8c8);
        bts[9][8]=findViewById(R.id.r9c8);
        bts[10][8]=findViewById(R.id.r10c8);
        bts[11][8]=findViewById(R.id.r11c8);
        bts[12][8]=findViewById(R.id.r12c8);
        bts[13][8]=findViewById(R.id.r13c8);
        bts[14][8]=findViewById(R.id.r14c8);
        bts[15][8]=findViewById(R.id.r15c8);
        bts[16][8]=findViewById(R.id.r16c8);
        bts[17][8]=findViewById(R.id.r17c8);
        bts[18][8]=findViewById(R.id.r18c8);
        bts[19][8]=findViewById(R.id.r19c8);
        bts[20][8]=findViewById(R.id.r20c8);
        bts[21][8]=findViewById(R.id.r21c8);

        bts[0][9]=findViewById(R.id.r0c9);
        bts[1][9]=findViewById(R.id.r1c9);
        bts[2][9]=findViewById(R.id.r2c9);
        bts[3][9]=findViewById(R.id.r3c9);
        bts[4][9]=findViewById(R.id.r4c9);
        bts[5][9]=findViewById(R.id.r5c9);
        bts[6][9]=findViewById(R.id.r6c9);
        bts[7][9]=findViewById(R.id.r7c9);
        bts[8][9]=findViewById(R.id.r8c9);
        bts[9][9]=findViewById(R.id.r9c9);
        bts[10][9]=findViewById(R.id.r10c9);
        bts[11][9]=findViewById(R.id.r11c9);
        bts[12][9]=findViewById(R.id.r12c9);
        bts[13][9]=findViewById(R.id.r13c9);
        bts[14][9]=findViewById(R.id.r14c9);
        bts[15][9]=findViewById(R.id.r15c9);
        bts[16][9]=findViewById(R.id.r16c9);
        bts[17][9]=findViewById(R.id.r17c9);
        bts[18][9]=findViewById(R.id.r18c9);
        bts[19][9]=findViewById(R.id.r19c9);
        bts[20][9]=findViewById(R.id.r20c9);
        bts[21][9]=findViewById(R.id.r21c9);

        bts[0][10]=findViewById(R.id.r0c10);
        bts[1][10]=findViewById(R.id.r1c10);
        bts[2][10]=findViewById(R.id.r2c10);
        bts[3][10]=findViewById(R.id.r3c10);
        bts[4][10]=findViewById(R.id.r4c10);
        bts[5][10]=findViewById(R.id.r5c10);
        bts[6][10]=findViewById(R.id.r6c10);
        bts[7][10]=findViewById(R.id.r7c10);
        bts[8][10]=findViewById(R.id.r8c10);
        bts[9][10]=findViewById(R.id.r9c10);
        bts[10][10]=findViewById(R.id.r10c10);
        bts[11][10]=findViewById(R.id.r11c10);
        bts[12][10]=findViewById(R.id.r12c10);
        bts[13][10]=findViewById(R.id.r13c10);
        bts[14][10]=findViewById(R.id.r14c10);
        bts[15][10]=findViewById(R.id.r15c10);
        bts[16][10]=findViewById(R.id.r16c10);
        bts[17][10]=findViewById(R.id.r17c10);
        bts[18][10]=findViewById(R.id.r18c10);
        bts[19][10]=findViewById(R.id.r19c10);
        bts[20][10]=findViewById(R.id.r20c10);
        bts[21][10]=findViewById(R.id.r21c10);

        bts[0][11]=findViewById(R.id.r0c11);
        bts[1][11]=findViewById(R.id.r1c11);
        bts[2][11]=findViewById(R.id.r2c11);
        bts[3][11]=findViewById(R.id.r3c11);
        bts[4][11]=findViewById(R.id.r4c11);
        bts[5][11]=findViewById(R.id.r5c11);
        bts[6][11]=findViewById(R.id.r6c11);
        bts[7][11]=findViewById(R.id.r7c11);
        bts[8][11]=findViewById(R.id.r8c11);
        bts[9][11]=findViewById(R.id.r9c11);
        bts[10][11]=findViewById(R.id.r10c11);
        bts[11][11]=findViewById(R.id.r11c11);
        bts[12][11]=findViewById(R.id.r12c11);
        bts[13][11]=findViewById(R.id.r13c11);
        bts[14][11]=findViewById(R.id.r14c11);
        bts[15][11]=findViewById(R.id.r15c11);
        bts[16][11]=findViewById(R.id.r16c11);
        bts[17][11]=findViewById(R.id.r17c11);
        bts[18][11]=findViewById(R.id.r18c11);
        bts[19][11]=findViewById(R.id.r19c11);
        bts[20][11]=findViewById(R.id.r20c11);
        bts[21][11]=findViewById(R.id.r21c11);

        return bts;
    }


    public void newPiece(){
        pieceOrigin = new Point (5,2);
        rotation = 0;
        // here we need to get a random piece
        //first we take a piece 1, the  a random piece with max 1 above the level that ia already there
    }

    private class Piece{
        Point [] block;
        int value;
        int maxX;
        int minX;
        Piece (Point[] block, int value){
            this.block =block;
            this.value=value;
            calculateMaxMin();
        }

        private void calculateMaxMin(){
            maxX=Integer.MIN_VALUE;
            minX=Integer.MAX_VALUE;
            for (int i=0;i<block.length;i++){
                if (block[i].x<minX){
                    minX=block[i].x;
                }
                if (block[i].x>maxX){
                    maxX=block[i].x;
                }
            }
        }
    }

    private boolean checkNeighbours(Point [] checkP){
        if (cPiece.value==1){
            return true;
        }
        for (int i = 0; i < checkP.length; i++) {
            Point p = checkP[i];
                // we need to check already fixed cells around for their value
                Log.println(Log.DEBUG, "Checking", "point" + (cRow + p.y) + ", col" + (rcolumn + p.x));
                if ( cRow+1+p.y<22 && well[cRow +1+ p.y][rcolumn + p.x] == cPiece.value-1){
                    return true;
                } else if (cRow-1+p.y>=0 &&well[cRow -1+ p.y][rcolumn + p.x] == cPiece.value-1){
                    return true;
                } else if (rcolumn-1+p.x>=0&&well[cRow + p.y][rcolumn -1+ p.x] == cPiece.value-1){
                    Log.println(Log.DEBUG, "Checking left", "point" + (cRow + p.y) + ", col" + (rcolumn -1+ p.x));
                    return true;
                } else if (rcolumn+1+p.x<12&&well[cRow + p.y][rcolumn +1+ p.x] == cPiece.value-1){
                    return true;
                }
        }
        nLives-=1;
        Button Lives =findViewById(R.id.bt3);
        Lives.setText(""+nLives);
        if (nLives==0){
            int tscore =0;
            for (int i=0;i<22;i++){
                for (int j=0;j<12;j++){
                    score +=well[i][j];
                    tscore+=well[i][j];
                }
            }
            showEndMessage(tscore);
        }
        return false;
    }

    private void fixBlock(Point [] toFix){
        //checkneighbours here
        boolean fix = checkNeighbours(toFix);
        if (fix) {
            int cmax=0;
            for (int i=0;i<22;i++){
                for (int j=0;j<12;j++){
                    if (well[i][j]>cmax){
                        cmax=well[i][j];
                    }
                }
            }
            maxWell=Math.min(cmax+1,9);
            for (int i = 0; i < toFix.length; i++) {
                Point p = toFix[i];
                well[cRow + p.y][rcolumn + p.x] = cPiece.value;
                // Log.println(Log.DEBUG, "set 1", "setting row" + (cRow + p.y) + ", col" + (rcolumn + p.x));
                if (cRow + p.y == 0) {
                    topreached = true;
                }
            }
        } else {
            repaint();
        }
        // check full lines here
        ArrayList<Integer> remove = checkFullRows();
        // remove full lines here
        if (remove.size()>0){
            removeFullRows(remove);
        }
    }

    private void removeFullRows(ArrayList<Integer> remove){
        // this contains all integer that need to be removed, best way is to start at the bottom
        // add a counter to move lines above more down
        int count =0;
        for (int i=remove.size()-1;i>=0;i--){ // reverse through the lines
            int cremove = remove.get(i)+count;
            //Log.println(Log.DEBUG, "Removing row ", "row" +cremove);
            for (int j=cremove;j>=0;j--){
                //j is the first row that needs to be removed
                //Log.println(Log.DEBUG, "j= "+j, "row" +cremove);
                //Log.println(Log.DEBUG, "Moving row "+j, "row" +cremove);
                if (j-1>0) {
                    well[j] = well[j - 1];
                } else {
                    well[j]= new int[12];
                    for (int k=0;k<12;k++){
                        well[j][k]=0;
                    }
                }
            }
            count++;
        }
        //repaint needs to be done
        repaint();
    }

    private void repaint(){
        for (int i=0;i<22;i++){
            for (int j=0;j<12;j++){
                //buttons[i][j].setBackgroundColor(colors[well[i][j]]);
                buttons[i][j].setBackgroundResource(ButtonImages[well[i][j]]);
            }
        }
    }

    private ArrayList<Integer> checkFullRows(){
        ArrayList<Integer> fullrows = new ArrayList<Integer>();
        for (int i=0;i<well.length;i++){
            int count=0;
            int tscore=0;
            for (int j=0;j<well[0].length;j++){
                if (well[i][j]>0){
                    count++;
                    tscore+=well[i][j];
                }
            }
            if (count==12){
                score+=tscore;
                fullrows.add(i);
            }
        }
        return fullrows;
    }
    private void showEndMessage(int tscore){
        // game over. show game over graphic
//        Looper.prepare();
        runOnUiThread(new Runnable() {
            public void run() {

                //your alert dialog here..


        new MaterialAlertDialogBuilder(flattris.this)
                .setTitle("Game Over")
                .setMessage("You have just scored "+score+" points.")
                .setPositiveButton("Watch add four 1 life", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nLives+=1;
                        Button Lives =findViewById(R.id.bt3);
                        Lives.setText(""+nLives);
                        score-=tscore;
                        synchronized (mythread) {
                            mythread.pause=false;
                            mythread.notify();
                        }
                    }
                })
                .setNegativeButton("Quit game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mythread.interrupt();
                        gamerun=false;
                        gamestarted=false;
                        nLives=5;
                        Button Lives =findViewById(R.id.bt3);
                        Lives.setText(""+nLives);
                        for (int k=0;k<22;k++){
                            for (int l=0;l<12;l++){
                                well[k][l]=0;
                            }
                        }
                        repaint();
                        ImageButton startGame =findViewById(R.id.bt1);
                        startGame.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        // now add to the highscore table:
                        Intent intent =new Intent(getApplicationContext(),HighScore.class);
                        intent.putExtra("Score", score);
                        intent.putExtra("GameMode",0);
                        Log.println(Log.DEBUG,"done","val" +score);
                        startActivity(intent);
                    }
                })
                .show();
            }
        });
    }

    private class FlatThread extends Thread{

        public boolean pause = false;

        public void run(){
            topreached=false;
            score=0;
            pieceFalling=false;
            maxWell=1;
            cPiece = getNewPiece(maxWell);
            rcolumn=5;
            while (topreached ==false ){ // split to set pause from this thread?
                synchronized (this) {
                    if (pause) {
                        try {
                            wait();
                        } catch (InterruptedException e){
                        }
                    }
                }
                if (nLives>0) {
                    if (pieceFalling == false) {
                        cRow = 0;
                        cPiece = getNewPiece(maxWell);
                        rcolumn = (int) (Math.floor(Math.random() * 5) + Math.floor(Math.random() * 5)) + 1;
                        pieceFalling = true;
                    }
                    try {
                        Thread.sleep(delay);
                        Point[] cp = cPiece.block;
                        //Log.println(Log.DEBUG,"done","val" +cPiece.value);
                        if (cRow > 0) { // clear previous drawing
                            clearPiece();
                        }
                        for (int i = 0; i < cPiece.value; i++) {
                            Point p = cp[i];
                            if (((cRow + p.y) >= 0) && (rcolumn + p.x >= 0)) {
                                //buttons[cRow + p.y][rcolumn + p.x].setBackgroundColor(colors[cPiece.value]);
                                buttons[cRow+p.y][rcolumn+p.x].setBackgroundResource(ButtonImages[cPiece.value]);
                            }
                        }


                    } catch (InterruptedException e) {
                    }
                    if (cRow > 20) {
                        //topreached=true;

                        pieceFalling = false;

                        Point[] cp = cPiece.block;
                        fixBlock(cp);
                        cRow = 0;
                        delay = 1000;
                    } else {
                        pieceFalling = !checkBelow(cPiece, cRow, rcolumn);
                        if (!pieceFalling && cRow == 0) {
                            topreached = true;
                        } else if (!pieceFalling) {
                            cRow = 0;
                            delay = 1000;
                        }
                    }
                    cRow += 1;
                } else {
                    this.pause=true;
                }
            }
            mHandler.post(new Runnable() {
                public void run(){
                    showEndMessage(0);
                }
            });
        }

    }
}

