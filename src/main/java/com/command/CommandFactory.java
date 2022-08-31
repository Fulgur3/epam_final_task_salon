package com.command;

import java.util.Arrays;
import java.util.Optional;

public class CommandFactory {
    private static CommandFactory instance;

    private CommandFactory(){
    }

    public static CommandFactory getInstance(){
        if(instance==null){
            instance=new CommandFactory();
        }
        return instance;
    }

    public Optional<Command> defineCommand(String commandName) {
        return Arrays.stream(CommandType.values())
                .filter(command -> command.getName().equalsIgnoreCase(commandName))
                .findFirst()
                .map(CommandType::getCommand);
    }

    /**
     * Returns access level of the command with a given name
     *
     * @param commandName
     * Command name to look for
     * @return
     * Found command's access level
     */
    public Optional<CommandAccessLevel> getCommandRight(String commandName){
        return Arrays.stream(CommandType.values())
                .filter(command -> command.getName().equalsIgnoreCase(commandName))
                .findFirst()
                .map(CommandType::getCommandAccessLevel);
    }
}
