package org.forweb.word.service;

import org.forweb.database.AbstractService;
import org.forweb.word.dao.WordDao;
import org.forweb.word.entity.Language;
import org.forweb.word.entity.Module;
import org.forweb.word.entity.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class WordService extends AbstractService<Word, WordDao> {
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private WordJsService wordJsService;


    public String get(Language language, Module module, String title) {
        if (language == null) {
            language = languageService.findByPrimary(true);
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
        Word out = dao.findByLanguageAndModuleAndTitle(language.getId(), module.getId(), title);
        return out.getValue();
    }

    public String get(Language language, Integer moduleId, String title) {
        Module module = moduleService.findOne(moduleId);
        return get(language, module, title);
    }

    public String get(Language language, String moduleTitle, String title) {
        Module module = moduleService.findByTitle(moduleTitle);
        return get(language, module, title);
    }


    public String get(Integer languageId, Integer moduleId, String title) {
        Language language = languageService.findOne(languageId);
        return get(language, moduleId, title);
    }

    public String get(Integer languageId, String moduleTitle, String title) {
        Language language = languageService.findOne(languageId);
        return get(language, moduleTitle, title);
    }

    public String get(Integer languageId, Module module, String title) {
        Language language = languageService.findOne(languageId);
        return get(language, module, title);
    }


    public String get(String languageTitle, String moduleTitle, String title) {
        Language language = languageService.getLanguage(languageTitle);
        return get(language, moduleTitle, title);
    }

    public String get(String languageTitle, Integer moduleId, String title) {
        Language language = languageService.getLanguage(languageTitle);
        return get(language, moduleId, title);
    }

    public String get(String languageTitle, Module module, String title) {
        Language language = languageService.getLanguage(languageTitle);
        return get(language, module, title);
    }


    public List<Word> save(List<Word> words) {
        if (words == null) {
            throw new RuntimeException("words can't be null");
        }
        Map<Integer, Language> languageMap = languageService.findAll().stream().collect(Collectors.toMap(Language::getId, v -> v));
        if (languageMap.size() == 0) {
            throw new RuntimeException("There is no languages");
        }
        if (languageMap.size() != words.size()) {
            throw new RuntimeException("Invalid arguments - words must be array in count equal to languages count");
        }
        List<Integer> validLanguages = new ArrayList<>(languageMap.size());
        String title = null;
        Module module = null;
        for (Word word : words) {
            if (word.getLanguage() == null) {
                throw new RuntimeException("Language can't be null");
            }
            if (languageMap.get(word.getLanguage()) == null) {
                throw new RuntimeException("Language with id '" + word.getLanguage() + "' does not exist");
            }
            if (validLanguages.contains(word.getLanguage())) {
                throw new RuntimeException("Language is duplicated. List of different languages must be passed.");
            } else {
                validLanguages.add(word.getLanguage());
            }
            if (title == null) {
                title = word.getTitle();
            } else if (!title.equals(word.getTitle())) {
                throw new RuntimeException("Titles must be same");
            }

            if (word.getModule() == null) {
                throw new RuntimeException("Module can't be null");
            }
            if (module == null) {
                module = moduleService.findOne(word.getModule());
            } else if (!module.getId().equals(word.getModule())) {
                throw new RuntimeException("Module with id '" + word.getModule() + "' does not exist");
            }
        }
        List<Word> out= dao.save(words);
        wordJsService.dump();
        return out;
    }

    public Word save(Word word) {
        List<Language> languages = languageService.findAll();
        switch (languages.size()) {
            case 0:
                throw new RuntimeException("There is no languages");
            case 1:
                Word out = dao.save(word);
                wordJsService.dump();
                return out;
            default:
                throw new RuntimeException("Can't use this method, language count is not equal to 1. Use save(List<Word>) method");

        }
    }

    public void delete(Word word) {
        dao.delete(dao.findAll(word.getModule(), word.getTitle()));
        wordJsService.dump();
    }

    public void deleteByLanguageId(Integer languageId) {
        dao.deleteByLanguageId(languageId);
        wordJsService.dump();
    }

    public void deleteByModuleId(Integer moduleId) {
        dao.deleteByModuleId(moduleId);
        wordJsService.dump();
    }
}
