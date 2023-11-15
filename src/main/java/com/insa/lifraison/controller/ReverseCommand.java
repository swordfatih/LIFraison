package com.insa.lifraison.controller;

public class ReverseCommand implements Command{
    private final Command cmd;

    /**
     * Create the command reverse to cmd (so that cmd.doCommand corresponds to this.undoCommand, and vice-versa)
     * @param cmd the command to reverse
     */
    public ReverseCommand(Command cmd){
        this.cmd = cmd;
    }

    @Override
    public void doCommand() {
        cmd.undoCommand();
    }

    @Override
    public void undoCommand() {
        cmd.doCommand();
    }

}
