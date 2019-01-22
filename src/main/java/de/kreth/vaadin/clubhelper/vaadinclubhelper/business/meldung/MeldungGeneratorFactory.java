package de.kreth.vaadin.clubhelper.vaadinclubhelper.business.meldung;

import de.kreth.vaadin.clubhelper.vaadinclubhelper.data.CompetitionType;

public class MeldungGeneratorFactory {

	public static MeldungGenerator forType(CompetitionType.Type type) {
		switch (type) {
		case DOPPELMINI:
//			break;
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
