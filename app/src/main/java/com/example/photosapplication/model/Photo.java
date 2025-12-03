package com.example.photosapplication.model;

import android.net.Uri;
import java.io.Serializable;
import java.util.List;
import com.example.photosapplication.model.util.UniqueList;

/**
 * Photo - the photo of the application
 * This class provides functionality adding and removing tags to the photo.
 */
public class Photo implements Serializable {
    private final Uri uri;
    private final List<Tag> tags;
    private final transient List<TagType> tagTypes;

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
        this.uri = uri;
        this.tags = new UniqueList<Tag>();
        this.tagTypes = new UniqueList<TagType>();
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
        return uri.equals(other.getUri());
    }

    /**
     * Gets the uri of the photo
     * 
     * @return the uri of the photo
     */
    Uri getUri() {
        return uri;
    }

    /**
     * Add a tag to this photo. If the tag's type is single-valued, any existing
     * tag of the same type will be replaced. If the tag type is not registered,
     * it will be auto-registered as multi-valued.
     *
     * @param typeName tag type name (e.g., "location" or "person")
     * @param value tag value (e.g., "New Brunswick")
     * @return true if the photo was changed (tag added or replaced)
     * @throws Exception the tag type name and value are null
     */
    public boolean addTag(String typeName, String value) throws Exception {
        if (typeName == null || value == null) {
            throw new IllegalArgumentException("Tag type and value are not provided.");
        }

        /* Determines if the tag with the type can be added for the user */
        TagType type = getTagType(typeName);
        if (type == null) {
            throw new Exception("The tag type is not defined.");
        }

        /* For single-valued types, remove any existing tag of the same type;
         * otherwise, for multi-valued types, just add the tag to the photo
         */
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
     * Initializes the tag types of the user
     */
    private void initializeTagTypes() {
        addTagType("location", false);
        addTagType("person", true);
    }

    /**
     * Add a tag type of the user
     *
     * @param tagTypeName the name of the tag type
     * @param multiValued whether the tag type will have multiple values associated with it
     */
    private void addTagType(String tagTypeName, boolean multiValued) {
        tagTypes.add(new TagType(tagTypeName, multiValued));
    }

    /**
     * Gets the tag type
     *
     * @param tagTypeName the tag type name
     * @return the tag type with tag type name
     */
    private TagType getTagType(String tagTypeName) {
        return tagTypes.stream().filter(tagType -> tagType.getName().equals(tagTypeName)).findFirst().orElse(null);
    }
}