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
	private ObservableList<Sessie> gebruikerObservableList;
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
//			sessieLijst = sessieRepo.findAll();
		}
		return sessieLijst;
	}
	
	public ObservableList<Sessie> geefSessiesObservable() {
		if (gebruikerObservableList == null) {
			gebruikerObservableList = FXCollections.observableArrayList(geefSessies());
		}
		return gebruikerObservableList;
	}
	 
	public ObservableList<GebruikerSessie> geefGebruikerSessiesObservable() {
		return FXCollections.observableArrayList(huidigeSessie.getGebruikerSessieLijst());
	 }

	public void setHuidigeSessie(Sessie sessie) {
		this.huidigeSessie = sessie;
	}
	
	public void wijzigSessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker, boolean open) {
		huidigeSessie.wijzigSessie(titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker, open);
	}

	public void voegSessieToe(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		Sessie sessie = new Sessie(verantwoordelijke, titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker);
		setHuidigeSessie(sessie);
		sessieLijst.add(sessie);
	}

	public void verwijderHuidigeSessie() {
		sessieLijst.remove(huidigeSessie);
		setHuidigeSessie(null);
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
