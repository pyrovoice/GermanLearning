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
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Word> a = new ArrayList<>();
    private Random r = new Random();
    private Word lastDisplayedWord;
    private String wordListName;
    private int requiredNbrWord = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.Success)).setOnClickListener(this);
        ((Button) findViewById(R.id.Failure)).setOnClickListener(this);
        ((Button) findViewById(R.id.ShowAnswer)).setOnClickListener(this);

        if (getIntent().getBooleanExtra("loadExistingSuite", false)) {
            ((Button) findViewById(R.id.Reroll)).setVisibility(View.GONE);
            try {
                a = Helper.getExistingWordList();
                // Determine the number of different words
                HashSet<Word> hs = new HashSet<Word>(a);
                hs.clear();
                requiredNbrWord = hs.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ((Button) findViewById(R.id.Reroll)).setOnClickListener(this);
            try {
                a = Helper.getWordList(this, requiredNbrWord);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = a.size() - 1; i >= 0; i--) {
                a.add(a.get(i));
                a.add(a.get(i));
            }
        }
        UpdateInfoTextView();
        displayNext();
    }

    private void displayNext() {
        Word w = a.get(r.nextInt(a.size()));
        while (w == lastDisplayedWord) {
            w = a.get(r.nextInt(a.size()));
        }
        lastDisplayedWord = w;
        ((TextView) findViewById(R.id.Answer)).setText("");
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
        toggleButtons(false);
    }

    private void applyAnswer(boolean b) {
        if (b) {
            // Check if it's the last one
            int counter = 0;
            for (Word w : a) {
                if (w.EnglishValue.equals(lastDisplayedWord.EnglishValue)) {
                    counter++;
                }
            }
            if (counter > 1)
                a.remove(lastDisplayedWord);
        } else {
            a.add(lastDisplayedWord);
        }
        UpdateInfoTextView();
        Helper.upsertWordListFromWords(lastDisplayedWord, Collections.frequency(a, lastDisplayedWord));
        displayNext();
    }

    public void UpdateInfoTextView(){
        int nbrWordMastered = 0;
        for(Word w: a){
            if(Collections.frequency(a, w) == 1){
                nbrWordMastered++;
            }
        }
        String s = "Word mastered: " + nbrWordMastered + "/" + requiredNbrWord;
        ((TextView) findViewById(R.id.currentWordInfo)).setText(s);
    }

    /***
     * Remove all instance of the current selected word from the list, then add a new word to the list, before displaying the next word.
     */
    private void rerollCurrentWord() {
        for (int i = a.size() - 1; i >= 0; i--) {
            if (lastDisplayedWord.EnglishValue.equals(a.get(i).EnglishValue))
                a.remove(i);
        }
        try {
            Word newWord = Helper.getWordList(this, 1).get(0);

            a.add(newWord);
            a.add(newWord);
            a.add(newWord);
        } catch (Exception e) {

        }
        UpdateInfoTextView();
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
            case R.id.Reroll:
                rerollCurrentWord();
                break;
        }
    }
}
