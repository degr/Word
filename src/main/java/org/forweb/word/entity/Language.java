package org.forweb.word.entity;


import org.forweb.database.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Language extends AbstractEntity {

    private String title;
    private String shortName;
    private String nativeTitle;

    private Boolean primary;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNativeTitle() {
        return nativeTitle;
    }

    public void setNativeTitle(String nativeTitle) {
        this.nativeTitle = nativeTitle;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }


}
