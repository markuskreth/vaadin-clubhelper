package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.CsvExporter;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.EventGrid;

public class ExportCsvCommand implements ClubCommand {

	private final AbstractComponent button;

	private final ApplicationContext context;

	public ExportCsvCommand(AbstractComponent button, ApplicationContext context) {
		super();
		this.button = button;
		this.context = context;
	}

	@Override
	public String getLabel() {
		return "Export Termintabelle";
	}

	@Override
	public Resource getIcon() {
		return VaadinIcons.FILE_TEXT;
	}

	@Override
	public void execute() {

		EventBusiness eventBusiness = context.getBean(EventBusiness.class);
		EventGrid grid = new EventGrid(eventBusiness, true);

		HorizontalLayout head = new HorizontalLayout();
		Button downloadButton = new Button("Download", VaadinIcons.DOWNLOAD);

		FileDownloader downloader = new FileDownloader(csvDownload(grid));
		downloader.extend(downloadButton);

		head.addComponents(new Label("Veranstaltungen"), downloadButton);

		VerticalLayout layout = new VerticalLayout();
		layout.addComponents(head, grid);

		Window window = new Window();
		window.setCaption("Veranstaltungen");
		window.setContent(layout);
		window.setModal(true);
		window.setWidth("50%");
		window.setHeight("90%");

		button.getUI().addWindow(window);
	}

	private StreamResource csvDownload(EventGrid grid) {
		List<ClubEvent> items = grid.getDataProvider()
				.fetch(new Query<>())
				.collect(Collectors.toList());
		CsvExporter exporter = new CsvExporter();
		StringWriter writer = new StringWriter();

		try {
			exporter.export(items, writer);
		}
		catch (IOException e) {
			Notification.show("Fehler beim Erzeugen der Veranstaltungen");
			throw new RuntimeException(e);
		}

		InputStream in = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
		final StreamResource resource = new StreamResource(() -> in, "Veranstaltungen.csv");
		resource.setMIMEType("application/pdf");

		return resource;

	}

}
