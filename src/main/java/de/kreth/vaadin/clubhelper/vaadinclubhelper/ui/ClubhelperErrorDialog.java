package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ClubhelperErrorDialog {

	private final Window errorDlg = new Window("Fehler");

	public ClubhelperErrorDialog(String errormessage, Exception exception) {
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(new Label(errormessage));
		layout.addComponent(new Label(exception.getMessage()));
		layout.addComponent(toStacktraceComponent(exception));
		layout.addComponent(new Button("Schließen", event -> errorDlg.close()));
		errorDlg.setContent(layout);
	}

	Label toStacktraceComponent(Exception exception) {

		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out);
		exception.printStackTrace(writer);
		writer.close();
		Label label = new Label(out.toString());
		label.setContentMode(ContentMode.PREFORMATTED);
		return label;
	}

	public void show(UI ui) {
		ui.addWindow(errorDlg);
	}
}
