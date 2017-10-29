package de.doaschdn.oboto;

import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JDA3Handler;
import de.doaschdn.oboto.command.ChannelInfoCommand;
import de.doaschdn.oboto.command.PingCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;

@SpringBootApplication
public class ObotoApplication implements CommandLineRunner {
    private ApplicationProperties applicationProperties;

    @Inject
    public ObotoApplication(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

	public static void main(String[] args) throws InterruptedException, LoginException, RateLimitedException {
		SpringApplication.run(ObotoApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        // Note: It is important to register your ReadyListener before building
        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(applicationProperties.getToken())
                .buildBlocking();

        CommandHandler cmdHandler = new JDA3Handler(jda);

        // TODO: Register commands based on @Command anotation
        cmdHandler.registerCommand(new PingCommand());
        cmdHandler.registerCommand(new ChannelInfoCommand());
    }
}
