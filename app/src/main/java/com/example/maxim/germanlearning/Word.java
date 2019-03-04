package com.example.maxim.germanlearning;

enum WordType {verb, other, noun}

public class Word {
    WordType WordType;
    String EnglishValue;
    String GermanValue;
    String AdditionnalGermanValue;


    public Word(WordType wt, String englishValue, String germanValue) {
        WordType = wt;
        EnglishValue = englishValue;
        GermanValue = germanValue;
    }
}
