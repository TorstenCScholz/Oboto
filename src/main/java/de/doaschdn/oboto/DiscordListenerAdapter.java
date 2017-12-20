package de.doaschdn.oboto;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class DiscordListenerAdapter extends ListenerAdapter {
    private final static Logger log = LoggerFactory.getLogger(DiscordListenerAdapter.class);

    private ApplicationProperties applicationProperties;

    private VoiceChannelService voiceChannelService;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        final JDA jda = event.getJDA();

        if (event.isFromType(ChannelType.PRIVATE)) {
            final String messageContent = event.getMessage().getContent();

            if (messageContent.startsWith("+voice")) {
                voiceChannelService.joinVoice(applicationProperties, jda);
            } else if (messageContent.startsWith("-voice")) {
                voiceChannelService.leaveVoice(applicationProperties, jda);
            }
        }
    }
}
