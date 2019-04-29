package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

class ClubhelperErrorDialogTest {

	@Mock
	private UI ui;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testErrorDialogContent() {
		Exception e = new Exception("Exception message");
		ClubhelperErrorDialog dlg = new ClubhelperErrorDialog("errormessage", e);
		dlg.show(ui);
		ArgumentCaptor<Window> windowCaptor = ArgumentCaptor.forClass(Window.class);
		verify(ui).addWindow(windowCaptor.capture());
		Window window = windowCaptor.getValue();
		Component content = window.getContent();
		assertTrue(content instanceof HasComponents);
		boolean errorMessageFound = false;
		boolean exceptionMessageFound = false;
		boolean stacktraceFound = false;
		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out);
		e.printStackTrace(writer);
		writer.close();
		String stacktraceLine = out.toString().split("\n")[0].trim();
		for (Component c : (HasComponents) content) {
			if (c instanceof Label) {
				Label label = (Label) c;
				String text = label.getValue();
				if ("errormessage".equals(text)) {
					errorMessageFound = true;
				}
				else if ("Exception message".equals(text)) {
					exceptionMessageFound = true;
				}
				else if (text.contains(stacktraceLine)) {
					stacktraceFound = true;
				}
			}
		}
		assertTrue(errorMessageFound, "Errormessage (caption) not found!");
		assertTrue(exceptionMessageFound, "Exception message not found!");
		assertTrue(stacktraceFound, "Stacktrace not found!");
	}

}
