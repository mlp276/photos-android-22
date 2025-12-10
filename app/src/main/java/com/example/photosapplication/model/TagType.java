package com.example.photosapplication.model;

import java.io.Serializable;

/**
 * Represents a tag type (e.g., "location" or "person").
 * The allowed tag types are predefined as static constants.
 *
 * Each tag type has a name and a flag indicating whether multiple values
 * are allowed for the same photo (multi-valued) or just one (single-valued).
 */
public final class TagType implements Serializable {
    static long serialVersionUID = 1L;

    public static final TagType PERSON = new TagType("person", true);
    public static final TagType LOCATION = new TagType("location", false);

    private final String name;
    private final boolean multiValued;

    /**
     * Private constructor for predefined tag types.
     *
     * @param name the name of the tag type
     * @param multiValued whether the tag type will have multiple values associated
     */
    private TagType(String name, boolean multiValued) {
        this.name = name;
        this.multiValued = multiValued;
    }

    /**
     * Gets the name of the tag type.
     *
     * @return the name of the tag type
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if this tag type supports multiple values for a single photo.
     *
     * @return true if multi-valued, false otherwise
     */
    public boolean isMultiValued() {
        return multiValued;
    }

    /**
     * Compares this TagType to another object for equality.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagType tagType = (TagType) o;
        return name.equalsIgnoreCase(tagType.name);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    /**
     * Returns the string representation of the TagType, which is its name.
     *
     * @return the name of the tag type
     */
    @Override
    public String toString() {
        return name;
    }
}
