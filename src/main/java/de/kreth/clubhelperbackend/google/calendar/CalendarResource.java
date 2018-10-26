package de.kreth.clubhelperbackend.google.calendar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class CalendarResource {

	private Map<String, CalendarKonfig> configs;

	public CalendarResource() throws IOException {
		configs = new HashMap<>();

		InputStream resStream = getClass()
				.getResourceAsStream("/calendars.properties");
		Properties prop = new Properties();
		prop.load(resStream);

		Enumeration<Object> keys = prop.keys();
		String className = getClass().getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));

		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			String name = key.substring(packageName.length());
			String value = prop.getProperty(key);

			StringTokenizer tok = new StringTokenizer(name, ".");
			name = tok.nextToken();
			String type = tok.nextToken();
			CalendarKonfig conf;

			if (configs.containsKey(name)) {
				conf = configs.get(name);
			} else {
				conf = new CalendarKonfig(null, null);
				configs.put(name, conf);
			}
			switch (type) {
				case "name" :
					conf.name = value;
					break;

				case "color" :
					conf.color = value;

				default :
					break;
			}
		}
	}

	public List<CalendarKonfig> getConfigs() {
		return new ArrayList<>(configs.values());
	}

	public static class CalendarKonfig {
		private String name;
		private String color;

		CalendarKonfig(String name, String color) {
			super();
			this.name = name;
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public String getColor() {
			return color;
		}
	}
}
