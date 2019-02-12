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

    private ArrayList<Word> a;
    private Random r = new Random();
    private Word lastDisplayedWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.Success)).setOnClickListener(this);
        ((Button) findViewById(R.id.Failure)).setOnClickListener(this);
        ((Button) findViewById(R.id.ShowAnswer)).setOnClickListener(this);

        displayNext();
    }

    private void resetList() {
        try {
            a = Helper.getWordList(this, 13);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Triple each words in th list
        for (int i = a.size() - 1; i >= 0; i--) {
            a.add(a.get(i));
            a.add(a.get(i));
        }
    }

    private void displayNext() {
        Word w = a.get(r.nextInt(a.size()));
        while (w == lastDisplayedWord) {
            w = a.get(r.nextInt(a.size()));
        }
        lastDisplayedWord = w;
        ((TextView) findViewById(R.id.Answer)).setText("");
        ((TextView) findViewById(R.id.Additionnal)).setText("");
        ((TextView) findViewById(R.id.Display)).setText(lastDisplayedWord.EnglishValue);
        toggleButtons(true);
    }

    private void toggleButtons(boolean ShowAnswerButton) {
        if (ShowAnswerButton) {
            ((Button) findViewById(R.id.Success)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.ShowAnswer)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.Failure)).setVisibility(View.GONE);
        } else {
            ((Button) findViewById(R.id.Success)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.ShowAnswer)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.Failure)).setVisibility(View.VISIBLE);
        }
    }

    private void showAnswer() {
        ((TextView) findViewById(R.id.Answer)).setText(lastDisplayedWord.GermanValue);
        ((TextView) findViewById(R.id.Additionnal)).setText(lastDisplayedWord.AdditionnalGermanValue);
        toggleButtons(false);
    }

    private void applyAnswer(boolean b) {
        if (b) {
            a.remove(lastDisplayedWord);
            if (a.size() == 0)
                resetList();
        } else {
            a.add(lastDisplayedWord);
        }
        displayNext();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ShowAnswer:
                showAnswer();
                break;
            case R.id.Success:
                applyAnswer(true);
                break;
            case R.id.Failure:
                applyAnswer(false);
                break;
        }
    }
}
