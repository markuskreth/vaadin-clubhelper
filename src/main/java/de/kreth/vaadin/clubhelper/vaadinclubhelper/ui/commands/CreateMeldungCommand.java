package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung.EventMeldung;

public class CreateMeldungCommand implements ClubCommand {

	private EventBusiness business;

	private Consumer<EventMeldung> showMeldungCommand;

	public CreateMeldungCommand(ApplicationContext context, Consumer<EventMeldung> showMeldungCommand) {
		this.business = context.getBean(EventBusiness.class);
		this.showMeldungCommand = showMeldungCommand;
	}

	@Override
	public String getLabel() {
		return "Meldung erstellen";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.OUTBOX;
	}

	@Override
	public void execute() {
		showMeldungCommand.accept(business.createMeldung());
	}

}
