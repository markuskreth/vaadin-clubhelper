package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.util.Collection;

import org.springframework.stereotype.Component;
import org.vaadin.addon.calendar.item.BasicItemProvider;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

/**
 * {@link ClubEvent} provider for vaadin calendar addon.
 * @author markus
 *
 */
@Component
public class ClubEventProvider extends BasicItemProvider<ClubEvent> {

	@Override
	public void setItems(Collection<ClubEvent> items) {
		super.itemList.clear();
		super.setItems(items);
		fireItemSetChanged();
	}

}
