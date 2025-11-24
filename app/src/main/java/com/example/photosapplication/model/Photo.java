package com.example.photosapplication.model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import com.example.photosapplication.model.util.UniqueList;

/**
 * Photo - the photo of the application
 * 
 * This class provides functionality adding and removing tags to the photo.
 */
public class Photo implements Serializable {
    static final long serialVersionUID = 1L;
    private static final String photoDateFormat = "MM/dd/yyyy";
    public static final SimpleDateFormat photoDateFormatter = new SimpleDateFormat(photoDateFormat);

    private User user;
    private File file;
    private String caption;
    private Calendar dateTaken;
    private List<Tag> tags;

    /**
     * Initializes the Photo object given the user and file
     * 
     * @param user the user of the photo
     * @param file the file of the photo referenced
     * @throws IllegalArgumentException the file is null or does not exist
     */
    public Photo(User user, File file) throws IllegalArgumentException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file);
        }
        this.user = user;
        this.file = file;
        this.dateTaken = calculateDateTaken(file);
        this.tags = new UniqueList<Tag>();
    }

    /**
     * Compares the two Photo objects by their file paths
     * 
     * @return a boolean value comparing the two objects
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Photo)) {
            return false;
        }
        Photo other = (Photo) o;
        return file.equals(other.getFile());
    }

    /**
     * Calculates the date taken provided by the file
     * 
     * @param file the file to calculate the date taken
     * @return the date taken as a Calendar object
     */
    private Calendar calculateDateTaken(File file) {
        return getLastModified(file);
    }

    /**
     * Gets the last modified date of the file
     * 
     * @param file the file to get the date
     * @return the last modified date of the file as a Calendar object
     */
    private Calendar getLastModified(File file) {
        Calendar lastModified = new GregorianCalendar();
        lastModified.setTimeInMillis(file.lastModified());
        /* Sets the millisecond component of the Calendar object to 0 */
        lastModified.set(Calendar.MILLISECOND, 0);
        return lastModified;
    }
    
    /**
     * Gets the caption of the photo
     * 
     * @return the caption of the photo
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption of the photo
     * 
     * @param caption the caption of the photo to set
     * @throws IllegalArgumentException the caption is null or is blank
     */
    public void setCaption(String caption) throws IllegalArgumentException {
        if (caption == null || caption.isBlank()) {
            throw new IllegalArgumentException("The caption is invalid.");
        }
        this.caption = caption;
    }

    /**
     * Gets the file path of the photo
     * 
     * @return the file object of the photo
     */
    File getFile() {
        return file;
    }

    /**
     * Gets the date taken of the photo
     * 
     * @return the date taken of the photo
     */
    public Calendar getDateTaken() {
        return dateTaken;
    }

    /**
     * Provides the file path of the photo as a String
     * 
     * @return the file path of the photo as a string
     */
    public String getFileString() {
        return file.toURI().toString();
    }

    /**
     * Add a tag to this photo. If the tag's type is single-valued, any existing
     * tag of the same type will be replaced. If the tag type is not registered,
     * it will be auto-registered as multi-valued.
     *
     * @param typeName tag type name (e.g., "location" or "person")
     * @param value tag value (e.g., "New Brunswick")
     * @return true if the photo was changed (tag added or replaced)
     * @throws Exception 
     */
    public boolean addTag(String typeName, String value) throws Exception {
        if (typeName == null || value == null) {
            throw new IllegalArgumentException("Tag type and value are not provided.");
        }

        /* Determines if the tag with the type can be added for the user */
        TagType type = user.getTagType(typeName);
        if (type == null) {
            throw new Exception("The tag type is not defined."); 
        }

        /* For single-valued types, remove any existing tag of the same type;
         * otherwise, for multi-valued types, just add the tag to the photo
         */
        if (!type.isMultiValued()) {
            for (java.util.Iterator<Tag> tagsIterator = tags.iterator(); tagsIterator.hasNext(); ) {
                Tag tag = tagsIterator.next();
                if (tag.getType().getName().equalsIgnoreCase(type.getName())) {
                    tagsIterator.remove();
                }
            }
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
        TagType type = user.getTagType(tagTypeName);
        if (type == null) {
            return false;
        }
        return tags.remove(new Tag(type, value));
    }

    /**
     * Return an unmodifiable view of this photo's tags.
     * 
     * @return a list of tags of the photo
     */
    public List<Tag> getTags() {
        return tags;
    }
}