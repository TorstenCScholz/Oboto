package de.doaschdn.oboto.command;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import net.dv8tion.jda.core.entities.Channel;

public class ChannelInfoCommand implements CommandExecutor {
    @Command(aliases = {"!channelInfo", "!ci"}, description = "Returns some basic information about the channel", async = true, privateMessages = false)
    public String onCommand(Channel channel) {
        return "You are in channel #" + channel.getName() + " with id " + channel.getId();
    }
}
