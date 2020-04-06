package domein;

import java.time.LocalDate;
import java.util.*;

import javafx.collections.ObservableList;

public class SessieKalender {

	private Collection<Sessie> sessieLijst;
	private int sessieKalenderID;
	private LocalDate startDate;
	private LocalDate eindDate;

	public int getSessieKalenderID() {
		return this.sessieKalenderID;
	}

	public void setSessieKalenderID(int value) {
		this.sessieKalenderID = value;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate value) {
		this.startDate = value;
	}

	public LocalDate getEindDate() {
		return this.eindDate;
	}

	public void setEindDate(LocalDate value) {
		this.eindDate = value;
	}

	public void wijzigSessieKalender(LocalDate startDate, LocalDate eindDate) {
		throw new UnsupportedOperationException();
	}

	public void voegSessieToe(Sessie sessie) {
		throw new UnsupportedOperationException();
	}

	public void verwijderSessie(Sessie sessie) {
		throw new UnsupportedOperationException();
	}

	public SessieKalender(LocalDate startDate, LocalDate eindDate) {
		throw new UnsupportedOperationException();
	}

	protected SessieKalender() {
		throw new UnsupportedOperationException();
	}

	public ObservableList<Sessie> geefSessiesMaand(int maand) {
		return null;
	}
}
