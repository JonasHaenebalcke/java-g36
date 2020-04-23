package domein;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

@Entity
@Table(name = "SessieKalender")
public class SessieKalender {

	@Transient
	private Collection<Sessie> sessieLijst;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int sessieKalenderID;
	@Column(name = "startDate")
	private LocalDate startDatum;
	@Column(name = "eindDate")
	private LocalDate eindDatum;

	@Transient
	private SimpleStringProperty startDatumProperty = new SimpleStringProperty();
	@Transient
	private SimpleStringProperty eindDatumProperty = new SimpleStringProperty();

	protected SessieKalender() {
		sessieLijst = new ArrayList<Sessie>();
	}

	public SessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		this();
		setDatums(startDatum, eindDatum);
	}

	public void setDatums(LocalDate startDatum, LocalDate eindDatum) {
		if (eindDatum.getYear() - startDatum.getYear() != 1) {
			throw new IllegalArgumentException("SessieKalender moet op eenvolgende jaren hebben ");
		}
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setStringProperties();
	}

	public void setStringProperties() {
		setStartDatumProperty(getStartDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setEindDatumProperty(getEindDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	private void setStartDatumProperty(String datum) {
		startDatumProperty.set(datum);
	}

	public StringProperty getStartDatumProperty() {
		return startDatumProperty;
	}

	private void setEindDatumProperty(String datum) {
		eindDatumProperty.set(datum);
	}

	public StringProperty getEindDatumProperty() {
		return eindDatumProperty;
	}

	public int getSessieKalenderID() {
		return this.sessieKalenderID;
	}

	public LocalDate getStartDatum() {
		return this.startDatum;
	}

	public void setStartDatum(LocalDate startDatum) {
		this.startDatum = startDatum;
		if (startDatum.isAfter(LocalDate.now()))
			this.startDatum = startDatum;
		else
			throw new IllegalArgumentException("De startdatum moet in de toekomst liggen.");
	}

	public LocalDate getEindDatum() {
		return this.eindDatum;
	}

	public void setEindDatum(LocalDate eindDatum) {
		this.eindDatum = eindDatum;
		if (eindDatum.isAfter(LocalDate.now()) && eindDatum.isAfter(startDatum))
			this.eindDatum = eindDatum;
		else
			throw new IllegalArgumentException("De einddatum moet in de toekomst liggen en na de startdatum komen.");
	}

	public void wijzigSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		if (startDatum.isAfter(eindDatum)) {
			throw new IllegalArgumentException("Startdatum kan niet na einddatum liggen");
		}
		setDatums(startDatum, eindDatum);
	}

	public void voegSessieToe(Sessie sessie) {
		if (sessieLijst.contains(sessie))
			throw new IllegalArgumentException("De gegeven sessie komt reeds voor in deze sessiekalender.");
		else
			sessieLijst.add(sessie);
	}

	public void verwijderSessie(Sessie sessie) {
		if (sessieLijst.contains(sessie))
			sessieLijst.remove(sessie);
		else
			throw new NullPointerException("De gegeven sessie komt niet voor in deze sessiekalender.");
	}

	public Collection<Sessie> geefSessiesMaand(int maand) {
		Collection<Sessie> sessies = new ArrayList<Sessie>();
		if (sessieLijst.isEmpty() || sessieLijst == null)
			throw new NullPointerException("Deze SessieKalender bevat nog geen sessies.");

		for (Sessie sessie : sessieLijst) {
			if (sessie.getStartDatum().getDayOfMonth() == maand)
				sessies.add(sessie);
		}
		if (sessies.isEmpty() || sessies == null)
			throw new NullPointerException("Deze SessieKalender bevat nog geen sessies voor deze maand.");

		return sessieLijst;
	}

	@Override
	public String toString() {
		return "startDate=" + startDatum + ", eindDate=" + eindDatum;
	}

}
