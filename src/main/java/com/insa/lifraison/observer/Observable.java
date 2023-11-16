package com.insa.lifraison.observer;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Observable is a class which extends from {@link com.insa.lifraison.observer.Selectable}
 * It is the class which notifies the observer when something is changing in it.
 */
public class Observable extends Selectable {
    /**
     * Types of notifications
     */
    public enum NotifType {
        UPDATE,
        ADD,
        REMOVE
    }

    /**
     * List of {@link com.insa.lifraison.observer.Observer} of the observable
     */
    private Collection<Observer> observers;

    public Observable(){
        observers = new ArrayList<Observer>();
    }

    /**
     * Add a new observer to the list of observers
     * @param o the new observer
     */
    public void addObserver(Observer o){
        if (!observers.contains(o)){
            observers.add(o);
        }
    }

    /**
     * Notify all observers
     * @param type type of notification
     *             <code>UPDATE</code> clear and recreate all information of the view
     *             <code>ADD</code> add a unique element on the view
     *             <code>REMOVE</code> remove a unique element on the view
     * @param arg the object that might be added, modify or removed from the observable
     */
    public void notifyObservers(NotifType type, Object arg){
        for (Observer o : observers) {
            o.update(type, this, arg);
        }
    }

    /**
     * Notify all observers
     * @param type type of notification
     */
    public void notifyObservers(NotifType type){
        notifyObservers(type, null);
    }

    /**
     * Select the Observable and notify
     */
    @Override
    public void select() {
        super.select();
        notifyObservers(NotifType.UPDATE);
    }

    /**
     * Unselect the Observable and notify
     */
    @Override
    public void unselect() {
        super.unselect();
        notifyObservers(NotifType.UPDATE);
    }
}
