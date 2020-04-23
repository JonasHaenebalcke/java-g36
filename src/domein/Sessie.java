package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Sessie {

	private StatusSessie statusSessie;
	private Gebruiker verantwoordelijke;
	private List<GebruikerSessie> gebruikerSessieLijst;
	private String titel;
	private String lokaal;
	private LocalDateTime eindDatum;
	private LocalDateTime startDatum;
	private int capaciteit;
	private String omschrijving;
	private String gastspreker;

	private boolean open;

	protected Sessie() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
		setOpen(false);
	}

	public Sessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		this();
		setVerantwoordelijke(verantwoordelijke);
//		setTitel(titel);
//		setLokaal(lokaal);
//		setStartDatum(startDatum);
//		setEindDatum(eindDatum);
//		setCapaciteit(capaciteit);
//		this.omschrijving = omschrijving;
//		this.gastspreker = gastspreker;
		wijzigSessie(titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker, open);
	}

	private void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getOpen() {
		return open;
	}

	public StatusSessie getStatusSessie() {
		return statusSessie;
	}

	public Gebruiker getVerantwoordelijke() {
		return verantwoordelijke;
	}

	public List<GebruikerSessie> getGebruikerSessieLijst() {
		return gebruikerSessieLijst;
	}

	public String getTitel() {
		return titel;
	}

	public String getLokaal() {
		return lokaal;
	}

	public LocalDateTime getEindDatum() {
		return eindDatum;
	}

	public LocalDateTime getStartDatum() {
		return startDatum;
	}

	public int getCapaciteit() {
		return capaciteit;
	}

	public String getOmschrijving() {
		return omschrijving;
	}

	public String getGastspreker() {
		return gastspreker;
	}

	private void setTitel(String titel) {
		if (titel.isBlank() || titel == null)
			throw new IllegalArgumentException("Titel moet ingevuld zijn.");
		this.titel = titel;
	}

	private void setLokaal(String lokaal) {
		if (lokaal.isBlank() || lokaal == null)
			throw new IllegalArgumentException("Lokaal moet ingevuld zijn.");
		this.lokaal = lokaal;
	}

	private void setStartDatum(LocalDateTime startDatum) {
//		this.startDatum = startDatum;
		if (eindDatum == null) {
			if (startDatum.isAfter(LocalDateTime.now().plusDays(1)))
				this.startDatum = startDatum;
			else
				throw new IllegalArgumentException("De startdatum moet in de toekomst liggen.");
		} else {
			if (
//					ChronoUnit.MINUTES.between(startDatum, eindDatum) >= 30 &&
			(startDatum.isAfter(LocalDateTime.now()) || eindDatum.isBefore(LocalDateTime.now())))
				this.startDatum = startDatum;
			else
				throw new IllegalArgumentException(
						"De startdatum en einddatum moeten beiden in het verleden of in de toekomst liggen.");
		}
	}

	private void setEindDatum(LocalDateTime eindDatum) {
//		this.eindDatum = eindDatum;
		if (eindDatum.isAfter(startDatum))
			this.eindDatum = eindDatum;
		else
			throw new IllegalArgumentException("De einddatum moet na de startdatum liggen.");
	}

	private void setCapaciteit(int capaciteit) {
		this.capaciteit = capaciteit;
	}

	private void setVerantwoordelijke(Gebruiker verantwoordelijke) {
		if (verantwoordelijke == null)
			throw new IllegalArgumentException("Verantwoordelijke moet geselecteerd zijn.");
		this.verantwoordelijke = verantwoordelijke;
	}

	public void wijzigSessie(String titel, String lokaal, LocalDateTime startDatum, LocalDateTime eindDatum,
			int capaciteit, String omschrijving, String gastspreker, boolean open) {
		if (!this.open)
			throw new IllegalArgumentException("De sessie mag niet geopend zijn om deze te kunnen wijzigen.");
		if (ChronoUnit.MINUTES.between(startDatum, eindDatum) < 30)
			throw new IllegalArgumentException("De sessie moet een minimumperiode van 30 minuten hebben.");

		setTitel(titel);
		setLokaal(lokaal);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setCapaciteit(capaciteit);
		this.omschrijving = omschrijving;
		this.gastspreker = gastspreker;
		setOpen(open);
	}

	public void wijzigIngeschrevenen(Gebruiker ingeschrevene, boolean ingeschreven, boolean aanwezig) {
		if (!open)
			throw new IllegalArgumentException("Deze sessie is nog niet open voor inschrijvingen");

		boolean gebruikerGevonden = false;
		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
			if (gebruikerSessie.getIngeschrevene().equals(ingeschrevene)) {
				gebruikerGevonden = true;
				if (ingeschreven)
					gebruikerSessie.wijzigAanwezigheid(aanwezig);
				else
					gebruikerSessieLijst.remove(gebruikerSessie);
				break;
			}
		}
		if (!gebruikerGevonden) {
			if (ingeschreven) {
				GebruikerSessie gebruikerSessie = new GebruikerSessie(this, ingeschrevene);
				gebruikerSessie.wijzigAanwezigheid(aanwezig);
				gebruikerSessieLijst.add(gebruikerSessie);
			} else
				throw new IllegalArgumentException("Gebruiker is al uitgeschreven voor deze sessie");
		}

	}
}
