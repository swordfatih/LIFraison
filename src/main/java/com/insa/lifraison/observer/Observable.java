package com.insa.lifraison.observer;

import java.util.Collection;
import java.util.ArrayList;


public class Observable extends Selectable {
    public enum NotifType {
        UPDATE,
        ADD,
        REMOVE
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

    public void notifyObservers(NotifType type, Object arg){
        for (Observer o : observers) {
            o.update(type, this, arg);
        }
    }
    public void notifyObservers(NotifType type){
        notifyObservers(type, null);
    }

    @Override
    public void select() {
        super.select();
        notifyObservers(NotifType.UPDATE);
    }

    @Override
    public void unselect() {
        super.unselect();
        notifyObservers(NotifType.UPDATE);
    }
}
