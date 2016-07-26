package org.forweb.word.dao;

import org.forweb.word.entity.Module;
import org.forweb.database.AbstractDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleDao extends AbstractDao<Module> {
    @Query("select module from Module module where module.title = :title ")
    Module findByTitle(@Param("title")String title);
}
