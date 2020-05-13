package domein;

import java.time.LocalDateTime;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class SessieController {

	private List<Sessie> sessieLijst;
	private Sessie huidigeSessie;
	private GebruikerController gc;
	private ObservableList<Sessie> sessieObservableList;
	private GenericDao<Sessie> sessieRepo;
	private FilteredList<Sessie> sessieFilteredLijst;

	public SessieController() {
		gc = new GebruikerController();
		setSessieRepo(new GenericDaoJpa(Sessie.class));
	}

	public SessieController(GebruikerController gc) {
		this.gc = gc;
		setSessieRepo(new GenericDaoJpa(Sessie.class));
	}

	public void setSessieRepo(GenericDao mock) {
		sessieRepo = mock;
	}

	public List<Sessie> geefSessies() {
		if (sessieLijst == null) {
			sessieLijst = sessieRepo.findAll();
		}
		return sessieLijst;
	}

	public ObservableList<Sessie> geefSessiesObservable() {
		if (sessieObservableList == null) {
			sessieObservableList = FXCollections.observableArrayList(geefSessies());
		}
		return sessieObservableList;
	}

	public FilteredList<Sessie> geefSessiesFiltered() {
		if (sessieFilteredLijst == null) {
			sessieFilteredLijst = new FilteredList<>(geefSessiesObservable(), p -> true);
		}
		return sessieFilteredLijst;
	}

	public ObservableList<GebruikerSessie> geefGebruikerSessiesObservable() {
		return FXCollections.observableArrayList(huidigeSessie.getGebruikerSessieLijst());
	}

	public void changeFilter(String filter, String status) {
		sessieFilteredLijst.setPredicate(sessie -> {
//			if ((filter == null || filter.isBlank()) && (status.contentEquals("Alle") || status == null || status.isBlank()))
//				return true;
			String lowercase = filter.toLowerCase();
			boolean filterbool = (filter == null || filter.isBlank()) ? true
					: (sessie.getTitel().toLowerCase().contains(lowercase)
							|| sessie.getVerantwoordelijke().getVoornaam().toLowerCase().contains(lowercase)
							|| sessie.getVerantwoordelijke().getFamilienaam().toLowerCase().contains(lowercase)
							|| sessie.getGastspreker().toLowerCase().contains(lowercase)
							|| sessie.getStartDatum().toString().toLowerCase().contains(lowercase));

			boolean statusbool = status.contentEquals("Alle") || status == null || status.isBlank() ? true
					: sessie.getStatusSessie().toString() == status;
			return (filterbool && statusbool);
		});
	}

	public void setHuidigeSessie(Sessie sessie) {
		this.huidigeSessie = sessie;
	}

	public void wijzigSessie(String titel, String lokaal, LocalDateTime startDatum, LocalDateTime eindDatum,
			int capaciteit, String omschrijving, String gastspreker, boolean open) {
		huidigeSessie.wijzigSessie(titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker, open);
		try {
			GenericDaoJpa.startTransaction();
			sessieRepo.update(huidigeSessie);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IllegalArgumentException("Er ging iets mis bij het opslaan van de gewijzigde sessie.");
		}
	}

	public void voegSessieToe(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		Sessie sessie = new Sessie(verantwoordelijke, titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving,
				gastspreker);
		try {
			setHuidigeSessie(sessie);
			sessieLijst.add(sessie);
			sessieObservableList.add(sessie);
			GenericDaoJpa.startTransaction();
			sessieRepo.insert(sessie);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IllegalArgumentException("Er ging iets mis bij het opslaan van de nieuwe sessie");
		}
	}

	public void verwijderHuidigeSessie() {
		Sessie sessie = huidigeSessie;
		if (sessie.getStatusSessie() != StatusSessie.nietOpen)
			throw new IllegalArgumentException("Sessie kan niet worden verwijderd want deze is al opengesteld.");
		try {
			sessieLijst.remove(sessie);
			sessieObservableList.remove(sessie);
			setHuidigeSessie(null);
			GenericDaoJpa.startTransaction();
			sessieRepo.delete(sessie);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IllegalArgumentException("Er ging iets mis bij het verwijderen van de sessie");
		}
	}

	public void wijzigIngeschrevenen(Gebruiker ingeschrevene, boolean ingeschreven, boolean aanwezig) {
		huidigeSessie.wijzigIngeschrevenen(ingeschrevene, ingeschreven, aanwezig);
	}

	public void addFeedback(Gebruiker auteur, String content, int score) {
		huidigeSessie.addFeedback(auteur, content, score);
	}

	public void wijzigFeedback(int feedbackID, String content, int score) {
		huidigeSessie.wijzigFeedback(feedbackID, content, score);
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}
}
