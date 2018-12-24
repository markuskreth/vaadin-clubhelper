package de.kreth.vaadin.clubhelper.vaadinclubhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "de.kreth.vaadin.clubhelper", "de.kreth.clubhelperbackend" })
@EnableScheduling
public class VaadinClubhelperApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(VaadinClubhelperApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(VaadinClubhelperApplication.class);
	}
}
