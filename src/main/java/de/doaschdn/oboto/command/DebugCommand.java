package de.doaschdn.oboto.command;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.doaschdn.oboto.ApplicationProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DebugCommand implements CommandExecutor {
    private ApplicationProperties applicationProperties;

    @Command(aliases = {"!debug"}, description = "Debugging command.")
    public String onDebug() {
        return applicationProperties.toString();
    }
}
