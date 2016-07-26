package org.forweb.word.service;

import org.forweb.word.dao.LanguageDao;
import org.forweb.word.dao.ModuleDao;
import org.forweb.word.dao.WordDao;
import org.forweb.word.entity.Language;
import org.forweb.word.entity.Module;
import org.forweb.word.entity.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WordService {

    public static Boolean GET_KEYS = false;

    @Autowired
    private WordDao wordDao;
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private LanguageDao languageDao;
    /**
     * Language -> module -> title
     */
    private Map<Integer, Map<Integer, Map<String, String>>> map;
    private List<Language> languages;
    private List<Module> modules;


    public Language getPrimaryLanguage() {
        for (Language language : _getLanguages()) {
            if (language.getPrimary()) {
                return language;
            }
        }
        return null;
    }

    public String get(Language language, Module module, String title) {
        if (language == null) {
            language = getPrimaryLanguage();
            if (language == null) {
                return "";
            }
        }
        if (module == null) {
            return "";
        }
        if (title == null) {
            return "";
        }
        if (GET_KEYS) {
            return language.getShortName() + "_" + module.getTitle() + "_" + title;
        } else {
            return getMap().get(language.getId()).get(module.getId()).get(title);
        }
    }

    public String get(Language language, Integer moduleId, String title) {
        Module module = getModule(moduleId);
        return get(language, module, title);
    }

    public String get(Language language, String moduleTitle, String title) {
        Module module = getModule(moduleTitle);
        return get(language, module, title);
    }


    public String get(Integer languageId, Integer moduleId, String title) {
        Language language = getLanguage(languageId);
        return get(language, moduleId, title);
    }

    public String get(Integer languageId, String moduleTitle, String title) {
        Language language = getLanguage(languageId);
        return get(language, moduleTitle, title);
    }

    public String get(Integer languageId, Module module, String title) {
        Language language = getLanguage(languageId);
        return get(language, module, title);
    }


    public String get(String languageTitle, String moduleTitle, String title) {
        Language language = getLanguage(languageTitle);
        return get(language, moduleTitle, title);
    }

    public String get(String languageTitle, Integer moduleId, String title) {
        Language language = getLanguage(languageTitle);
        return get(language, moduleId, title);
    }

    public String get(String languageTitle, Module module, String title) {
        Language language = getLanguage(languageTitle);
        return get(language, module, title);
    }


    public List<Word> save(List<Word> words) {
        if (words == null || words.size() == 0) {
            throw new RuntimeException("Invalid arguments - words must be array in count equal to languages count");
        }
        Map<Integer, Map<Integer, Map<String, String>>> map = getMap();
        List<Integer> validLanguages = new ArrayList<>(map.size());
        for (Word word : words) {
            if (word.getLanguage() == null) {
                throw new RuntimeException("Language can't be null");
            }
            if (map.get(word.getLanguage()) == null) {
                throw new RuntimeException("Language with id '" + word.getLanguage() + "' does not exist");
            }
            if (validLanguages.contains(word.getLanguage())) {
                throw new RuntimeException("Language is duplicated");
            } else {
                validLanguages.add(word.getLanguage());
            }

            if (word.getModule() == null) {
                throw new RuntimeException("Module can't be null");
            }
            Module module = moduleDao.findOne(word.getModule());
            if (module == null) {
                throw new RuntimeException("Module with id '" + word.getModule() + "' does not exist");
            }
        }

        return wordDao.save(words);
    }

    public Module save(Module module) {
        if (module.getTitle() == null) {
            throw new RuntimeException("Module title must not be null");
        }
        Module item = moduleDao.findByTitle(module.getTitle());
        if (item != null && !item.getId().equals(module.getId())) {
            throw new RuntimeException("Module with same title not found");
        }
        Module out = moduleDao.save(module);
        for (Map<Integer, Map<String, String>> language : this.getMap().values()) {
            language.put(out.getId(), new HashMap<>());
        }
        return out;
    }

    public Language save(Language language) {
        List<Language> languages = languageDao.findAll();
        for (Language item : languages) {
            if (item.getShortName().equals(language.getShortName()) && !item.getId().equals(language.getId())) {
                throw new RuntimeException("Language shortname must be unique");
            }
        }
        if (languages.size() == 0) {
            language.setPrimary(true);
        } else {
            if (Boolean.TRUE.equals(language.getPrimary())) {
                for (Language item : languages) {
                    if (Boolean.TRUE.equals(item.getPrimary())) {
                        item.setPrimary(false);
                        languageDao.save(item);
                    }
                }
            }
        }
        Language out = languageDao.save(language);
        this.getMap().put(out.getId(), new HashMap<>());
        return out;
    }

    public void delete(Language language) {
        wordDao.delete(wordDao.findAllByLanguage(language.getId()));
        this.getMap().remove(language.getId());
    }

    public void delete(Module module) {
        wordDao.delete(wordDao.findAll(module.getId()));
        for (Map<Integer, Map<String, String>> language : this.getMap().values()) {
            language.remove(module.getId());
        }
    }

    public void delete(Word word) {
        wordDao.delete(
                wordDao.findAll(word.getModule(), word.getTitle())
        );
        Module module = moduleDao.findOne(word.getModule());
        for (Map<Integer, Map<String, String>> language : this.getMap().values()) {
            Map<String, String> moduleMap = language.get(module.getId());
            moduleMap.remove(word.getTitle());
        }
    }

    public Language getLanguage(String shortName) {
        for (Language language : _getLanguages()) {
            if (language.getShortName().equals(shortName)) {
                return language;
            }
        }
        return null;
    }

    public List<Language> getLanguages() {
        return new ArrayList<>(_getLanguages());
    }

    public List<Module> getModules() {
        return new ArrayList<>(_getModules());
    }

    private Map<Integer, Map<Integer, Map<String, String>>> getMap() {
        if (map == null) {
            map = new HashMap<>();
            for (Word item : wordDao.findAll()) {
                Map<Integer, Map<String, String>> lang;
                if (!map.containsKey(item.getLanguage())) {
                    lang = new HashMap<>();
                    map.put(item.getLanguage(), lang);
                } else {
                    lang = map.get(item.getLanguage());
                }
                Map<String, String> module;
                if (!lang.containsKey(item.getModule())) {
                    module = new HashMap<>();
                    lang.put(item.getModule(), module);
                } else {
                    module = lang.get(item.getModule());
                }
                module.put(item.getTitle(), item.getValue());
            }
        }
        return map;
    }

    private List<Language> _getLanguages() {
        if (this.languages == null) {
            languages = languageDao.findAll();
        }
        return languages;
    }

    private Language getLanguage(Integer languageId) {
        for (Language language : _getLanguages()) {
            if (language.getId().equals(languageId)) {
                return language;
            }
        }
        return null;
    }

    private Module getModule(Integer moduleId) {
        for (Module module : _getModules()) {
            if (module.getId().equals(moduleId)) {
                return module;
            }
        }
        return null;
    }

    private List<Module> _getModules() {
        if (modules == null) {
            modules = moduleDao.findAll();
        }
        return modules;
    }

    private Module getModule(String moduleTitle) {
        for (Module module : _getModules()) {
            if (module.getTitle().equals(moduleTitle)) {
                return module;
            }
        }
        return null;
    }
}
