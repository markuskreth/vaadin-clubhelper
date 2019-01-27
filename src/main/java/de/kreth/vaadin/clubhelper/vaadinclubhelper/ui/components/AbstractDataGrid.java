package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.Editor;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public abstract class AbstractDataGrid<T> extends Grid<T> {

	private static final long serialVersionUID = -3404971410481135696L;
	private final List<T> contactSource;
	private final ListDataProvider<T> contactDataProvider;
	private boolean hasChanges;
	private Column<T, Component> deleteButtonColumn;
	private Consumer<T> deleteConsumer;

	public AbstractDataGrid() {

		this.contactSource = new ArrayList<>();
		this.contactDataProvider = DataProvider.ofCollection(contactSource);
		setDataProvider(contactDataProvider);

		Editor<T> editor = getEditor();
		editor.setEnabled(true);
		Binder<T> binder = editor.getBinder();
		editor.addSaveListener(ev -> hasChanges = true);

		createColumnAndBinding(binder);

		deleteButtonColumn = addComponentColumn(c -> {
			Button deleteButton = new Button(VaadinIcons.TRASH);
			deleteButton.addClickListener(ev -> deleteConsumer.accept(c));
			return deleteButton;
		});
		deleteButtonColumn.setHidden(true);
	}

	protected abstract void createColumnAndBinding(Binder<T> binder);

	public void setDeleteConsumer(Consumer<T> deleteConsumer) {
		this.deleteConsumer = deleteConsumer;
		if (deleteConsumer != null) {
			deleteButtonColumn.setHidden(false);
		} else {
			deleteButtonColumn.setHidden(true);
		}
	}

	public final void setPerson(Person person) {
		hasChanges = false;
		contactSource.clear();
		if (person != null) {
			contactSource.addAll(readValues(person));
		}
		contactDataProvider.refreshAll();
	}

	protected abstract Collection<? extends T> readValues(Person person);

	public final boolean hasChanges() {
		return hasChanges;
	}

}
