package com.example.photosapplication.model.state;

/**
 * PhotosState - the photos state to load the photos page
 */
public class PhotosState extends ApplicationState {
	private static PhotosState instance = null;

    /* Instantiates the Photos state */
    private PhotosState() { }

    /**
     * Gets the Photos state singleton
     * 
     * @return the photos state singleton
     */
	public static PhotosState getInstance() {
		if (instance == null) {
			instance = new PhotosState();
		}
		return instance;
	}

    /**
     * Enter the Albums state to load the albums page and set up the classes
     */
    public void enter() {

	}

    /**
     * Processes the event based on the event input to the application state
     */
    public ApplicationState processEvent() throws Exception {
        switch (processEventInput) {
            case PHOTOS_TO_ALBUMS:
                return AlbumsState.getInstance();
            default:
                throw new Exception("Invalid state transition.");
        }
    }
}
