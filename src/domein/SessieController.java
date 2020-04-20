package domein;

import java.time.LocalDate;
import java.util.*;

import javafx.collections.ObservableList;
import repository.GenericDaoJpa;

public class SessieController {

	private Collection<Sessie> sessieList;

	public SessieController() {

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

	public void close() {
		GenericDaoJpa.closePersistency();
	}
}
