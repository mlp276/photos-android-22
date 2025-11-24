package com.example.photosapplication.model;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;
import com.example.photosapplication.model.util.UniqueList;

/**
 * User - the user of the application
 * 
 * This model implements adding and removing albums, adding and removing tag types,
 * and copying and moving photos from one album to another
 */
public class User implements Serializable {
    static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private List<Album> albums;
    private List<TagType> tagTypes;

    /**
     * Initializes the User object
     * 
     * @param username the username of the user
     * @param password the password of the user
     * @throws IllegalArgumentException the username or password are null or blank
     */
    public User(String username, String password) throws IllegalArgumentException {
        this.setUser(username, password);
        this.albums = new UniqueList<Album>();
        this.tagTypes = new UniqueList<TagType>();
        initializeTagTypes();
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
     * @return true if the tag type is successfully added
     */
    public boolean addTagType(String tagTypeName, boolean multiValued) {
        return tagTypes.add(new TagType(tagTypeName, multiValued));
    }

    /**
     * Remove a tag type of the user
     * 
     * @param tagTypeName the name of the tag type
     * @return true if the tag type is successfully removed
     */
    public boolean removeTagType(String tagTypeName) {
        TagType type = getTagType(tagTypeName);
        if (type == null) return false;
        return tagTypes.remove(type);
    }
    
    /**
     * Gets a list of tag types
     * 
     * @return a list of tag types
     */
    public List<TagType> getTagTypes() {
        return tagTypes;
    }

    /**
     * Gets the tag type
     * 
     * @param tagTypeName the tag type name
     * @return the tag type with tag type name
     */
    public TagType getTagType(String tagTypeName) {
        return tagTypes.stream().filter(tagType -> tagType.getName().equals(tagTypeName)).findFirst().orElse(null);
    }

    /**
     * Gets the username
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user with the username and password
     * 
     * @param username the username of the user
     * @param password the password of the user
     * @throws IllegalArgumentException the username and password are null or blank
     */
    private void setUser(String username, String password) throws IllegalArgumentException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("The username or password is invalid.");
        }
        this.username = username;
        this.password = password;
    }

    /**
     * Compares two User objects by their username
     * 
     * @returns true if the two users have the same usernames
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return username.equals(other.getUsername());
    }

    /**
     * The list iterator of the album of the users
     * 
     * @return the list iterator of the album
     */
    public ListIterator<Album> getAlbumListIterator() {
        return albums.listIterator();
    }

    /**
     * Adds an album of the user
     * 
     * @param album the album of the user
     * @return true if the album has been successfully added
     */
    public boolean addAlbum(Album album) {
        return albums.add(album);
    }

    /**
     * Removes an album from the user
     * 
     * @param album the album of the user
     * @return true if the album has been successfully removed
     */
    public boolean removeAlbum(Album album) {
        return albums.remove(album);
    }

    /**
     * Copies a photo from one album to another album
     * 
     * @param destAlbumName the destination album of the copied photo
     * @param photo the photo to be copied
     * @throws Exception the destination album does not exists or the photo was unable to be copied
     */
    public void copyPhoto(String destAlbumName, Photo photo) throws Exception {
        Album destAlbum = albums.stream().filter(album -> album.getName().equals(destAlbumName)).findAny().orElse(null);

        if (destAlbum == null) {
            throw new NullPointerException("Destination album does not exist.");
        }

        if (!destAlbum.addPhoto(photo)) {
            throw new Exception("Unable to add the photo in the destination album.");
        }
    }

    /**
     * Moves a photo from one album to another album
     * 
     * @param srcAlbumName the source album of the photo
     * @param destAlbumName the destination album of the photo to move
     * @param photo the photo to be moved
     * @throws Exception the source or destination album does not exist, or the photo was unable to be moved
     */
    public void movePhoto(String srcAlbumName, String destAlbumName, Photo photo) throws Exception {
        Album srcAlbum = albums.stream().filter(album -> album.getName().equals(srcAlbumName)).findAny().orElse(null);
        Album destAlbum = albums.stream().filter(album -> album.getName().equals(destAlbumName)).findAny().orElse(null);
        
        if (srcAlbumName == null) {
            throw new NullPointerException("Source album does not exist.");
        }
        if (destAlbum == null) {
            throw new NullPointerException("Destination album does not exist.");
        }
        if (!srcAlbum.removePhoto(photo)) {
            throw new Exception("Unable to remove the photo from the source album.");
        }
        if (!destAlbum.addPhoto(photo)) {
            srcAlbum.addPhoto(photo);
            throw new Exception("Unable to add the photo in the destination album.");
        }
    }

    /**
     * Checks if the user has albums
     * 
     * @return true if the user has an album
     */
    public boolean hasAlbums() {
        return !albums.isEmpty();
    }

    /**
     * Provides the number of albums for the user
     * 
     * @return the number of albums for the user
     */
    public int getAlbumCount() {
        return albums.size();
    }

    /**
     * Authenticates the user with the given username and password
     * 
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return true if the authentication is successful
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
