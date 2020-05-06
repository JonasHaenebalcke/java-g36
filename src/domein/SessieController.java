package domein;

import java.time.LocalDateTime;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class SessieController {

	private List<Sessie> sessieLijst;
	private Sessie huidigeSessie;
	private GebruikerController gc;
	private ObservableList<Sessie> sessieObservableList;
	private ObservableList<GebruikerSessie> gebruikerSessieObservableList;
	private GenericDao<Sessie> sessieRepo;

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
			sessieLijst.forEach(sessie -> sessie.setStringProperties());
		}
		return sessieLijst;
	}

	public ObservableList<Sessie> geefSessiesObservable() {
		if (sessieObservableList == null) {
			sessieObservableList = FXCollections.observableArrayList(geefSessies());

		}
		System.out.println(sessieObservableList);
		return sessieObservableList;
	}

	public ObservableList<GebruikerSessie> geefGebruikerSessiesObservable() {
		return FXCollections.observableArrayList(huidigeSessie.getGebruikerSessieLijst());
	}

	public void setHuidigeSessie(Sessie sessie) {
		this.huidigeSessie = sessie;
	}

	public void wijzigSessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker, boolean open) {
//		try {
			GenericDaoJpa.startTransaction();

			huidigeSessie.wijzigSessie(titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker,
					open);

			sessieRepo.update(huidigeSessie);
			GenericDaoJpa.commitTransaction();
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			throw new IllegalArgumentException("Er ging iets mis bij het opslaan van de gewijzigde sessie.");
//		}
	}

	public void voegSessieToe(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		Sessie sessie = new Sessie(verantwoordelijke, titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving,
				gastspreker);
		try {
			GenericDaoJpa.startTransaction();
			setHuidigeSessie(sessie);
			sessieLijst.add(sessie);
			sessieObservableList.add(sessie);
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
			GenericDaoJpa.startTransaction();
			sessieLijst.remove(sessie);
			sessieObservableList.remove(sessie);
			setHuidigeSessie(null);
			sessieRepo.delete(sessie);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IllegalArgumentException("Er ging iets mis bij het verwijderen van de sessie");
		}
	}

	public void wijzigIngeschrevenen(Gebruiker ingeschrevene, boolean ingeschreven, boolean aanwezig) {
		huidigeSessie.wijzigIngeschrevenen(ingeschrevene, ingeschreven, aanwezig);
		gebruikerSessieObservableList = FXCollections.observableArrayList(huidigeSessie.getGebruikerSessieLijst());
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
