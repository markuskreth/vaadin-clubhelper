package de.kreth.vaadin.clubhelper.vaadinclubhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VaadinClubhelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaadinClubhelperApplication.class, args);
	}
}
