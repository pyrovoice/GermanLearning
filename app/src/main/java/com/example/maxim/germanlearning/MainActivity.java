package com.example.maxim.germanlearning;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private  ArrayList<Word> a;
    private Random r = new Random();
    private Word lastDisplayedWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.button)).setOnClickListener(this);
        try {
           a = Helper.getWordList(this, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayNext();
    }

    private void displayNext(){
        Word w = a.get(r.nextInt(a.size()));
        while(w == lastDisplayedWord){
            w = a.get(r.nextInt(a.size()));
        }
        lastDisplayedWord = w;
        ((TextView) findViewById(R.id.Answer)).setText("");
        ((TextView) findViewById(R.id.Additionnal)).setText("");
        ((TextView) findViewById(R.id.Display)).setText(lastDisplayedWord.EnglishValue);
    }

    private void showAnswer() {
        ((TextView) findViewById(R.id.Answer)).setText(lastDisplayedWord.GermanValue);
        ((TextView) findViewById(R.id.Additionnal)).setText(lastDisplayedWord.AdditionnalGermanValue);
    }

    @Override
    public void onClick(View v) {
        if(((TextView) findViewById(R.id.Answer)).getText().equals("")){
            showAnswer();
        }else{
            displayNext();
        }
    }
}
