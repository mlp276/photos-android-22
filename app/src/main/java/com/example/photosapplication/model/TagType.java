package com.example.photosapplication.model;

import java.io.Serializable;

public final class TagType implements Serializable {
    static long serialVersionUID = 1L;

    public static final TagType PERSON = new TagType("person", true);
    public static final TagType LOCATION = new TagType("location", false);

    private final String name;
    private final boolean multiValued;

    private TagType(String name, boolean multiValued) {
        this.name = name;
        this.multiValued = multiValued;
    }

    public String getName() {
        return name;
    }

    public boolean isMultiValued() {
        return multiValued;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagType tagType = (TagType) o;
        return name.equalsIgnoreCase(tagType.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
