package de.doaschdn.oboto.command;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.doaschdn.oboto.ApplicationProperties;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.entities.Channel;

@AllArgsConstructor
public class ChannelInfoCommand implements CommandExecutor {
    private ApplicationProperties applicationProperties;

    @Command(aliases = {"!channelInfo", "!ci"}, description = "Returns some basic information about the channel", async = true, privateMessages = false)
    public String onCommand(Channel channel) {
        return "You are in channel #" + channel.getName() + " with id " + channel.getId();
    }
}
