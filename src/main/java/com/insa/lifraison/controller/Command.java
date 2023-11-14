package com.insa.lifraison.controller;

/**
 * Interface Command to execute a command
 */
public interface Command {

    /**
     * Execute the command this
     */
    void doCommand();

    /**
     * Execute the reverse command of this
     */
    void undoCommand();
}
