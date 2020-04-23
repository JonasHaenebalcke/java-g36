package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sessie {

	private StatusSessie statusSessie;
	private Gebruiker verantwoordelijke;
	private List<GebruikerSessie> gebruikerSessieLijst;
	private String titel;
	private String lokaal;
	private LocalDate eindDatum;
	private LocalDate startDatum;
	private int capaciteit;
	private String omschrijving;
	private String gastspreker;

	protected Sessie() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
	}

	public Sessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDate startDatum, LocalDate eindDatum,
			int capaciteit, String omschrijving, String gastspreker) {
		this();
		setVerantwoordelijke(verantwoordelijke);
		setTitel(titel);
		setLokaal(lokaal);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setCapaciteit(capaciteit);
		this.omschrijving = omschrijving;
		this.gastspreker = gastspreker;
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

	public LocalDate getEindDatum() {
		return eindDatum;
	}

	public LocalDate getStartDatum() {
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
		if(titel.isBlank() || titel == null)
			throw new IllegalArgumentException("Titel kan moet ingevuld zijn.");
		this.titel = titel;
	}

	private void setLokaal(String lokaal) {
		if(titel.isBlank() || titel == null)
			throw new IllegalArgumentException("Titel kan moet ingevuld zijn.");
		this.lokaal = lokaal;
	}

	private void setStartDatum(LocalDate startDatum) {
		
		this.startDatum = startDatum;
	}

	private void setEindDatum(LocalDate eindDatum) {
		this.eindDatum = eindDatum;
	}

	private void setCapaciteit(int capaciteit) {
		this.capaciteit = capaciteit;
	}

	private void setVerantwoordelijke(Gebruiker verantwoordelijke) {
		if(titel.isBlank() || titel == null)
			throw new IllegalArgumentException("Titel kan moet ingevuld zijn.");
		this.verantwoordelijke = verantwoordelijke;
	}

	public void wijzigSessie(String titel, String lokaal, LocalDate startDatum, LocalDate eindDatum, int capaciteit,
			String omschrijving, String gastspreker) {
		setTitel(titel);
		setLokaal(lokaal);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setCapaciteit(capaciteit);
		this.omschrijving = omschrijving;
		this.gastspreker = gastspreker;
	}

	public void wijzigIngeschrevenen(Gebruiker ingeschrevenen, boolean ingeschreven, boolean aanwezig) {
		throw new UnsupportedOperationException();
	}
}
