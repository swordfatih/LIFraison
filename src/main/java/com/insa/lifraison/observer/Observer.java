package com.insa.lifraison.observer;

/**
 * The observer can observe one or multiple Observable.
 * Everytime one of its Observable notify it of an update,
 * it receives all information necessary for its update
 */
public interface Observer {
    /**
     * Update the view
     * @param type the type of notification
     * @param observed the observable which has notified the observer
     * @param arg optional information about the update
     */
    public void update(Observable.NotifType type, Observable observed, Object arg);
}
