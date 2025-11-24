package com.example.photosapplication.model.state;

/**
 * LoginState - the login state to load the login page
 */
public class LoginState extends ApplicationState {
	private static LoginState instance = null;

    /* Instantiates the Login state */
    private LoginState() { }

    /**
     * Gets the Login state singleton
     * 
     * @return the login state singleton
     */
    public static LoginState getInstance() {
		if (instance == null) {
			instance = new LoginState();
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
            case LOGIN_TO_ALBUMS:
                return AlbumsState.getInstance();
            case LOGIN_TO_ADMIN:
                return AdminState.getInstance();
            default:
                throw new Exception("Invalid state transition.");
        }
    }
}
