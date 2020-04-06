package domein;

import java.time.LocalDate;

public class Sessie {

	private StatusSessie statusSessie;

	protected Sessie() {
		throw new UnsupportedOperationException();
	}

	public Sessie(Gebruiker verantwoordelijke, LocalDate startDatum, LocalDate eindDatum, String titel, String lokaal,
			int capaciteit, StatusSessie statusSessie, String omschrijving, String gastSpreker) {
		throw new UnsupportedOperationException();
	}

	public void operation() {
		throw new UnsupportedOperationException();
	}
}
