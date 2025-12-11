package com.example.photosapplication.model;

import android.net.Uri;

import com.example.photosapplication.util.UniqueList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Photo - the photo of the application
 * This class provides functionality adding and removing tags to the photo.
 */
public class Photo implements Serializable {
    private String uriString;
    private final List<Tag> tags;

    /**
     * Initializes the Photo object given the uri
     *
     * @param uri the uri of the photo referenced
     * @throws IllegalArgumentException the uri is null
     */
    public Photo(Uri uri) throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException("Uri does not exist");
        }
        setUri(uri);
        this.tags = new UniqueList<Tag>();
    }

    /**
     * Compares the two Photo objects by their uri paths
     *
     * @return a boolean value comparing the two objects
     */
    public boolean equals(Object o) {
        if (!(o instanceof Photo)) {
            return false;
        }
        Photo other = (Photo) o;
        return getUri().equals(other.getUri());
    }

    public void setUri(Uri uri) {
        this.uriString = uri.toString();
    }

    public Uri getUri() {
        return Uri.parse(uriString);
    }

    /**
     * Add a tag to this photo. If the tag's type is single-valued, any existing
     * tag of the same type will be replaced.
     *
     * @param typeName tag type name (e.g., "location" or "person")
     * @param value    tag value (e.g., "New Brunswick")
     * @return true if the photo was changed (tag added or replaced)
     * @throws Exception the tag type name and value are null or the type is not defined
     */
    public boolean addTag(String typeName, String value) throws Exception {
        if (typeName == null || value == null) {
            throw new IllegalArgumentException("Tag type and value are not provided.");
        }

        TagType type = getTagType(typeName);
        if (type == null) {
            throw new Exception("The tag type is not defined.");
        }

        if (!type.isMultiValued()) {
            tags.removeIf(tag -> tag.getType().getName().equalsIgnoreCase(type.getName()));
        }

        Tag tag = new Tag(type, value);
        return tags.add(tag);
    }

    /**
     * Remove a specific (type, value) tag from the photo.
     *
     * @return true if removed
     */
    public boolean removeTag(String tagTypeName, String value) {
        TagType type = getTagType(tagTypeName);
        if (type == null) {
            return false;
        }
        return tags.remove(new Tag(type, value));
    }

    /**
     * Gets the tag type from the predefined set of tag types.
     *
     * @param tagTypeName the tag type name
     * @return the tag type with tag type name, or null if not found
     */
    private TagType getTagType(String tagTypeName) {
        if (TagType.PERSON.getName().equalsIgnoreCase(tagTypeName)) {
            return TagType.PERSON;
        }
        if (TagType.LOCATION.getName().equalsIgnoreCase(tagTypeName)) {
            return TagType.LOCATION;
        }
        return null;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean hasTag(Predicate<Tag> tagFilter) {
        return tags.stream()
                .anyMatch(tagFilter);
    }
}
