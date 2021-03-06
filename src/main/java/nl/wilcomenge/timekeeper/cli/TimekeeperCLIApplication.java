package nl.wilcomenge.timekeeper.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TimekeeperCLIApplication implements ApplicationRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(TimekeeperCLIApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TimekeeperCLIApplication.class, args);
		context.close();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}
