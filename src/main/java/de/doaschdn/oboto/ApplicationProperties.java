package de.doaschdn.oboto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties()
@Getter
@Setter
public class ApplicationProperties {
    private String token;

    private Server server;

    @Getter
    @Setter
    public static class Server {
        private String id;

        private String statusChannelId;

        private String voiceChannelId;
    }
}
