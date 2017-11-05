package de.doaschdn.oboto;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
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

                    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                    playerManager.registerSourceManager(new LocalAudioSourceManager());

                    AudioSourceManagers.registerRemoteSources(playerManager);
                    AudioPlayer player = playerManager.createPlayer();
//                    TrackScheduler trackScheduler = new TrackScheduler(player);
//                    player.addListener(trackScheduler);

                    guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));

                    log.info("YOLO {}", System.getProperty("user.dir"));

                    playerManager.loadItem("1.mp3", new AudioLoadResultHandler() {
                        @Override
                        public void trackLoaded(AudioTrack track) {
//                            trackScheduler.queue(track);
                            player.playTrack(track);
                            log.info("Playing yo.");
                        }

                        @Override
                        public void playlistLoaded(AudioPlaylist playlist) {
                            for (AudioTrack track : playlist.getTracks()) {
//                                trackScheduler.queue(track);
                            }
                        }

                        @Override
                        public void noMatches() {
                            log.info("Rip, not found.");
                            // Notify the user that we've got nothing
                        }

                        @Override
                        public void loadFailed(FriendlyException throwable) {
                            log.info("Rip, loading failed.");
                            // Notify the user that everything exploded
                        }
                    });
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
