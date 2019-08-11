package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.security.SecurityVerifier;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperNavigation;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.ClubhelperViews;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Component
public class MenuItemStateFactory implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemStateFactory.class);

	private ApplicationContext context;

	@Autowired
	ClubhelperNavigation clubhelperNavigation;

	@Autowired
	SecurityVerifier securityGroupVerifier;

	private UI ui;

	private Supplier<ZonedDateTime> startDateSupplier;

	private Supplier<ZonedDateTime> endDateSupplier;

	public MenuItemState currentState() {
		MenuItemState state;
		View currentView = clubhelperNavigation.getNavigator().getCurrentView();
		ClubhelperViews current = ClubhelperViews.byView(currentView);
		if (ClubhelperViews.PersonEditView == current) {
			state = new LoggedinMenuitemState(context, startDateSupplier, endDateSupplier, this::showPrint);
		}
		else if (securityGroupVerifier.isLoggedin()) {

			state = new LoggedinMenuitemState(context, startDateSupplier, endDateSupplier, this::showPrint);
		}
		else {
			state = new LoggedOffState(context, startDateSupplier, endDateSupplier, this::showPrint);
		}

		return state;
	}

	private void showPrint(String calendarMonth, JasperPrint print) {
		Window window = new Window();
		window.setCaption("View PDF");
		AbstractComponent e;
		try {
			e = createEmbedded(calendarMonth, print);
		}
		catch (Exception e1) {
			LOGGER.error("Error creating PDF Element", e1);
			return;
		}
		window.setContent(e);
		window.setModal(true);
		window.setWidth("50%");
		window.setHeight("90%");
		ui.addWindow(window);
	}

	private AbstractComponent createEmbedded(String title, JasperPrint print) throws IOException, JRException {

		Path pdfFile = Files.createTempFile(title.replace(' ', '_'), ".pdf").toAbsolutePath();
		JasperExportManager.exportReportToPdfFile(print, pdfFile.toString());

		final FileResource resource = new FileResource(pdfFile.toFile());

		BrowserFrame c = new BrowserFrame("PDF invoice", resource);

		c.setSizeFull();

		return c;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public void setStartDateSupplier(Supplier<ZonedDateTime> startDateSupplier) {
		this.startDateSupplier = startDateSupplier;
	}

	public void setEndDateSupplier(Supplier<ZonedDateTime> endDateSupplier) {
		this.endDateSupplier = endDateSupplier;
	}

	public void configure(UI ui) {
		this.ui = ui;
	}

}
