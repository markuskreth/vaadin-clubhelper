package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class MonthlyCalendarSource<T extends CharSequence> implements JRDataSource {

	private final List<Integer> days;
	private final Map<Integer, T> dayContent;
	private int index = -1;
	private int prefix;
	private final Collection<Integer> holidays;

	public MonthlyCalendarSource(YearMonth yearMonthObject, Map<Integer, T> dayContent, Collection<Integer> holidays) {

		days = new ArrayList<>();
		this.dayContent = dayContent;
		this.holidays = holidays;
		prefix = yearMonthObject.atDay(1).getDayOfWeek().getValue() - 1;
		int daysInMonth = yearMonthObject.lengthOfMonth();

		for (int i = 0, limit = daysInMonth + prefix; i < limit; i++) {
			days.add(i + 1);
		}

	}
//
//	public MonthlyCalendarSource(YearMonth yearMonthObject, Map<LocalDate, T> dayContent,
//			Collection<Integer> holidays) {
//		this(yearMonthObject, map(dayContent), holidays);
//	}

	public static <T extends CharSequence> Map<Integer, T> map(Map<LocalDate, T> dayContent) {
		Map<Integer, T> values = new HashMap<>();
		for (Entry<LocalDate, T> entry : dayContent.entrySet()) {
			values.put(entry.getKey().getDayOfMonth(), entry.getValue());
		}
		return values;
	}

	@Override
	public boolean next() throws JRException {

		if (index + 1 >= days.size()) {
			return false;
		}
		index++;
		return true;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		switch (jrField.getName()) {
		case "id":
			return days.get(index);

		case "Field_Value":
			T content = dayContent.get(index - prefix);
			if (content != null && days.get(index) > 0) {
				return content.toString();
			} else {
				return "";
			}
		case "IsHoliday":
			return holidays.contains(index);
		default:
			return "";
		}
	}

	public static MonthlyCalendarSource<String> createTestSource() {
		Map<Integer, String> values = new HashMap<>();
		for (int i = 1; i < 30; i += 3) {
			values.put(i, String.format("Termin am %s.", i));
		}

		List<Integer> holi = Arrays.asList(2, 3, 4, 5, 6);
		return new MonthlyCalendarSource<>(YearMonth.now(), values, holi);
	}
}