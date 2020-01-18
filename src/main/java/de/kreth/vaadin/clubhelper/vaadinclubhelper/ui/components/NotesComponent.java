package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.List;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.PersonNote;

public class NotesComponent extends VerticalLayout {

	private final TextArea notesComponent;

	private PersonNote note;

	private boolean hasChanges;

	public NotesComponent() {
		notesComponent = new TextArea();
		notesComponent.setMaxLength(2000);
		notesComponent.setCaption("Notizen");
		notesComponent.setRows(25);
		notesComponent.addValueChangeListener(this::textChange);
		notesComponent.setValueChangeMode(ValueChangeMode.LAZY);
		notesComponent.setValueChangeTimeout(1500);
//		addComponent(new Label("<H1>Notizen</H1>", ContentMode.HTML));
		addComponent(notesComponent);
		hasChanges = false;
	}

	private void textChange(ValueChangeEvent<String> ev) {
		if (note != null) {
			if (ev.isUserOriginated()) {
				hasChanges = true;
			}
			note.setNotetext(ev.getValue());
		}
	}

	public void setPerson(Person person) {
		notesComponent.clear();
		hasChanges = false;
		if (person != null) {
			List<PersonNote> notes = person.getNotes();
			if (notes.isEmpty() == false) {
				note = notes.get(0);
				notesComponent.setValue(note.getNotetext());
			}
			else {
				note = new PersonNote();
				notes.add(note);
				note.setPerson(person);
			}
		}
	}

	public boolean hasChanges() {
		return hasChanges;
	}

}
