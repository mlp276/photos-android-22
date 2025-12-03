package com.example.photosapplication.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import com.example.photosapplication.model.util.UniqueList;

/**
 * Album - the album to contain a set of photos for the user.
 * This album provides business logic for adding and removing photos,
 * retrieving the earliest and latest photos that were taken, and searching
 * for photos based on the date range and the tags of the photos
 */
public class Album implements Serializable {
    private String name;
    private final List<Photo> photos;
    
    /**
     * Instantiates the Album object
     * 
     * @param name the name of the album
     * @throws IllegalArgumentException the name is null or blank
     */
    public Album(String name) throws IllegalArgumentException {
        this.setName(name);
        this.photos = new UniqueList<Photo>();
    }

    /**
     * Gets the name of the album
     * 
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album
     * 
     * @param name the name of the album to set
     * @throws IllegalArgumentException the input album name is null or blank
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The album name is invalid.");
        }
        this.name = name;
    }

    /**
     * Compares the two Album objects by their name
     * 
     * @return a boolean value comparing the two objects
     */
    public boolean equals(Object o) {
        if (!(o instanceof Album)) {
            return false;
        }
        Album other = (Album) o;
        return name.equals(other.getName());
    }

    /**
     * Adds a photo to the album
     * 
     * @param photo the photo to add
     * @return a boolean value indicating a successful addition of the photo
     */
    public boolean addPhoto(Photo photo) {
        return photos.add(photo);
    }

    /**
     * Removes a photo from the album
     * 
     * @param photo the photo to remove
     * @return a boolean value indicating a successful removal of the photo
     */
    public boolean removePhoto(Photo photo) {
        return photos.remove(photo);
    }

    /**
     * Checks if the album has photos
     * 
     * @return a boolean value that indicates an existence of a photo in the album
     */
    public boolean hasPhotos() {
        return !photos.isEmpty();
    }

    /**
     * Provides the number of photos in the album
     * 
     * @return the number of photos in the album
     */
    public int getPhotoCount() {
        return photos.size();
    }

    /**
     * Searches for photos in the album by the date range of the date taken
     * 
     * @param startDate the earliest date in the range to search photos
     * @param endDate the latest date in the range to search photos
     * @return a list of photos that lie within the range of date taken
     */
    public List<Photo> searchPhotosByDate(Calendar startDate, Calendar endDate) {
        return null;
    }

    /**
     * Searches for photos in the album by the presence of tags in the photo
     * 
     * @param tagPredicate the predicate of checking the tags in the photo
     * @return a list of photos that contain the tags
     */
    public List<Photo> searchPhotosByTag(Predicate<Tag> tagPredicate) {
        return null;
    }
}
