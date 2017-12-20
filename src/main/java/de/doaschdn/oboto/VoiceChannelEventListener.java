package de.doaschdn.oboto;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


// TODO: Create different event listeners

@AllArgsConstructor()
public class VoiceChannelEventListener implements EventListener {
    private final static Logger log = LoggerFactory.getLogger(VoiceChannelEventListener.class);

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private ApplicationProperties applicationProperties;

    private VoiceChannelService voiceChannelService;

    // TODO
    final static String[] helloSoundFilenames = new String[] {
            "hello/D.VA - Kor 1.mp3",
            "hello/Genji - Hello.mp3",
            "hello/Hanzo - Greetings.mp3",
            "hello/Junkrat - Gooday Mate.mp3",
            "hello/Lúcio - Hello.mp3",
            "hello/McCree - Hello.mp3",
            "hello/McCree - Howdy.mp3",
            "hello/Mei - Chinese 13.mp3",
            "hello/Reinhardt - Hello.mp3",
            "hello/Torbjörn - Hello.mp3",
            "hello/Tracer - Hiya.mp3",
            "hello/Widowmaker - Bonjour.mp3",
            "hello/Winston - Hi There.mp3",
            "hello/Zarya - Privjet.mp3",
            "hello/Zenyatta - Greetings.mp3",
            "hello/Doomfist - Hello there.ogg"
    };

    final static String[] byeSoundFilenames = new String[] {
            "bye/Genji - Fare Well.mp3",
            "bye/Reinhardt - Farewell.mp3",
            "bye/Reinhardt - Goodbye.mp3",
            "bye/Soldier 76 - Dismissed.mp3",
            "bye/Zenyatta - Farewell.mp3"
    };

    @Override
    public void onEvent(final Event event) {
        final JDA jda = event.getJDA();
        final TextChannel statusChannel = jda.getTextChannelById(applicationProperties.getServer().getStatusChannelId());

        log.info("Event: {}", event.getClass().getCanonicalName());

        if (event instanceof ReadyEvent) {
            voiceChannelService.leaveVoice(applicationProperties, jda);
            voiceChannelService.joinVoice(applicationProperties, jda);
        } else if (event instanceof GuildVoiceJoinEvent) {
            final GuildVoiceJoinEvent guildVoiceJoinEvent = (GuildVoiceJoinEvent) event;

            final Member member = guildVoiceJoinEvent.getMember();
            final Channel channelJoined = guildVoiceJoinEvent.getChannelJoined();
            final Guild guild = guildVoiceJoinEvent.getGuild();

            if (!guild.getId().equals(applicationProperties.getServer().getId())) {
                return;
            }

            log.info("User {} joined voice channel {}", member.getEffectiveName(), channelJoined.getName());

            final String timeJoined = DATE_FORMAT.format(new Date());

            statusChannel.sendMessage(String.format("[%s] **%s** joined :sound:**%s**.", timeJoined, member.getEffectiveName(), channelJoined.getName())).queue();

            voiceChannelService.playSound(((GuildVoiceJoinEvent) event).getGuild().getAudioManager(), getRandomArrayEntry(helloSoundFilenames), guild);
        } else if (event instanceof GuildVoiceLeaveEvent) {
            final GuildVoiceLeaveEvent guildVoiceLeaveEvent = (GuildVoiceLeaveEvent) event;

            final Member member = guildVoiceLeaveEvent.getMember();
            final Channel channelLeft = guildVoiceLeaveEvent.getChannelLeft();
            final Guild guild = guildVoiceLeaveEvent.getGuild();

            if (!guild.getId().equals(applicationProperties.getServer().getId())) {
                return;
            }

            log.info("User {} left voice channel {}", member.getEffectiveName(), channelLeft.getName());

            final String timeLeft = DATE_FORMAT.format(new Date());

            statusChannel.sendMessage(String.format("[%s] **%s** left :sound:**%s**.", timeLeft, member.getEffectiveName(), channelLeft.getName())).queue();

            voiceChannelService.playSound(((GuildVoiceLeaveEvent) event).getGuild().getAudioManager(), getRandomArrayEntry(byeSoundFilenames), guild);
        } else if (event instanceof GuildVoiceMoveEvent) {
            final GuildVoiceMoveEvent guildVoiceMoveEvent = (GuildVoiceMoveEvent) event;

            final Member member = guildVoiceMoveEvent.getMember();
            final Channel channelJoined = guildVoiceMoveEvent.getChannelJoined();
            final Channel channelLeft = guildVoiceMoveEvent.getChannelLeft();
            final Guild guild = guildVoiceMoveEvent.getGuild();

            if (!guild.getId().equals(applicationProperties.getServer().getId())) {
                return;
            }

            // TODO: Event occurs even if the user themselves moved to another channel
            log.info("User {} was moved from voice channel {} to {}", member.getEffectiveName(), channelLeft.getName(), channelJoined.getName());

            final String timeLeft = DATE_FORMAT.format(new Date());

            statusChannel.sendMessage(String.format("[%s] **%s** was moved from :sound:%s to :sound:**%s**.", timeLeft, member.getEffectiveName(), channelLeft.getName(), channelJoined.getName())).queue();

            // Check if moved user was the bot itself. If so, rejoin.
            if (((GuildVoiceMoveEvent) event).getMember().getUser().getId().equals(jda.getSelfUser().getId())) {
                log.info("Bot got moved out of voice channel without proper command. Rejoining.");
                voiceChannelService.joinVoice(applicationProperties, jda);
            } else {
                if (channelJoined.getId().equals(applicationProperties.getServer().getVoiceChannelId())) {
                    voiceChannelService.playSound(((GuildVoiceMoveEvent) event).getGuild().getAudioManager(), getRandomArrayEntry(helloSoundFilenames), guild);
                } else if (channelLeft.getId().equals(applicationProperties.getServer().getVoiceChannelId())) {
                    voiceChannelService.playSound(((GuildVoiceMoveEvent) event).getGuild().getAudioManager(), getRandomArrayEntry(byeSoundFilenames), guild);
                }
            }
        }
    }

    // TODO: Move somewhere else and make Random static
    private <T> T getRandomArrayEntry(T[] array) {
        return array[new Random().nextInt(array.length)];
    }
}
