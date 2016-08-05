package org.forweb.word.dao;

import org.forweb.word.entity.Language;
import org.forweb.database.AbstractDao;
import org.springframework.stereotype.Repository;


@Repository
public interface LanguageDao extends AbstractDao<Language> {
    Language findByShortName(String shortName);
    Language findByTitle(String title);
    Language findByNativeTitle( String nativeTitle);

    Language findByPrimary(Boolean primary);

}
