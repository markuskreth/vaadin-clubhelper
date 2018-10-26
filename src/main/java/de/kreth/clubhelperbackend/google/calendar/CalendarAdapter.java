package de.kreth.clubhelperbackend.google.calendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;

import de.kreth.clubhelperbackend.google.GoogleBaseAdapter;
import de.kreth.clubhelperbackend.google.calendar.CalendarResource.CalendarKonfig;

public class CalendarAdapter extends GoogleBaseAdapter {

	com.google.api.services.calendar.Calendar service;
	private Lock lock = new ReentrantLock();
	private CalendarResource res;

	public CalendarAdapter() throws GeneralSecurityException, IOException {
		super();
		res = new CalendarResource();

	}

	@Override
	protected void checkRefreshToken(String serverName) throws IOException {
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)) {
				try {
					super.checkRefreshToken(serverName);
					if (service == null && credential != null) {
						service = new com.google.api.services.calendar.Calendar.Builder(
								HTTP_TRANSPORT, JSON_FACTORY, credential)
										.setApplicationName(APPLICATION_NAME)
										.build();
					}
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			if (log.isWarnEnabled()) {
				log.warn("Lock interrupted", e);
			}
		}
		if (service == null) {
			throw new IllegalStateException(
					"Calendar Service not instanciated!");
		}
	}

	Calendar getCalendarBySummaryName(List<CalendarListEntry> items,
			String calendarSummary) throws IOException {

		String calendarId = null;
		for (CalendarListEntry e : items) {
			if (calendarSummary.equals(e.getSummary())) {
				calendarId = e.getId();
				break;
			}
		}
		if (calendarId == null) {
			throw new IllegalStateException(
					"Calendar " + calendarSummary + " not found!");
		}

		return service.calendars().get(calendarId).execute();
	}

	public List<Event> getAllEvents(String serverName)
			throws IOException, InterruptedException {

		final List<Event> events = new ArrayList<>();

		List<CalendarListEntry> items = getCalendarList(serverName);
		final long oldest = getOldest();

		List<CalendarKonfig> configs = res.getConfigs();
		ExecutorService exec = Executors.newFixedThreadPool(configs.size());
		for (CalendarKonfig c : configs) {
			exec.execute(new FetchEventsRunner(items, c.getName(), events,
					oldest, c.getColor()));
		}

		exec.shutdown();
		try {
			exec.awaitTermination(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Thread terminated - event list may be incomplete.", e);
		}
		return events;
	}

	private long getOldest() {
		GregorianCalendar oldestCal = new GregorianCalendar();
		oldestCal.set(java.util.Calendar.DAY_OF_MONTH, 1);
		oldestCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
		oldestCal.set(java.util.Calendar.MINUTE, 0);
		oldestCal.add(java.util.Calendar.MONTH, -1);
		oldestCal.add(java.util.Calendar.YEAR, -1);
		oldestCal.add(java.util.Calendar.HOUR_OF_DAY, -1);

		return oldestCal.getTimeInMillis();
	}

	List<CalendarListEntry> getCalendarList(String serverName)
			throws IOException {
		checkRefreshToken(serverName);
		CalendarList calendarList;
		try {
			calendarList = service.calendarList().list().execute();
		} catch (IOException e) {
			if (log.isWarnEnabled()) {
				log.warn("Error fetching Calendar List, trying token refresh",
						e);
			}
			credential.refreshToken();
			if (log.isInfoEnabled()) {
				log.info("Successfully refreshed Google Security Token.");
			}
			calendarList = service.calendarList().list().execute();
		}
		return calendarList.getItems();
	}

	private final class FetchEventsRunner implements Runnable {
		private final List<Event> events;
		private final long oldest;
		private String colorClass;
		private List<CalendarListEntry> items;
		private String summary;

		private FetchEventsRunner(List<CalendarListEntry> items, String summary,
				List<Event> events, long oldest, String colorClass) {
			this.events = events;
			this.oldest = oldest;
			this.items = items;
			this.summary = summary;
			this.colorClass = colorClass;
		}

		@Override
		public void run() {

			try {
				log.debug("Fetching events of calendar \"{}\"", summary);
				Calendar calendar = getCalendarBySummaryName(items, summary);
				DateTime timeMin = new DateTime(oldest);
				List<Event> eventItems = service.events().list(calendar.getId())
						.setTimeMin(timeMin).execute().getItems();
				eventItems.forEach(item -> item.set("colorClass", colorClass));
				events.addAll(eventItems);
				log.debug("Added {} Events for \"{}\"", eventItems.size(), summary);

			} catch (IOException e) {
				log.error("Unable to fetch Events from {}", summary, e);
			}
		}
	}

	static Event createDefaultEvent(String summary) {
		Event ev = new Event().setGuestsCanInviteOthers(false)
				.setGuestsCanModify(false).setGuestsCanSeeOtherGuests(true)
				.setSummary(summary);
		List<EventAttendee> attendees = new ArrayList<>();
		ev.setAttendees(attendees);
		return ev;
	}

}
