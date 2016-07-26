package org.forweb.word.entity;

import org.forweb.database.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Module extends AbstractEntity {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
