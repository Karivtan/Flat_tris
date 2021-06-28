package com.example.flattris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {
    public static final String HighScoreFile = "FlatTrisHighScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Button Settings =findViewById(R.id.ResetScores);
        Settings.setOnClickListener(this::resetHighScore);

        Button Settings2 =findViewById(R.id.BackSettings);
        Settings2.setOnClickListener(this::reStartGame);

        TextView Inf1 = findViewById(R.id.Inf1);
        TextView Inf2 = findViewById(R.id.Inf2);
        TextView Inf3 = findViewById(R.id.Inf3);

        TextView Cha1 = findViewById(R.id.Ch1);
        TextView Cha2 = findViewById(R.id.Ch2);
        TextView Cha3 = findViewById(R.id.Ch3);

        TextView Con1 = findViewById(R.id.Con1);
        TextView Con2 = findViewById(R.id.Con2);
        TextView Con3 = findViewById(R.id.Con3);

        int newScore =getIntent().getIntExtra("Score",0);
        int gameMode =getIntent().getIntExtra("GameMode",0);
        SharedPreferences Data = getSharedPreferences(HighScoreFile, MODE_PRIVATE);

        int ScoreI1 = Data.getInt("Inf1",0);
        int ScoreI2 = Data.getInt("Inf2",0);
        int ScoreI3 = Data.getInt("Inf3",0);

        int ScoreC1 = Data.getInt("Con1",0);
        int ScoreC2 = Data.getInt("Con2",0);
        int ScoreC3 = Data.getInt("Con3",0);

        int ScoreCH1 = Data.getInt("Cha1",0);
        int ScoreCH2 = Data.getInt("Cha2",0);
        int ScoreCH3 = Data.getInt("Cha3",0);

        Log.println(Log.DEBUG,"Highscore","cur val" +ScoreI1);
        if (gameMode==0){ //infintiy mode
            if (newScore>ScoreI1){
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Inf1",newScore);
                editor.putInt("Inf2",ScoreI1);
                editor.putInt("Inf3",ScoreI2);
                editor.apply();
            } else if (newScore>ScoreI2){
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Inf2",newScore);
                editor.putInt("Inf3",ScoreI2);
                editor.apply();
            } else if (newScore>ScoreI3) {
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Inf3", newScore);
                editor.apply();
            }
        } else if (gameMode==1){ // challenge mode
            if (newScore>ScoreCH1){
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Cha1",newScore);
                editor.putInt("Cha2",ScoreCH1);
                editor.putInt("Cha3",ScoreCH2);
                editor.apply();
            } else if (newScore>ScoreCH2){
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Cha2",newScore);
                editor.putInt("Cha3",ScoreCH2);
                editor.apply();
            } else if (newScore>ScoreCH3) {
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Cha3", newScore);
                editor.apply();
            }
        } else { // construction mode
            if (newScore>ScoreC1){
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Con1",newScore);
                editor.putInt("Con2",ScoreC1);
                editor.putInt("Con",ScoreC2);
                editor.apply();
            } else if (newScore>ScoreC2){
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Con2",newScore);
                editor.putInt("Con3",ScoreC2);
                editor.apply();
            } else if (newScore>ScoreC3) {
                SharedPreferences.Editor editor = Data.edit();
                editor.putInt("Con3", newScore);
                editor.apply();
            }
        }
        // reread scores after adaptation
        updateHighScore(Data);

    }

    public void reStartGame(View view){
        int gameMode =getIntent().getIntExtra("GameMode",0);
        if (gameMode==10){
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), flattris.class));
        }
    }

    public void resetHighScore(View view){
        SharedPreferences Data = getSharedPreferences(HighScoreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = Data.edit();
        editor.putInt("Inf1",0);
        editor.putInt("Inf2",0);
        editor.putInt("Inf3",0);
        editor.putInt("Cha1",0);
        editor.putInt("Cha2",0);
        editor.putInt("Cha3",0);
        editor.putInt("Con1",0);
        editor.putInt("Con2",0);
        editor.putInt("Con",0);
        editor.apply();
    }

    public void updateHighScore(SharedPreferences Data){

        int ScoreI1 = Data.getInt("Inf1",0);
        int ScoreI2 = Data.getInt("Inf2",0);
        int ScoreI3 = Data.getInt("Inf3",0);

        int ScoreC1 = Data.getInt("Con1",0);
        int ScoreC2 = Data.getInt("Con2",0);
        int ScoreC3 = Data.getInt("Con3",0);

        int ScoreCH1 = Data.getInt("Cha1",0);
        int ScoreCH2 = Data.getInt("Cha2",0);
        int ScoreCH3 = Data.getInt("Cha3",0);

        TextView Inf1 = findViewById(R.id.Inf1);
        TextView Inf2 = findViewById(R.id.Inf2);
        TextView Inf3 = findViewById(R.id.Inf3);

        TextView Cha1 = findViewById(R.id.Ch1);
        TextView Cha2 = findViewById(R.id.Ch2);
        TextView Cha3 = findViewById(R.id.Ch3);

        TextView Con1 = findViewById(R.id.Con1);
        TextView Con2 = findViewById(R.id.Con2);
        TextView Con3 = findViewById(R.id.Con3);

        // settextview scores
        Resources res = getResources();
        Inf1.setText(""+ScoreI1);
        Inf2.setText(""+ScoreI2);
        Inf3.setText(""+ScoreI3);

        Cha1.setText(""+ScoreCH1);
        Cha2.setText(""+ScoreCH2);
        Cha3.setText(""+ScoreCH3);

        Con1.setText(""+ScoreC1);
        Con2.setText(""+ScoreC2);
        Con3.setText(""+ScoreC3);
    }


}