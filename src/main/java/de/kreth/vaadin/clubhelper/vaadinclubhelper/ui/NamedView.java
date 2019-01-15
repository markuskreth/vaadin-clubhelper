package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui;

import com.vaadin.navigator.View;

public interface NamedView extends View {
	/**
	 * Navigation view name used for this view.
	 * 
	 * @return view name.
	 */
	String getViewName();
}
