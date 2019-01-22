package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.ui.NumberField;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.Editor;
import com.vaadin.ui.components.grid.EditorSaveEvent;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.business.EventBusiness;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.PflichtenDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Altersgruppe;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType.Type;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Pflicht;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.events.DataUpdatedEvent;

public class EventAltersgruppen extends VerticalLayout implements DataUpdatedEvent {

	private static final long serialVersionUID = -7777374233838542085L;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final int OLDEST_YEAR = 1900;
	private static final int CURRENT_YEAR = LocalDateTime.now().getYear();

	private final Grid<Altersgruppe> gruppen;
	private final ListDataProvider<Altersgruppe> provider;
	private final Binder<Altersgruppe> binder;
	private final EventBusiness eventBusiness;
	private Column<Altersgruppe, Pflicht> pflichtColumn;

	public EventAltersgruppen(PflichtenDao pflichtenDao, EventBusiness eventBusiness) {

		setCaption("Altersklassen");
		this.eventBusiness = eventBusiness;
		Button addButton = new Button("Hinzufügen");
		addButton.addClickListener(ev -> addGruppe());

		HorizontalLayout buttonLayout = new HorizontalLayout(addButton);

		provider = DataProvider.ofCollection(eventBusiness.getCurrent().getAltersgruppen());

		gruppen = new Grid<>();
		gruppen.setDataProvider(provider);
		binder = gruppen.getEditor().getBinder();

		setupGrid(pflichtenDao);

		addComponents(buttonLayout, gruppen);
	}

	void setupGrid(PflichtenDao pflichtenDao) {

		Binding<Altersgruppe, Pflicht> pflichtBinding = binder.bind(createPflichtenCombo(pflichtenDao.listAll()),
				Altersgruppe::getPflicht, Altersgruppe::setPflicht);
		Binding<Altersgruppe, String> bezBinding = binder.bind(createBezeichnungField("Altersgruppe.Bezeichnung"),
				Altersgruppe::getBezeichnung, Altersgruppe::setBezeichnung);

		IntNumberField vonField = new IntNumberField("Von", "Altersgruppe.Start");
		Binding<Altersgruppe, Integer> bindingStart = binder.forField(vonField).withValidator(new YearValidator())
				.bind(Altersgruppe::getStart, Altersgruppe::setStart);
		Binding<Altersgruppe, Integer> bindingEnd = binder.forField(new IntNumberField("Bis", "Altersgruppe.End"))
				.withValidator(new BisYearValidator(vonField)).bind(Altersgruppe::getEnd, Altersgruppe::setEnd);
		vonField.addValueChangeListener(ev -> bindingEnd.validate());

		gruppen.addColumn(Altersgruppe::getBezeichnung).setCaption("Bezeichnung").setEditorBinding(bezBinding);
		gruppen.addColumn(Altersgruppe::getStart).setCaption("Von").setEditorBinding(bindingStart);
		gruppen.addColumn(Altersgruppe::getEnd).setCaption("Bis").setEditorBinding(bindingEnd);
		pflichtColumn = gruppen.addColumn(Altersgruppe::getPflicht).setCaption("Pflicht")
				.setEditorBinding(pflichtBinding);
		updateFinisched();

		binder.addValueChangeListener(this::valueChange);
		gruppen.addSelectionListener(this::selectionChange);

		Editor<Altersgruppe> editor = gruppen.getEditor();
		editor.setEnabled(true);

		editor.addSaveListener(ev -> gridRowSaved(ev));
		gruppen.setSelectionMode(SelectionMode.SINGLE);
	}

	private void gridRowSaved(EditorSaveEvent<Altersgruppe> ev) {
		binder.validate();
		Altersgruppe bean = ev.getBean();
		if (binder.writeBeanIfValid(bean)) {
			validateAndStore(bean);
		}
		provider.refreshAll();
	}

	void selectionChange(SelectionEvent<Altersgruppe> event) {
		Optional<Altersgruppe> selected = event.getFirstSelectedItem();
		if (selected.isPresent()) {
			binder.setBean(selected.get());
		} else {
			binder.setBean(null);
		}
		provider.refreshAll();
	}

	void valueChange(ValueChangeEvent<Object> event) {

		Component component = event.getComponent();
		Object value = event.getValue();
		Object oldValue = event.getOldValue();
		String componentId = component.getId();

		LOGGER.trace("Changed: {}, value={}, old={}", componentId, value, oldValue);
	}

	public void updateData() {
		Collection<Altersgruppe> items = provider.getItems();
		items.clear();
		ClubEvent current = eventBusiness.getCurrent();
		Set<Altersgruppe> altersgruppen = current.getAltersgruppen();
		items.addAll(altersgruppen);
	}

	public void validateAndStore(Altersgruppe edited) {
		if (edited == null) {
			return;
		}
		if (edited.getBezeichnung() != null && !edited.getBezeichnung().isBlank() && edited.getPflicht() != null
				&& edited.getClubEvent() != null) {
			eventBusiness.storeAltersgruppe(edited);
			LOGGER.info("Stored: {}", edited);
		}
	}

	TextField createBezeichnungField(String id) {
		TextField textField = new TextField();
		textField.setId(id);
		return textField;
	}

	ComboBox<Pflicht> createPflichtenCombo(List<Pflicht> pflichtenList) {
		ComboBox<Pflicht> pflichten = new ComboBox<>();
		pflichten.setId("Altersgruppe.Pflicht");
		pflichten.setItems(pflichtenList);
		return pflichten;
	}

	private void addGruppe() {

		Altersgruppe e = eventBusiness.createAltersgruppe();
		provider.getItems().add(e);
		binder.setBean(e);
		provider.refreshAll();
	}

	private static class YearValidator extends AbstractValidator<Integer> {

		private static final long serialVersionUID = 450137530250464249L;
		private static final String ERROR_MESSAGE = String.format("Jahreszahl muss zwischen %d und %d liegen",
				OLDEST_YEAR, CURRENT_YEAR);

		protected YearValidator() {
			super(ERROR_MESSAGE);
		}

		@Override
		public ValidationResult apply(Integer value, ValueContext context) {
			if (value >= OLDEST_YEAR && value <= CURRENT_YEAR) {
				return ValidationResult.ok();
			}

			return ValidationResult.error(ERROR_MESSAGE);
		}

	}

	private static class BisYearValidator extends YearValidator {

		private static final long serialVersionUID = -7197197454502399416L;
		final IntNumberField vonField;

		public BisYearValidator(IntNumberField vonField) {
			super();
			this.vonField = vonField;
		}

		@Override
		public ValidationResult apply(Integer value, ValueContext context) {
			if (value > vonField.getValue()) {
				return super.apply(value, context);
			} else {
				return ValidationResult.error("Bis Wert muss größer als Von Wert sein!");
			}
		}
	}

	private class IntNumberField extends CustomField<Integer> {

		private static final long serialVersionUID = 2221967167572584942L;
		private final NumberField field;

		public IntNumberField(String caption, String id) {
			field = new NumberField();
			field.setDecimalAllowed(false);
			field.setDecimalSeparatorAlwaysShown(false);
			field.setGroupingUsed(false);
			field.setNegativeAllowed(false);
			field.setPlaceholder(String.format("Jahr zwischen 1900 und %d", CURRENT_YEAR));
			field.setValueChangeMode(ValueChangeMode.LAZY);
			field.setValueChangeTimeout(300);
			field.setMinValue(OLDEST_YEAR);
			field.setMaxValue(CURRENT_YEAR);
			field.setCaption(caption);
			field.setId(id);
		}

		@Override
		public Integer getValue() {
			String value = field.getValue();
			if (value == null || value.isBlank()) {
				return Integer.valueOf(0);
			}
			return Double.valueOf(value).intValue();
		}

		@Override
		protected Component initContent() {
			return field;
		}

		@Override
		protected void doSetValue(Integer value) {
			field.setValue(value.doubleValue());
		}

	}

	@Override
	public void updateFinisched() {
		Type type = currentType();
		pflichtColumn.setHidden(Type.DOPPELMINI == type);
	}

	public Type currentType() {
		ClubEvent current = eventBusiness.getCurrent();
		CompetitionType competitionType = current.getCompetitionType();
		if (competitionType == null) {
			return null;
		}
		Type type = competitionType.getType();
		return type;
	}
}
