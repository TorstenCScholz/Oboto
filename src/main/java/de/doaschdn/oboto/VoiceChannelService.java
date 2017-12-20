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

import java.util.LinkedList;

@Service
public class VoiceChannelService {
    private final static Logger log = LoggerFactory.getLogger(VoiceChannelService.class);

    private static final int WAIT_BEFORE_PLAYING_SOUND = 500;

    private AudioPlayerManager playerManager;

    private AudioPlayer audioPlayer;

    // Currently not needed, but keep for now as I am not 100% sure if playing sounds always works as intended
    private LinkedList<AudioTrack> playlist = new LinkedList<>();

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

    public void leaveVoice(ApplicationProperties applicationProperties, JDA jda) {
        VoiceChannel voiceChannel = jda.getVoiceChannelById(applicationProperties.getServer().getVoiceChannelId());
        Guild guild = voiceChannel.getGuild();

        guild.getAudioManager().closeAudioConnection();
    }

    public void playSound(AudioManager audioManager, final String filename, Guild guild) {
//                    TrackScheduler trackScheduler = new TrackScheduler(player);
//                    player.addListener(trackScheduler);

        if (!guild.getAudioManager().isConnected()) {
            log.info("Not connected to voice, not playing sound.");
            return;
        }

        if (audioManager.getSendingHandler() == null) {
            audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        }

        playerManager.loadItem(filename, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
//                            trackScheduler.queue(track);

                log.info("Loaded track: {}", filename);

                if (!playlist.contains(track)) {
                    playlist.add(track);
                }

                try {
                    Thread.sleep(WAIT_BEFORE_PLAYING_SOUND);
                } catch (InterruptedException e) {}

                audioPlayer.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
//                                trackScheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                log.info("Not found: {}", filename);
                // Notify the user that we've got nothing
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                log.info("Loading failed: {}", filename);
                // Notify the user that everything exploded
            }
        });
    }
}
