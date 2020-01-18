package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.ClubhelperMenuBar;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemState;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu.MenuItemStateFactory;

public class SysteminfoView extends VerticalLayout implements View {

	private final Grid<Entry<String, String>> valueGrid;

	private ListDataProvider<Entry<String, String>> entryDataProvider;

	private ClubhelperMenuBar menuBar;

	private MenuItemStateFactory menuStateFactory;

	public SysteminfoView(boolean mobile, MenuItemStateFactory menuStateFactory) {
		this.menuStateFactory = menuStateFactory;

		valueGrid = new Grid<>();
		valueGrid.addColumn(Entry<String, String>::getKey)
				.setCaption("Schlüssel")
				.setSortable(true)
				.setResizable(true);
		valueGrid.addColumn(Entry<String, String>::getValue)
				.setCaption("Wert")
				.setSortable(false)
				.setResizable(true);

		valueGrid.setSelectionMode(SelectionMode.NONE);
		valueGrid.setSizeFull();

		entryDataProvider = DataProvider.ofCollection(new ArrayList<Entry<String, String>>());
		valueGrid.setDataProvider(entryDataProvider);

		menuBar = new ClubhelperMenuBar(null);
		addComponent(menuBar);

		Label title = new Label("<h1>Systeminformationen</h1>", ContentMode.HTML);
		addComponent(title);
		addComponent(valueGrid);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		MenuItemState state = menuStateFactory.currentState();
		menuBar.applyState(state);

		Collection<Entry<String, String>> items = entryDataProvider.getItems();
		items.clear();
		Locale locale = Locale.getDefault();
		items.add(new SimpleEntry<>("Sprache", locale.toLanguageTag()));

		Map<String, String> environment = System.getenv();
		items.addAll(environment.entrySet());

		Properties systemProperties = System.getProperties();
		items.addAll(systemProperties.entrySet().stream().map(this::mapEntry).collect(Collectors.toList()));
	}

	private Entry<String, String> mapEntry(Entry<Object, Object> item) {
		return new SimpleEntry<>(item.getKey().toString(), item.getValue().toString());
	}
}
