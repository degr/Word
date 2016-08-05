package org.forweb.word.view;

import org.forweb.word.entity.Language;
import org.forweb.word.entity.Module;
import org.forweb.word.entity.Word;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    private List<Language> languages;
    private List<Module> modules;
    private Map<Integer, Map<Integer, Map<String, String>>> words;

    public Dictionary(List<Language> languages, List<Module> modules, List<Word> words) {
        this.languages = languages;
        this.modules = modules;
        this.words = new HashMap<>();
        for(Word word : words) {
            if(!this.words.containsKey(word.getLanguage())) {
                this.words.put(word.getLanguage(), new HashMap<>());
            }
            Map<Integer, Map<String, String>> lang = new HashMap<>();
            if(!lang.containsKey(word.getModule())) {
                lang.put(word.getModule(), new HashMap<>());
            }
            lang.get(word.getModule()).put(word.getTitle(), word.getValue());
        }
    }


    public List<Language> getLanguages() {
        return languages;
    }

    public List<Module> getModules() {
        return modules;
    }

    public Map<Integer, Map<Integer, Map<String, String>>>  getWords() {
        return words;
    }

}
