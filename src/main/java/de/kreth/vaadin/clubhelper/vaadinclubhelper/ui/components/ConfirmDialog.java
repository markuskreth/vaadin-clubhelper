package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.EnumSet;
import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConfirmDialog extends Window {

	private static final long serialVersionUID = -3043842684692296495L;

	private Label message;

	private ConfirmDialog(Builder builder) {
		super(builder.caption);
		setModal(true);
		setClosable(false);

		this.message = new Label(builder.message);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		for (Buttons button : builder.buttons) {

			Button b = new Button(button.caption);
			b.addClickListener(ev -> {
				ConfirmDialog.this.close();
				if (builder.resultHandler != null) {
					builder.resultHandler.accept(button);
				}
			});
			buttonLayout.addComponent(b);
		}
		VerticalLayout layout = new VerticalLayout();
		layout.addComponents(message, buttonLayout);
		setContent(layout);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String caption;
		private String message;
		private EnumSet<Buttons> buttons;
		private Consumer<Buttons> resultHandler;

		private Builder() {
			buttons = EnumSet.noneOf(Buttons.class);
		}

		public Builder setCaption(String caption) {
			this.caption = caption;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setResultHandler(Consumer<Buttons> resultHandler) {
			this.resultHandler = resultHandler;
			return this;
		}

		public Builder saveDiscardCancel() {
			buttons = EnumSet.of(Buttons.CANCEL, Buttons.DISCARD, Buttons.SAVE);
			return this;
		}

		public ConfirmDialog build() {
			return new ConfirmDialog(this);
		}

	}

	public enum Buttons {
		YES("JA"), NO("NEIN"), OK("OK"), SAVE("Speichern"), DISCARD("Verwerfen"), CANCEL("Abbrechen");

		final String caption;

		private Buttons(String caption) {
			this.caption = caption;
		}
	}
}
