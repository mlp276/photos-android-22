package com.example.photosapplication.model.state;

/**
 * ApplicationState - the state of the application based on the page loaded
 */
public abstract class ApplicationState {

    public enum StateTransition {
        LOGIN_TO_ALBUMS,
        ALBUMS_TO_LOGIN,
        ALBUMS_TO_PHOTOS,
        PHOTOS_TO_ALBUMS,
        LOGIN_TO_ADMIN,
        ADMIN_TO_LOGIN
    };

    protected StateTransition processEventInput = null;

    /**
     * Sets the event input of the application state to transition
     * 
     * @param processEventInput the application state transition
     */
    public void setEventInput(StateTransition processEventInput) {
        this.processEventInput = processEventInput;
    }

    /**
     * Enters the application state
     */
	public abstract void enter();

    /**
     * Processes the event of the application state based on the input
     * 
     * @return the new application state based on the input
     * @throws Exception the application state failed to transition
     */
    public abstract ApplicationState processEvent() throws Exception;
}