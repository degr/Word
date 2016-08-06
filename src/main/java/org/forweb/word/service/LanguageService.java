package org.forweb.word.service;

import org.forweb.database.AbstractService;
import org.forweb.word.dao.LanguageDao;
import org.forweb.word.entity.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService extends AbstractService<Language, LanguageDao> {

    @Autowired
    private WordService wordService;

    public void delete(Iterable<Language> list) {
        for (Language language : list) {
            wordService.deleteByLanguageId(language.getId());
        }
        this.dao.delete(list);
        updatePrimaryLanguage();
    }

    public void delete(Language language) {
        wordService.deleteByLanguageId(language.getId());
        this.dao.delete(language);
        updatePrimaryLanguage();
    }

    public void delete(Integer id) {
        wordService.deleteByLanguageId(id);
        this.dao.delete(id);
        updatePrimaryLanguage();
    }


    public Iterable<Language> save(Iterable<Language> list) {
        List<Language> out = new ArrayList<>();
        for (Language language : list) {
            out.add(save(language));
        }
        return out;
    }

    public Language save(Language language) {
        List<Language> languages = dao.findAll();
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
                        dao.save(item);
                    }
                }
            }
        }
        return dao.save(language);
    }

    public Language getLanguage(String shortName) {
        Language out = dao.findByShortName(shortName);
        if (out == null) {
            out = dao.findByTitle(shortName);
            if (out == null) {
                return dao.findByNativeTitle(shortName);
            } else {
                return out;
            }
        } else {
            return out;
        }
    }

    private void updatePrimaryLanguage() {
        if (dao.findByPrimary(true) == null) {
            for (Language language : dao.findAll()) {
                language.setPrimary(true);
                dao.save(language);
                break;
            }
        }
    }

    public Language findByPrimary(boolean primary) {
        return dao.findByPrimary(primary);
    }
}
