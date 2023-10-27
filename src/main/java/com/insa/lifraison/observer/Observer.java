package com.insa.lifraison.observer;

public interface Observer {
    public void update(Observable.NotifType type, Object arg);
}
