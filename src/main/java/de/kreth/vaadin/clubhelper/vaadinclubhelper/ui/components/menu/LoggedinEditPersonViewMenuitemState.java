package de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.components.menu;

import java.time.ZonedDateTime;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.ApplicationContext;

import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.dao.GroupDao;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.GroupDef;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.Person;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.commands.AddPersonCommand;
import de.kreth.vaadin.clubhelper.vaadinclubhelper.ui.navigation.PersonEditView;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Status für authentifizierte User im {@link PersonEditView}
 * @author markus
 *
 */
class LoggedinEditPersonViewMenuitemState extends LoggedinMenuitemState {

	private final Consumer<Person> newPersonConsumer;

	private GroupDao groupDao;

	public LoggedinEditPersonViewMenuitemState(ApplicationContext context, UI ui, Supplier<ZonedDateTime> startProvider,
			Supplier<ZonedDateTime> endProvider, BiConsumer<String, JasperPrint> printConsumer,
			Consumer<Person> newPersonConsumer) {
		super(context, ui, startProvider, endProvider, printConsumer);
		this.newPersonConsumer = newPersonConsumer;
		groupDao = context.getBean(GroupDao.class);
	}

	@Override
	public void applyMenuStates(ClubhelperMenuBar menuBar) {
		super.applyMenuStates(menuBar);

		MenuItem editMenu = menuBar.getEditMenuItem();

		GroupDef defaultGroup = groupDao.get(1);
		editMenu.addSeparator();
		AddPersonCommand addPersonCommand = new AddPersonCommand(newPersonConsumer, defaultGroup);
		editMenu.addItem(addPersonCommand.getLabel(), new CommandWrapper(addPersonCommand));
	}
}
