package org.forweb.word.dao;

import org.forweb.word.entity.Language;
import org.forweb.word.entity.Word;
import org.forweb.database.AbstractDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordDao extends AbstractDao<Word> {

    @Query("select word from Word word where word.language = :languageId and word.module = :moduleId")
    List<Word> get(@Param("languageId") Integer languageId, @Param("moduleId")Integer moduleId);

    @Query("select word from Word word where word.module = :moduleId and word.title = :title")
    List<Word> findAll(@Param("moduleId")Integer module, @Param("title")String title);

    @Query("select word from Word word where word.module = :moduleId")
    Word findAll(@Param("moduleId")Integer moduleId);

    @Query("select word from Word word where word.language = :languageId")
    Word findAllByLanguage(@Param("languageId") Integer language);
}
