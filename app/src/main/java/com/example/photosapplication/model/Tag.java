package com.example.photosapplication.model;

import java.io.Serializable;

/**
 * Represents a tag: an immutable (type, value) pair.
 * 
 * Example tags: ("location", "New Brunswick"), ("person", "susan")
 * 
 * Equality is based on type and case-insensitive value, so no two tags
 * with the same name and value can exist in a Set.
 */
public class Tag implements Serializable {
    static long serialVersionUID = 1L;
    
    private TagType type;
    private String value;

    /**
     * Create a tag with the given type and value.
     * 
     * @param type the TagType (e.g., from TagType.get() or TagType.registerType())
     * @param value the tag value (e.g., "susan")
     * @throws IllegalArgumentException if type or value is null
     */
    public Tag(TagType type, String value) {
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (value == null) throw new IllegalArgumentException("value cannot be null");
        this.type = type;
        this.value = value.trim();
    }

    /**
     * Gets the type of the tag
     * 
     * @return the type of the tag
     */
    public TagType getType() {
        return type;
    }

    /**
     * Gets the value of the tag
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Compares the two Tag objects by their type and value
     * 
     * @return a boolean value comparing the two objects
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Tag)){
            return false;
        }
        Tag that = (Tag) o;
        return this.type.equals(that.type) && this.value.equalsIgnoreCase(that.value);
    }

    /**
     * Hashes the tag object to a code
     * 
     * @returns the hash code of the object
     */
    public int hashCode() {
        return type.hashCode() * 31 + value.toLowerCase().hashCode();
    }

    /**
     * Returns the string representation of the object
     * 
     * @return the string of the object
     */
    public String toString() {
        return type.getName() + ": " + value;
    }
}
