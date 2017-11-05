package de.doaschdn.oboto;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;

public class TrackScheduler implements AudioEventListener {
    private final AudioPlayer audioPlayer;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void onEvent(AudioEvent audioEvent) {

    }

//    @Override
//    public void onPlayerPause(AudioPlayer player) {
//        // Player was paused
//    }
//
//    @Override
//    public void onPlayerResume(AudioPlayer player) {
//        // Player was resumed
//    }
//
//    @Override
//    public void onTrackStart(AudioPlayer player, AudioTrack track) {
//        // A track started playing
//    }
//
//    @Override
//    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
//        if (endReason.mayStartNext) {
//            // Start next track
//        }
//
//        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
//        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
//        // endReason == STOPPED: The player was stopped.
//        // endReason == REPLACED: Another track started playing while this had not finished
//        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
//        //                       clone of this back to your queue
//    }
//
//    @Override
//    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
//        // An already playing track threw an exception (track end event will still be received separately)
//    }
//
//    @Override
//    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
//        // Audio track has been unable to provide us any audio, might want to just start a new track
//    }
}
