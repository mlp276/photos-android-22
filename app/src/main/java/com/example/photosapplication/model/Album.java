package com.example.photosapplication.model;

import com.example.photosapplication.util.UniqueList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public Album(String name, List<Photo> photos) throws IllegalArgumentException {
        this.setName(name);
        this.photos = photos;
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

    public List<Photo> getPhotos() {
        return photos;
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

    public Photo getPhoto(int position) {
        return photos.get(position);
    }

    public Photo getNextPhoto(int position) {
        if (position + 1 < getPhotoCount()) {
            return getPhoto(position + 1);
        }
        return null;
    }

    public Photo getPreviousPhoto(int position) {
        if (position - 1 >= 0) {
            return getPhoto(position - 1);
        }
        return null;
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
     * Searches for photos in the album by the presence of tags in the photo
     *
     * @param tagPredicate the predicate of checking the tags in the photo
     * @return a list of photos that contain the tags
     */
    public List<Photo> searchPhotosInAlbum(Predicate<Tag> tagPredicate) {
        return photos.stream()
                .filter(photo -> photo.hasTag(tagPredicate))
                .collect(Collectors.toList());
    }
}
