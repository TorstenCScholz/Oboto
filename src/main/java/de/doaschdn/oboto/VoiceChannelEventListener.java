package de.doaschdn.oboto;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


// TODO: Create different event listeners

@AllArgsConstructor()
public class VoiceChannelEventListener implements EventListener {
    private final static Logger log = LoggerFactory.getLogger(VoiceChannelEventListener.class);

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private ApplicationProperties applicationProperties;

    @Override
    public void onEvent(final Event event) {
        final JDA jda = event.getJDA();
        TextChannel statusChannel = jda.getTextChannelById(applicationProperties.getServer().getStatusChannelId());

        log.info("Event: {}", event.getClass().getCanonicalName());

        if (event instanceof GuildVoiceJoinEvent) {
            GuildVoiceJoinEvent guildVoiceJoinEvent = (GuildVoiceJoinEvent) event;

            Member member = guildVoiceJoinEvent.getMember();
            Channel channelJoined = guildVoiceJoinEvent.getChannelJoined();

            log.info("User {} joined voice channel {}", member.getEffectiveName(), channelJoined.getName());

            String timeJoined = DATE_FORMAT.format(new Date());

            statusChannel.sendMessage(String.format("[%s] **%s** joined :sound:**%s**.", timeJoined, member.getEffectiveName(), channelJoined.getName())).queue();
        } else if (event instanceof GuildVoiceLeaveEvent) {
            GuildVoiceLeaveEvent guildVoiceLeaveEvent = (GuildVoiceLeaveEvent) event;

            Member member = guildVoiceLeaveEvent.getMember();
            Channel channelLeft = guildVoiceLeaveEvent.getChannelLeft();

            log.info("User {} left voice channel {}", member.getEffectiveName(), channelLeft.getName());

            String timeLeft = DATE_FORMAT.format(new Date());

            statusChannel.sendMessage(String.format("[%s] **%s** left :sound:**%s**.", timeLeft, member.getEffectiveName(), channelLeft.getName())).queue();
        } else if (event instanceof GuildVoiceMoveEvent) {
            GuildVoiceMoveEvent guildVoiceMoveEvent = (GuildVoiceMoveEvent) event;

            Member member = guildVoiceMoveEvent.getMember();
            Channel channelJoined = guildVoiceMoveEvent.getChannelJoined();
            Channel channelLeft = guildVoiceMoveEvent.getChannelLeft();

            log.info("User {} was moved from voice channel {} to {}", member.getEffectiveName(), channelLeft.getName(), channelJoined.getName());

            String timeLeft = DATE_FORMAT.format(new Date());

            statusChannel.sendMessage(String.format("[%s] **%s** was moved from :sound:%s to :sound:**%s**.", timeLeft, member.getEffectiveName(), channelLeft.getName(), channelJoined.getName())).queue();
        }
    }
}
