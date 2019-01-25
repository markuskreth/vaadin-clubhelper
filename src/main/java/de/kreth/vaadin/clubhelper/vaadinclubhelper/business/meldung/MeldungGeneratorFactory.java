package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;

public class MeldungGeneratorFactory {

	public static MeldungGenerator forType(CompetitionType.Type type) {
		if (type == null) {
			return new MeldungDmtWettkampfGenerator(); // no Pflichten printed
		}
		switch (type) {
		case DOPPELMINI:
			return new MeldungDmtWettkampfGenerator();
		case LIGA:
//			break;
		case MANNSCHAFT:
//			break;
		case SYNCHRON:
//			break;
		case EINZEL:
		default:
			return new MeldungEinzelWettkampfGenerator();

		}
	}
}
