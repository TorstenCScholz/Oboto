package de.doaschdn.oboto;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class DiscordListenerAdapter extends ListenerAdapter {
    private final static Logger log = LoggerFactory.getLogger(DiscordListenerAdapter.class);

    private ApplicationProperties applicationProperties;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        final JDA jda = event.getJDA();

        if (event.isFromType(ChannelType.PRIVATE)) {
            String messageContent = event.getMessage().getContent();

            // TODO: SUPER TODO, CLEAN UP THIS MESS
            if (messageContent.startsWith("+voice")) {
                VoiceChannel voiceChannel = jda.getVoiceChannelById(applicationProperties.getServer().getVoiceChannelId());
                Guild guild = voiceChannel.getGuild();

                try {
                    if (!guild.getAudioManager().isConnected()) {
                        guild.getAudioManager().openAudioConnection(voiceChannel);
                    }
                }
                catch (PermissionException e) {
                    log.error("Cannot play audio.");
                    e.printStackTrace();
                }
            } else if (messageContent.startsWith("-voice")) {
                VoiceChannel voiceChannel = jda.getVoiceChannelById(applicationProperties.getServer().getVoiceChannelId());
                Guild guild = voiceChannel.getGuild();

                guild.getAudioManager().closeAudioConnection();
            }
        }
    }
}
