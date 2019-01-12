package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class SingleEventView extends CustomComponent {

	private static final long serialVersionUID = 4701035948083549772L;

	private final TextField textTitle;
	private final TextField textLocation;

	public SingleEventView() {
		setCaption("Gewählte Veranstaltung");
		addStyleName("bold-caption");

		textTitle = new TextField();
		textTitle.setId("event.title");
		textTitle.setStyleName("title_label");
		textTitle.setCaption("Veranstaltung");
		textTitle.setEnabled(false);
		textTitle.setSizeFull();

		textLocation = new TextField();
		textLocation.setId("event.location");
		textLocation.setStyleName("title_label");
		textLocation.setCaption("Ort");
		textLocation.setEnabled(false);
		textLocation.setSizeFull();

		VerticalLayout panel = new VerticalLayout();
		panel.addComponents(textTitle, textLocation);
		setCompositionRoot(panel);
	}

	public void setTitle(String value) {
		if (value == null) {
			value = "";
		}
		textTitle.setValue(value);
	}

	public void setLocation(String value) {
		if (value == null) {
			value = "";
		}
		textLocation.setValue(value);
	}

	public void setEvent(ClubEvent ev) {

		if (ev != null) {

			setTitle(ev.getCaption());
			setLocation(ev.getLocation());

		} else {
			setTitle("");
			setLocation("");
		}
	}

}
