package de.doaschdn.oboto.command;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.doaschdn.oboto.ApplicationProperties;

public class DebugCommand implements CommandExecutor {
    private ApplicationProperties applicationProperties;

    DebugCommand(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Command(aliases = {"!debug"}, description = "Debugging command.")
    public String onDebug() {
        return "Id: "
                + applicationProperties.getServer().getId()
                + ", Status: "
                + applicationProperties.getServer().getStatusChannelId()
                + ", Voice: "
                + applicationProperties.getServer().getVoiceChannelId();
    }
}
