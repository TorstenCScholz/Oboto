package de.doaschdn.oboto.command;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.doaschdn.oboto.ApplicationProperties;

public class PingCommand implements CommandExecutor {
    private ApplicationProperties applicationProperties;

    public PingCommand(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Command(aliases = {"!ping"}, description = "Pong!")
    public String onCommand(String command, String[] args) {
        return "Pong!";
    }
}
