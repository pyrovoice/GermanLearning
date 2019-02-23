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
    private static ArrayList<Word> verbs = new ArrayList<>();
    private static ArrayList<Word> nouns = new ArrayList<>();
    private static ArrayList<Word> other = new ArrayList<>();
    private static final String ABSOLUTE_PATH_TO_LISTS = "/mnt/sdcard/GermanLearning/";
    private static final String SEPARATOR = ";";

    public static ArrayList<Word> getWordList(Context context, int nbr) throws IOException {
        if (verbs.size() <= 0) {
            loadWords(context);
        }
        Random r = new Random();
        ArrayList<Word> rWords = new ArrayList<>();
        for (int i = 0; i < (nbr/3 + nbr%3);i++) {
            rWords.add(nouns.get(r.nextInt(nouns.size())));
        }
        for (int i = 0; i < (nbr/3);i++) {
            rWords.add(verbs.get(r.nextInt(verbs.size())));
        }
        for (int i = 0; i < (nbr/3);i++) {
            rWords.add(other.get(r.nextInt(other.size())));
        }
        return rWords;
    }

    private static void loadWords(Context context) throws IOException {
        InputStream r = context.getResources().openRawResource(R.raw.nouns);
        BufferedReader reader = new BufferedReader(new InputStreamReader(r));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("–");
            String[] germanValue;
            germanValue = parts[1].split("_");

            Word w = new Word(WordType.noun, "(n) " + parts[0], germanValue[0], germanValue[1]);
            nouns.add(w);
        }
        r = context.getResources().openRawResource(R.raw.verbs);
        reader = new BufferedReader(new InputStreamReader(r));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("-");
            String german = parts[0];
            if (german.contains(",")) {
                german = german.split(",")[0];
            }
            String eng = parts[1];
            Word w = new Word(WordType.verb, "(v) " + eng.split(";")[0], german, null);
            verbs.add(w);

        }
        r = context.getResources().openRawResource(R.raw.other);
        reader = new BufferedReader(new InputStreamReader(r));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("-");
            String german = parts[0];
            String eng = parts[1];
            Word w = new Word(WordType.other, eng.split(";")[0], german, null);
            other.add(w);
        }
    }

    private static void writeToFile(String path, String toWrite) {
        //Create files if don't exists
        File f = new File(ABSOLUTE_PATH_TO_LISTS);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public static String createWordListFromWords(ArrayList<Word> words) {
        return createWordListFromWords(words, null);
    }

    public static String createWordListFromWords(ArrayList<Word> words, String fileName) {
        if (fileName == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            fileName = format.format(new Date());
        }
        String fileContent = "";
        for (Word w : words) {
            fileContent += w.wt + SEPARATOR + w.EnglishValue + SEPARATOR + w.GermanValue + "\n";
        }
        Helper.writeToFile(ABSOLUTE_PATH_TO_LISTS + fileName, fileContent);
        return fileName;
    }

    public static ArrayList<Word> getWordFromList(String fileName) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(ABSOLUTE_PATH_TO_LISTS + fileName)));
        ArrayList<Word> words = new ArrayList<>();
        for (String line; (line = r.readLine()) != null; ) {
            String[] parts = line.split(SEPARATOR);
            words.add(new Word(parts[0], parts[1], parts[2]));
        }
        return words;

    }

    public static String getRandomWordList(ArrayList<Word> a) throws IOException {
        File folder = new File(ABSOLUTE_PATH_TO_LISTS);
        File[] listOfFiles = folder.listFiles();
        Random r = new Random();
        File selectedFile = listOfFiles[r.nextInt(listOfFiles.length)];
        a.addAll(getWordFromList(selectedFile.getName()));
        return selectedFile.getName();
    }
}
