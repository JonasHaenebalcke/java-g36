package domein;

import java.time.LocalDate;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDaoJpa;

public class SessieController {

	private Collection<Sessie> sessieList;
	 private Sessie huidigeSessie;

	public SessieController() {

	}

	public void setHuidigeSessie(Sessie sessie) {
		this.huidigeSessie = sessie;
	}
	
	public void wijzigSessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDate startDatum,
			LocalDate eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		throw new UnsupportedOperationException();
	}

	public void voegSessieToe(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDate startDatum,
			LocalDate eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		throw new UnsupportedOperationException();
	}

	public void verwijderSessie(Sessie sessie) {

	}

	public void wijzigIngeschrevenen(Gebruiker ingeschrevenen, boolean ingeschreven, boolean aanwezig) {

	}

	public Sessie geefSessie(int index) {
		return null;
	}

	public List<Sessie> geefSessies(StatusSessie statusSessie) {
		return null;
	}

	public ObservableList<Sessie> geefSessiesObservable(StatusSessie statusSessie) {
		return null;
	}
	 
	public ObservableList<GebruikerSessie> geefGebruikerSessiesObservable() {
		return FXCollections.observableArrayList(huidigeSessie.getGebruikerSessieLijst());
	 }

	public void close() {
		GenericDaoJpa.closePersistency();
	}
}
