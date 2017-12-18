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
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VoiceChannelService {
    private final static Logger log = LoggerFactory.getLogger(VoiceChannelService.class);

    private AudioPlayerManager playerManager;

    private AudioPlayer audioPlayer;

    public VoiceChannelService() {
        playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        AudioSourceManagers.registerRemoteSources(playerManager);
        audioPlayer = playerManager.createPlayer();
    }

    public void joinVoice(ApplicationProperties applicationProperties, final JDA jda) {
        VoiceChannel voiceChannel = jda.getVoiceChannelById(applicationProperties.getServer().getVoiceChannelId());
        Guild guild = voiceChannel.getGuild();

        try {
            if (!guild.getAudioManager().isConnected() || !guild.getAudioManager().getConnectedChannel().equals(voiceChannel)) {
                guild.getAudioManager().openAudioConnection(voiceChannel);
            }
        }
        catch (PermissionException e) {
            log.error("Cannot play audio.");
            e.printStackTrace();
        }
    }

    public void playSound(AudioManager audioManager, String filename) {
//                    TrackScheduler trackScheduler = new TrackScheduler(player);
//                    player.addListener(trackScheduler);

        if (audioManager.getSendingHandler() == null) {
            audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        }

        log.info("YOLO {}", System.getProperty("user.dir"));

        playerManager.loadItem(filename, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
//                            trackScheduler.queue(track);
                audioPlayer.playTrack(track);
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
}
