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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import java.lang.reflect.Method;
import java.util.Set;

@SpringBootApplication
public class ObotoApplication implements CommandLineRunner {
    private ApplicationProperties applicationProperties;

    private Reflections reflections;

    @Inject
    public ObotoApplication(ApplicationProperties applicationProperties, Reflections reflections) {
        this.applicationProperties = applicationProperties;
        this.reflections = reflections;
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
                CommandExecutor commandExecutor = (CommandExecutor) method.getDeclaringClass().newInstance();
                cmdHandler.registerCommand(commandExecutor);
            }
            catch (InstantiationException | IllegalAccessException e) {
                System.err.println("Cannot instantiate command.");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run(String... strings) throws Exception {
        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(applicationProperties.getToken())
                .buildBlocking();

        registerCommands(jda);
    }
}
