package com.example.photosapplication.model.state;

/**
 * AlbumsState - the albums state to load the albums page
 */
public class AlbumsState extends ApplicationState {
	private static AlbumsState instance = null;

    /* Instantiates the Albums state */
    private AlbumsState() { }

    /**
     * Gets the Albums state singleton
     * 
     * @return the albums state singleton
     */
	public static AlbumsState getInstance() {
		if (instance == null) {
			instance = new AlbumsState();
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
            case ALBUMS_TO_LOGIN:
                return LoginState.getInstance();
            case ALBUMS_TO_PHOTOS:
                return PhotosState.getInstance();
            default:
                throw new Exception("Invalid state transition.");
        }
    }
}
