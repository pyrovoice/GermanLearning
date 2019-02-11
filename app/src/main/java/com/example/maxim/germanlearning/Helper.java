package com.example.maxim.germanlearning;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Helper {
    private static ArrayList<Word> allWords = new ArrayList<>();

    public static ArrayList<Word> getWordList(Context context, int nbr) throws IOException {
        if (allWords.size() <= 0) {
            loadWords(context);
        }
        Random r = new Random();
        ArrayList<Word> rWords = new ArrayList<>();
        while (rWords.size() < nbr) {
            rWords.add(allWords.get(r.nextInt(allWords.size())));
        }
        return rWords;
    }

    private static void loadWords(Context context) throws IOException {
        allWords = new ArrayList<>();
        InputStream r = context.getResources().openRawResource(R.raw.nouns);
        BufferedReader reader = new BufferedReader(new InputStreamReader(r));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("–");
            String[] germanValue;
            germanValue = parts[1].split("_");

            Word w = new Word(WordType.noun, parts[0], germanValue[0], germanValue[1]);
            allWords.add(w);
        }
        r = context.getResources().openRawResource(R.raw.verbs);
        reader = new BufferedReader(new InputStreamReader(r));
        while (line != null) {
            int a = line.indexOf(" ");
            String german = line.substring(0, a);
            String eng = line.substring(a + 1);
            Word w = new Word(WordType.verb, eng, german, null);
            allWords.add(w);
        }
    }
}
