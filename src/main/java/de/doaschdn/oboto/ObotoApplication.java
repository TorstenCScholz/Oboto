package de.doaschdn.oboto;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JDA3Handler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@SpringBootApplication
public class ObotoApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ObotoApplication.class);

    private ApplicationProperties applicationProperties;

    private Reflections reflections;

    private VoiceChannelService voiceChannelService;

    @Inject
    public ObotoApplication(ApplicationProperties applicationProperties,
                            Reflections reflections,
                            VoiceChannelService voiceChannelService) {
        this.applicationProperties = applicationProperties;
        this.reflections = reflections;
        this.voiceChannelService = voiceChannelService;
    }

	public static void main(String[] args) throws InterruptedException, LoginException, RateLimitedException {
		SpringApplication.run(ObotoApplication.class, args);
	}

	private void registerCommands(JDA jda) {
        CommandHandler cmdHandler = new JDA3Handler(jda);

        Set<Method> commands =
                reflections.getMethodsAnnotatedWith(Command.class);

        commands.forEach(method -> {
            try {
                CommandExecutor commandExecutor;

                if (CommandExecutor.class.isAssignableFrom(method.getDeclaringClass())) {
                    commandExecutor = (CommandExecutor) method
                            .getDeclaringClass()
                            .getConstructor(ApplicationProperties.class)
                            .newInstance(applicationProperties);

                    cmdHandler.registerCommand(commandExecutor);
                } else {
                    log.warn("Class '{}' containing command '{}' is no CommandExecutor, ignoring.", method.getDeclaringClass().getCanonicalName(), method.getName());
                }
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.warn("Cannot register Command: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run(String... strings) throws Exception {
        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(applicationProperties.getToken())
                .addEventListener(new DiscordListenerAdapter(applicationProperties, voiceChannelService))
                .addEventListener(new VoiceChannelEventListener(applicationProperties, voiceChannelService))
                .buildBlocking();

        registerCommands(jda);
    }
}
