package com.insa.lifraison.controller;

/**
 * Interface Command to execute a command
 */
public interface Command {

    /**
     * Execute the command
     */
    void doCommand();

    /**
     * Execute the reverse command
     */
    void undoCommand();
}
