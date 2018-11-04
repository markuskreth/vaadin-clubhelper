package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.calendar.Year;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperPrint;

public class YearlyCalendarCreator extends CalendarCreator {

	private Year year;

	public YearlyCalendarCreator(int year) {
		this.year = new Year(year);
	}

	@Override
	protected JRDataSource getSource() {
		return new EmptySource();
	}

	@Override
	protected void fillParameterMap(Map<String, Object> parameters) {
		parameters.put("Year", year);
	}

	@Override
	protected InputStream jrxmlResource() {
		return CalendarCreator.class.getResourceAsStream("/jasper/calendar_year.jrxml");
	}

	private static class EmptySource implements JRDataSource {

		@Override
		public boolean next() throws JRException {
			return false;
		}

		@Override
		public Object getFieldValue(JRField jrField) throws JRException {
			return null;
		}
		
	}
}
