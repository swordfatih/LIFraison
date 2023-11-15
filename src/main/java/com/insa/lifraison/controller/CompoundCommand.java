package com.insa.lifraison.controller;

import java.util.ArrayList;
/**
 * CompoundCommand is a class which permits to make multiples commands as one command
 * The class extends Command {@link com.insa.lifraison.controller.Command}
 */
public class CompoundCommand implements Command {
    private ArrayList<Command> commands;

    /**
     * Constructor of CompoundCommand
     */
    public CompoundCommand() {
        this.commands = new ArrayList<>();
    }

    /**
     * Constructor of CompoundCommand
     */
    public CompoundCommand(ArrayList<Command> commands) {
        this.commands = commands;
    }

    /**
     * Add a command in commands
     * @param command the command to add
     */
    public void addCommand(Command command) {
        this.commands.add(command);
    }

    /**
     * Execute all commands of the list
     */
    @Override
    public void doCommand() {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).doCommand();
        }
    }

    /**
     * Undo all commands of the list
     */
    public void undoCommand() {
        for (int i = commands.size()-1; i >= 0; i--) {
            commands.get(i).undoCommand();
        }
    }
}
