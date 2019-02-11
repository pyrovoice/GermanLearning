package com.example.maxim.germanlearning;

enum WordType {verb, noun}

public class Word {
    WordType wt;
    String EnglishValue;
    String GermanValue;
    String AdditionnalGermanValue;

    public Word(WordType wt, String englishValue, String germanValue, String additionnalGermanValue) {
        this.wt = wt;
        EnglishValue = englishValue;
        GermanValue = germanValue;
        AdditionnalGermanValue = additionnalGermanValue;
    }
}
