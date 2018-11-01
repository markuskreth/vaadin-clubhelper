package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.BackwardHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.DateClickHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ForwardHandler;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickHandler;

import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.ClubEvent;

public class CalendarComponent extends CustomComponent {

	private static final long serialVersionUID = -9152173211931554059L;

	private transient DateTimeFormatter dfMonth = DateTimeFormatter.ofPattern("MMMM uu");

	private Label monthName;

	private ClubEventProvider dataProvider;
	private Calendar<ClubEvent> calendar;

	public CalendarComponent() {

		monthName = new Label();
		monthName.setStyleName("title_label");

		Button popupButton = new Button("Menu");
		popupButton.addClickListener(ev -> openPopupMenu(ev));
		
		HorizontalLayout head = new HorizontalLayout(monthName, popupButton);
		
		dataProvider = new ClubEventProvider();
		calendar = new Calendar<>(dataProvider)
				.withMonth(Month.from(LocalDateTime.now()));
		calendar.setCaption("Events");
		calendar.setSizeFull();

		updateMonthText(calendar.getStartDate());

		VerticalLayout layout = new VerticalLayout(head, calendar);
		layout.setSizeFull();
		setCompositionRoot(layout);
	}

	private void openPopupMenu(ClickEvent ev) {
		ContextMenu contextMenu = new ContextMenu(ev.getButton(), true);
		contextMenu.addItem("Export", ev1 -> calendarExport(ev1));
		contextMenu.open(210, 40);
	}

	private void calendarExport(MenuItem ev1) {
		Notification.show("Do Export");
	}

	private void updateMonthText(ZonedDateTime startDate) {
		monthName.setValue(dfMonth.format(startDate));
	}

	public Registration setHandler(ForwardHandler listener) {
		return calendar.setHandler(listener);
	}

	public Registration setHandler(BackwardHandler listener) {
		return calendar.setHandler(listener);
	}

	public Registration setHandler(DateClickHandler listener) {
		return calendar.setHandler(listener);
	}

	public Registration setHandler(ItemClickHandler listener) {
		return calendar.setHandler(listener);
	}

	public void setItems(Collection<ClubEvent> items) {
		dataProvider.setItems(items);
		calendar.markAsDirty();
	}

	static class ClubEventProvider extends BasicItemProvider<ClubEvent> {

		private static final long serialVersionUID = -5415397258827236704L;

		@Override
		public void setItems(Collection<ClubEvent> items) {
			super.setItems(items);
			fireItemSetChanged();
		}

	}

}
