package domein;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

@Entity
@Table(name = "SessieKalender")
public class SessieKalender {

//	@Transient
	@OneToMany(mappedBy = "sessieKalender")
	private Collection<Sessie> sessieLijst;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sessieKalenderID")
	private int sessieKalenderID;
	@Column(name = "StartDatum")
	private LocalDate startDatum;
	@Column(name = "EindDatum")
	private LocalDate eindDatum;

	@Transient
	private SimpleStringProperty startDatumProperty;
	@Transient
	private SimpleStringProperty eindDatumProperty;

	protected SessieKalender() {
		sessieLijst = new ArrayList<Sessie>();
	}

	public SessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		this();
		setDatums(startDatum, eindDatum);
	}

	public SessieKalender(LocalDate startDatum, LocalDate eindDatum, boolean bool) {
		this();
		this.startDatum = startDatum;
		this.eindDatum = eindDatum;
		setStringProperties();
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
		setStartDatumProperty();
		setEindDatumProperty();
	}

	private void setStartDatumProperty() {
		if (startDatumProperty == null)
			startDatumProperty = new SimpleStringProperty();
		startDatumProperty.set(getStartDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	public StringProperty getStartDatumProperty() {
		if (startDatumProperty == null)
			setStartDatumProperty();
		return startDatumProperty;
	}

	private void setEindDatumProperty() {
		if (eindDatumProperty == null)
			eindDatumProperty = new SimpleStringProperty();
		eindDatumProperty.set(getEindDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	public StringProperty getEindDatumProperty() {
		if (eindDatumProperty == null)
			setEindDatumProperty();
		return eindDatumProperty;
	}

	public int getSessieKalenderID() {
		return this.sessieKalenderID;
	}

	public LocalDate getStartDatum() {
		return this.startDatum;
	}

	public void setStartDatum(LocalDate startDatum) {
		if (startDatum.isAfter(LocalDate.now()))
			this.startDatum = startDatum;
		else
			throw new IllegalArgumentException("De startdatum moet in de toekomst liggen.");
	}

	public LocalDate getEindDatum() {
		return this.eindDatum;
	}

	public void setEindDatum(LocalDate eindDatum) {
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

	public Collection<Sessie> geefSessiesMaand(Number maand) {
		Collection<Sessie> sessies = new ArrayList<Sessie>();
		if (sessieLijst.isEmpty() || sessieLijst == null)
			throw new NullPointerException("Deze SessieKalender bevat nog geen sessies.");

		for (Sessie sessie : sessieLijst) {
			if (sessie.getStartDatum().getMonthValue() == (int) maand)
				sessies.add(sessie);
		}
		if (sessies.isEmpty() || sessies == null)
			throw new NullPointerException("Deze SessieKalender bevat nog geen sessies voor deze maand.");

		return sessies;
	}

	@Override
	public String toString() {
		sessieLijst.size();
		return sessieLijst.toString();
//		return "SessieKalender [sessieLijst=" + sessieLijst + ", sessieKalenderID=" + sessieKalenderID + ", startDatum="
//				+ startDatum + ", eindDatum=" + eindDatum + ", startDatumProperty=" + startDatumProperty
//				+ ", eindDatumProperty=" + eindDatumProperty + "]";
	}

}
