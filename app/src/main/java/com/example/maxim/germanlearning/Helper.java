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
    private static final String ABSOLUTE_PATH_TO_LISTS = "/mnt/sdcard/GermanLearning/";
    private static final String SEPARATOR = ";";

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
            Word w = new Word(WordType.verb, "To " + eng.split(";")[0], german, null);
            allWords.add(w);

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
