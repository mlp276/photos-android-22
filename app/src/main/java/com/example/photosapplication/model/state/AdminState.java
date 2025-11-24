package com.example.photosapplication.model.state;

/**
 * AdminState - the admin state to load the admin page
 */
public class AdminState extends ApplicationState {
	private static AdminState instance = null;

    /* Instantiates the AdminState */
    private AdminState() { }

    /**
     * Get the AdminState singleton instance
     * 
     * @return the AdminState singleton instance
     */
	public static AdminState getInstance() {
		if (instance == null) {
			instance = new AdminState();
		}
		return instance;
	}

    /**
     * Enter the Admin state to load the admin page and set up the classes
     */
    public void enter() {

    }

    /**
     * Processes the event based on the event input to the application state
     */
    public ApplicationState processEvent() throws Exception {
        switch (processEventInput) {
            case ADMIN_TO_LOGIN:
                return LoginState.getInstance();
            default:
                throw new Exception("Invalid state transition.");
        }
    }
}
