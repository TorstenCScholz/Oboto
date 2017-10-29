package de.doaschdn.oboto.command;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.doaschdn.oboto.ApplicationProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PingCommand implements CommandExecutor {
    private ApplicationProperties applicationProperties;

    @Command(aliases = {"!ping"}, description = "Pong!")
    public String onCommand(String command, String[] args) {
        return "Pong!";
    }
}
