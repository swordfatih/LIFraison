package com.insa.lifraison.observer;

import java.util.Collection;
import java.util.ArrayList;


public class Observable {
    public enum NotifType {
        FULL_UPDATE,
        ADD,
        DELETE
    }

    private Collection<Observer> observers;

    public Observable(){
        observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o){
        if (!observers.contains(o)){
            observers.add(o);
        }
    }
    public void notifyObservers(NotifType type){
        for (Observer o : observers)
            o.update(type, this, null);
    }

    public void notifyObservers(NotifType type, Object arg){
        for (Observer o : observers)
            o.update(type, this, arg);
    }
}
