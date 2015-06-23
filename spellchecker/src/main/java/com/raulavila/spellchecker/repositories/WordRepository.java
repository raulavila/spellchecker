package com.raulavila.spellchecker.repositories;

public interface WordRepository {

    boolean addWord(String language, String word);

    boolean removeWord(String language, String word);

    boolean contains(String language, String word);

}
