package domein;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Id;

import javafx.collections.ObservableList;

public class SessieKalender {

	private Collection<Sessie> sessieLijst;
	@Id
	@Column(name = "Id")
	private int sessieKalenderID;
	@Column(name = "startDate")
	private LocalDate startDate;
	@Column(name = "eindDate")
	private LocalDate eindDate;

	protected SessieKalender() {
		
	}
	
	public SessieKalender(LocalDate startDate, LocalDate eindDate) {
		setStartDate(startDate);
		setEindDate(eindDate);
	}
	
	public int getSessieKalenderID() {
		return this.sessieKalenderID;
	}

	public void setSessieKalenderID(int id) {
		this.sessieKalenderID = id;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate startDate) {
		if(startDate.isAfter(LocalDate.now()))
			this.startDate = startDate;
		else
			throw new IllegalArgumentException("De startdatum moet in de toekomst liggen.");
	}

	public LocalDate getEindDate() {
		return this.eindDate;
	}

	public void setEindDate(LocalDate eindDate) {
		if(eindDate.isAfter(LocalDate.now()) && eindDate.isAfter(startDate))
			this.eindDate = eindDate;
		else
			throw new IllegalArgumentException("De einddatum moet in de toekomst liggen en na de startdatum komen.");
	}

	public void wijzigSessieKalender(LocalDate startDate, LocalDate eindDate) {
		setStartDate(startDate);
		setEindDate(eindDate);
	}

	public void voegSessieToe(Sessie sessie) {
		if(sessieLijst.contains(sessie))
			throw new IllegalArgumentException("De gegeven sessie komt reeds voor in deze sessiekalender.");
		else
			sessieLijst.add(sessie);
	}

	public void verwijderSessie(Sessie sessie) {
		if(sessieLijst.contains(sessie)) 
			sessieLijst.remove(sessie);
		else
			throw new NullPointerException("De gegeven sessie komt niet voor in deze sessiekalender.");
	}

	public ObservableList<Sessie> geefSessiesMaand(int maand) {
		//ObservableList<Sessie> sessies; //kan nog niet aan "maand" van sessie.
		/*sessieLijst.forEach( s -> {
			if()
		});*/
		if(sessieLijst.isEmpty() || sessieLijst == null)
			throw new NullPointerException("Sessie lijst is leeg.");
		else
			return (ObservableList<Sessie>) sessieLijst;
	}
	
}