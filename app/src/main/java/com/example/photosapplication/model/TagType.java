package com.example.photosapplication.model;

import java.io.Serializable;

/**
 * Represents a tag type (e.g., "location" or "person").
 * Tag types are registered in a global registry so users can pick from presets
 * and define new types at runtime.
 * Each tag type has a name and a flag indicating whether multiple values
 * are allowed for the same photo (multi-valued) or just one (single-valued).
 */
public class TagType implements Serializable {
    private final String name;
    private final boolean multiValued;

    /**
     * Initializes the tag type
     * 
     * @param name the name of the tag type
     * @param multiValued whether the tag type will have multiple values associated
     */
    public TagType(String name, boolean multiValued) {
        this.name = name;
        this.multiValued = multiValued;
    }

    /**
     * Gets the name of the tag type
     * 
     * @return the name of the tag type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the indication of whether the tag will be multivalued
     * 
     * @return whether the tag will be multivalued
     */
    public boolean isMultiValued() {
        return multiValued;
    }

    /**
     * Compares the two tag objects by their name
     * 
     * @return a boolean value indicating comparison of the two objects
     */
    public boolean equals(Object o) {
        if (!(o instanceof TagType)) {
            return false;
        }
        TagType other = (TagType) o;
        return this.name.equalsIgnoreCase(other.getName());
    }
}
