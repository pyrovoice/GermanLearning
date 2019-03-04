package com.example.maxim.germanlearning;

import android.content.Context;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Helper {
    private static ArrayList<Word> allWords = new ArrayList<>();
    private static final String ABSOLUTE_PATH_TO_FILE = "/mnt/sdcard/GermanLearning/existingWords.txt";
    private static final String SEPARATOR = ";";
    private static final String COUNT_SEPARATOR = "-";


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

            Word w = new Word(WordType.noun, "A " + parts[0], germanValue[0], germanValue[1]);
            allWords.add(w);
        }
        r = context.getResources().openRawResource(R.raw.verbs);
        reader = new BufferedReader(new InputStreamReader(r));
        while (line != null) {
            String[] parts = line.split("-");
            String german = parts[0];
            if (german.contains(",")) {
                german = german.split(",")[0];
            }
            String eng = parts[1];
            for (String s : eng.split(";")) {
                Word w = new Word(WordType.verb, "To " + s, german, null);
                allWords.add(w);
            }
        }
    }

    private static void writeToFile(String path, String toWrite) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            byte[] buffer = toWrite.getBytes(StandardCharsets.UTF_8);
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String upsertWordListFromWords(Word words, int nbrOccurence) {
        String fileContent = "";
        for (Word w : words) {
            fileContent += w.wt + SEPARATOR + w.EnglishValue + SEPARATOR + w.GermanValue + "\n";
        }
        Helper.writeToFile(ABSOLUTE_PATH_TO_LISTS + fileName, fileContent);
        return fileName;
    }


    public static ArrayList<Word> getExistingWordList(ArrayList<Word> a) throws Exception{
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(ABSOLUTE_PATH_TO_FILE)));
        ArrayList<Word> words = new ArrayList<>();
        for (String line; (line = r.readLine()) != null; ) {
            String[] parts = line.split(COUNT_SEPARATOR);
            int counter = Integer.parseInt(parts[1]);
            String[] wordDetails = parts[0].split(SEPARATOR);
            for(int i = 0; i < counter; i++)
                words.add(new Word(wordDetails[0], wordDetails[1], wordDetails[2]));
        }
        return words;
    }
}
