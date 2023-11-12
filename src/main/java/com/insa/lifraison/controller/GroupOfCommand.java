package com.insa.lifraison.controller;

import java.util.ArrayList;

public class GroupOfCommand implements Command {
    private ArrayList<Command> commands;

    public GroupOfCommand() {
        this.commands = new ArrayList<>();
    }

    public GroupOfCommand(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public void doCommand() {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).doCommand();
        }
    }

    public void undoCommand() {
        for (int i = commands.size()-1; i >= 0; i--) {
            commands.get(i).undoCommand();
        }
    }
}
