package org.forweb.word.entity;


import org.forweb.database.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Word extends AbstractEntity{
    private String title;
    private String value;
    private Integer module;
    private Integer language;


    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
