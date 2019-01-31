package de.kreth.vaadin.clubhelper.vaadinclubhelper;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.kreth.googleconnectors.calendar.CalendarAdapter;

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

	@Bean
	public CalendarAdapter calendarAdapter() throws GeneralSecurityException, IOException {
		return new CalendarAdapter();
	}

}
