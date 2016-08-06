package org.forweb.word.dao;

import org.forweb.word.entity.Word;
import org.forweb.database.AbstractDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WordDao extends AbstractDao<Word> {

    @Query("select word from Word word where word.language = :languageId and word.module = :moduleId")
    List<Word> get(@Param("languageId") Integer languageId, @Param("moduleId")Integer moduleId);

    @Query("select word from Word word where word.module = :moduleId and word.title = :title")
    List<Word> findAll(@Param("moduleId")Integer module, @Param("title")String title);

    @Query("select word from Word word where word.module = :moduleId")
    Word findAll(@Param("moduleId")Integer moduleId);

    @Query("select word from Word word where word.language = :languageId")
    Word findAllByLanguage(@Param("languageId") Integer language);


    @Transactional
    @Modifying
    @Query("delete from Word word where word.language = :languageId")
    void deleteByLanguageId(@Param("languageId") Integer languageId);

    @Transactional
    @Modifying
    @Query("delete from Word word where word.module = :moduleId")
    void deleteByModuleId(@Param("moduleId") Integer moduleId);

    Word findByLanguageAndModuleAndTitle(Integer language, Integer module, String title);
}
