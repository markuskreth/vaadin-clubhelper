package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.Editor;
import com.vaadin.ui.components.grid.EditorCancelEvent;
import com.vaadin.ui.components.grid.EditorCancelListener;
import com.vaadin.ui.renderers.AbstractRenderer;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;

public abstract class AbstractDataGrid<T> extends VerticalLayout {

	private static final long serialVersionUID = -3404971410481135696L;

	private final Grid<T> grid;

	private final List<T> source;

	private final ListDataProvider<T> dataProvider;

	private boolean hasChanges;

	private Column<T, Component> deleteButtonColumn;

	private Consumer<T> deleteConsumer;

	private final List<Consumer<T>> successConsumers;

	private final HorizontalLayout buttonLayout;

	private final Button addButton;

	protected AbstractDataGrid() {
		this.grid = new Grid<>();
		this.source = new ArrayList<>();
		successConsumers = new ArrayList<>();
		this.dataProvider = DataProvider.ofCollection(source);
		grid.setDataProvider(dataProvider);

		EditedListener editedListener = new EditedListener();

		Editor<T> editor = grid.getEditor();
		editor.setEnabled(true);
		editor.addOpenListener(event -> grid.getEditor().getBinder().setBean(event.getBean()));
		grid.addSelectionListener(ev -> editedListener.editObject = null);
		Binder<T> binder = editor.getBinder();
		editor.addSaveListener(ev -> {
			hasChanges = true;
			if (editedListener.editObject != null) {
				for (Consumer<T> consumer : successConsumers) {
					consumer.accept(ev.getBean());
				}
			}
			dataProvider.refreshAll();
		});
		editor.addCancelListener(editedListener);

		createColumnAndBinding(binder);

		binder.withValidator((obj, context) -> validate(obj, context));

		deleteButtonColumn = grid.addComponentColumn(c -> {
			Button deleteButton = new Button(VaadinIcons.TRASH);
			deleteButton.addClickListener(ev -> deleteConsumer.accept(c));
			return deleteButton;
		});
		deleteButtonColumn.setSortable(false);
		deleteButtonColumn.setHidden(true);

		buttonLayout = new HorizontalLayout();
		addButton = new Button("Hinzufügen");
		addButton.addClickListener(ev -> {
			T newItem = createNewItem();
			source.add(newItem);
			editedListener.editObject = newItem;
			dataProvider.refreshAll();
			grid.getEditor().editRow(source.size() - 1);
		});
		buttonLayout.addComponent(addButton);

		addComponents(buttonLayout, grid);
	}

	protected abstract ValidationResult validate(T obj, ValueContext context);

	public void addSuccessConsumer(Consumer<T> consumer) {
		this.successConsumers.add(consumer);
	}

	protected abstract T createNewItem();

	protected abstract void createColumnAndBinding(Binder<T> binder);

	protected void setEditorEnabled(boolean visible) {
		grid.getEditor().setEnabled(visible);
		buttonLayout.setVisible(visible);
	}

	public void setDeleteConsumer(Consumer<T> deleteConsumer) {
		this.deleteConsumer = deleteConsumer;
		if (deleteConsumer != null) {
			deleteButtonColumn.setHidden(false);
		}
		else {
			deleteButtonColumn.setHidden(true);
		}
	}

	public final void setPerson(Person person) {
		hasChanges = false;
		source.clear();
		if (person != null) {
			source.addAll(readValues(person));
		}
		dataProvider.refreshAll();
	}

	protected abstract Collection<? extends T> readValues(Person person);

	public final boolean hasChanges() {
		return hasChanges;
	}

	class EditedListener implements EditorCancelListener<T> {

		private static final long serialVersionUID = -884798775149504135L;

		private T editObject;

		@Override
		public void onEditorCancel(EditorCancelEvent<T> event) {
			if (editObject != null) {
				source.remove(editObject);
				dataProvider.refreshAll();
			}
		}

	}

	public Column<T, ?> addColumn(String propertyName) {
		return grid.addColumn(propertyName);
	}

	public Column<T, ?> addColumn(String propertyName, AbstractRenderer<? super T, ?> renderer) {
		return grid.addColumn(propertyName, renderer);
	}

	public <V> Column<T, V> addColumn(ValueProvider<T, V> valueProvider) {
		return grid.addColumn(valueProvider);
	}

	public <V> Column<T, V> addColumn(ValueProvider<T, V> valueProvider,
			AbstractRenderer<? super T, ? super V> renderer) {
		return grid.addColumn(valueProvider, renderer);
	}

	public <V> Column<T, V> addColumn(ValueProvider<T, V> valueProvider,
			ValueProvider<V, String> presentationProvider) {
		return grid.addColumn(valueProvider, presentationProvider);
	}

	public <V, P> Column<T, V> addColumn(ValueProvider<T, V> valueProvider, ValueProvider<V, P> presentationProvider,
			AbstractRenderer<? super T, ? super P> renderer) {
		return grid.addColumn(valueProvider, presentationProvider, renderer);
	}

	public <V extends Component> Column<T, V> addComponentColumn(ValueProvider<T, V> componentProvider) {
		return grid.addComponentColumn(componentProvider);
	}

}
