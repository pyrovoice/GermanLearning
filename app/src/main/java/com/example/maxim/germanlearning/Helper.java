package com.example.maxim.germanlearning;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.nio.charset.StandardCharsets;

public class Helper {
    private static final String ABSOLUTE_PATH_TO_FILE = "/mnt/sdcard/GermanLearning/existingWords.txt";
    private static final String ABSOLUTE_PATH_TO_FOLDER = "/mnt/sdcard/GermanLearning";
    private static ArrayList<Word> verbs = new ArrayList<>();
    private static ArrayList<Word> nouns = new ArrayList<>();
    private static ArrayList<Word> other = new ArrayList<>();
    private static HashMap<Word, Integer> ExistingWords = new HashMap<>();
    private static final String SEPARATOR = ";";
    private static final String PARTS_SEPARATOR = "-";


    public static ArrayList<Word> getWordList(Context context, int nbr) throws IOException {
        if (verbs.size() <= 0) {
            loadWords(context);
        }
        Random r = new Random();
        ArrayList<Word> rWords = new ArrayList<>();
        for (int i = 0; i < (nbr / 3 + nbr % 3); i++) {
            rWords.add(nouns.get(r.nextInt(nouns.size())));
        }
        for (int i = 0; i < (nbr / 3); i++) {
            rWords.add(verbs.get(r.nextInt(verbs.size())));
        }
        for (int i = 0; i < (nbr / 3); i++) {
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

            Word w = new Word(WordType.noun, "(n) " + parts[0], germanValue[0]);
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
            Word w = new Word(WordType.verb, "(v) " + eng.split(";")[0], german);
            verbs.add(w);

        }
        r = context.getResources().openRawResource(R.raw.other);
        reader = new BufferedReader(new InputStreamReader(r));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("-");
            String german = parts[0];
            String eng = parts[1];
            Word w = new Word(WordType.other, eng.split(";")[0], german);
            other.add(w);
        }
        //Load file of existing word
        File f = new File(ABSOLUTE_PATH_TO_FILE);
        if (f.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String lineRead = null;
            while ((lineRead = br.readLine()) != null) {
                //For each line, create and add the word to existing
                ExistingWords.putAll(StringToWordMap(lineRead));
            }
        }
    }

    /**
     * Parse the line and create the Word, then return an arraylist containing the specified number of occurences for this word.
     *
     * @param s Format: WordType-EnglishValue;GermanValue-NumberOccurence
     * @return ArrayList<Word>
     */
    private static Map<Word, Integer> StringToWordMap(String s) {

        String[] parts = s.split(PARTS_SEPARATOR);
        String[] wordDetails = parts[1].split(SEPARATOR);
        Word w = new Word(WordType.valueOf(parts[0]), wordDetails[0], wordDetails[1]);
        HashMap<Word, Integer> returnValue = new HashMap<>();
        returnValue.put(w, Integer.parseInt(parts[2]));
        return returnValue;
    }

    /**
     * Create a string according to the Word and occurence
     *
     * @param w            The word
     * @param nbrOccurence Number of time this word exists in the list
     * @return String Format: WordType-EnglishValue;GermanValue-NumberOccurence
     */
    private static String WordToString(Word w, int nbrOccurence) {
        return w.WordType + PARTS_SEPARATOR + w.EnglishValue + SEPARATOR + w.GermanValue + PARTS_SEPARATOR + nbrOccurence;

    }

    private static void writeToFile(String path, String toWrite) {
        //Create files if don't exists
        File f = new File(ABSOLUTE_PATH_TO_FOLDER);
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(ABSOLUTE_PATH_TO_FILE);
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

    public static void upsertWordListFromWords(Word word, int nbrOccurence) {
        // update the word list
        ExistingWords.put(word, nbrOccurence);
        // Update the file
        StringBuilder sb = new StringBuilder();
        for(Word w : ExistingWords.keySet()){
            sb.append(WordToString(word, nbrOccurence) + "\n");
        }
        writeToFile(ABSOLUTE_PATH_TO_FILE, sb.toString());
    }


    public static ArrayList<Word> getExistingWordList() throws Exception {
        ArrayList<Word> returnList = new ArrayList<>();
        for(Word w : ExistingWords.keySet()){
            for(int i = 0; i < ExistingWords.get(w); i++){
                returnList.add(w);
            }
        }
        return returnList;
    }
}
