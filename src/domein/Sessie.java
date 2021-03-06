package domein;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@SuppressWarnings("serial")
@Entity
@Table(name = "Sessie")
public class Sessie implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SessieID")
	private int sessieID;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "StatusSessie")
	private StatusSessie statusSessie;
	@ManyToOne
	@JoinColumn(name = "VerantwoordelijkeId")
	private Gebruiker verantwoordelijke;
	@OneToMany(mappedBy = "sessie")
	private List<GebruikerSessie> gebruikerSessieLijst;
	@OneToMany(mappedBy = "sessie")
	private List<Feedback> feedbackLijst;
	@OneToMany(mappedBy = "sessie")
	private List<Aankondiging> aankondigingen;
	@Column(name = "Titel")
	private String titel;
	@Column(name = "Lokaal")
	private String lokaal;
	@Column(name = "EindDatum")
	private LocalDateTime eindDatum;
	@Column(name = "StartDatum")
	private LocalDateTime startDatum;
	@Column(name = "Capaciteit")
	private int capaciteit;
	@Column(name = "Beschrijving")
	private String omschrijving;
	@Column(name = "Gastspreker")
	private String gastspreker;
	@ManyToOne
	@JoinColumn(name = "SessieKalenderID")
	private SessieKalender sessieKalender;

	@Transient
	private SimpleStringProperty titelProperty;
	@Transient
	private SimpleStringProperty startDatumSessieProperty;
	@Transient
	private SimpleStringProperty eindDatumSessieProperty;
	@Transient
	private SimpleStringProperty naamVerantwoordelijke;
	@Transient
	private SimpleStringProperty aantalDeelnemersProperty;
	@Transient
	private SimpleStringProperty aantalIngeschrevenenProperty;
	@Transient
	private SimpleStringProperty aantalAanwezigenProperty;
	@Transient
	private SimpleStringProperty gemiddleScoreProperty;
	@Transient
	private SimpleStringProperty duurProperty;
	@Transient
	private SimpleStringProperty startUur;
	@Transient
	private SimpleStringProperty openPlaatsenCapaciteit;
	@Transient
	private ObservableList<Feedback> feedbackObservableLijst;

	protected Sessie() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
		statusSessie = StatusSessie.nietOpen;
		aankondigingen = new ArrayList<Aankondiging>();
		feedbackLijst = new ArrayList<Feedback>();
	}

	public Sessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		this();
		setVerantwoordelijke(verantwoordelijke);
		wijzigSessie(titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker, false);
	}

	public ObservableList<Feedback> getFeedbackObservable() {
		if (feedbackObservableLijst == null) {
			feedbackObservableLijst = FXCollections.observableArrayList(getFeedbackLijst());
		}
		return feedbackObservableLijst;
	}

	public List<Feedback> getFeedbackLijst() {
//		idee
//		List<Feedback> feedbackLijst = new ArrayList<Feedback>();
//		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
//			Feedback feedback = gebruikerSessie.getFeedback();
//			if(feedback != null)
//				feedbackLijst.add(feedback);
//		}
		return feedbackLijst;
	}

	public void addFeedback(Gebruiker auteur, String content, int score) {
		Feedback feedback = new Feedback(auteur, this, content, score);
		feedbackLijst.add(feedback);
		auteur.addFeedback(feedback);
	}

	public void wijzigFeedback(int feedbackID, String content, int score) {
		for (Feedback feedback : feedbackLijst) {
			if (feedback.getFeedbackID() == feedbackID) {
				feedback.wijzigFeedback(content, score);
				break;
			}
		}
	}

	private void zetInschrijvingenOpen(boolean open) {
		if (open && statusSessie == StatusSessie.nietOpen)
			statusSessie = StatusSessie.InschrijvingenOpen;
		else if (!open && statusSessie != StatusSessie.nietOpen)
			throw new IllegalArgumentException("Sessie kan niet terug gesloten worden");
	}

	public boolean isInschrijvingenOpen() {
		return statusSessie == StatusSessie.InschrijvingenOpen;
	}

	public SimpleStringProperty getNaamVerantwoordelijke() {
		if (naamVerantwoordelijke == null)
			setNaamVerantwoordelijke();
		return naamVerantwoordelijke;
	}

	public void setNaamVerantwoordelijke() {
		if (naamVerantwoordelijke == null)
			naamVerantwoordelijke = new SimpleStringProperty();
		this.naamVerantwoordelijke
				.set(this.verantwoordelijke.getVoornaam() + " " + this.verantwoordelijke.getFamilienaam());
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

	public LocalDateTime getEindDatum() {
		return eindDatum;
	}

	public LocalDateTime getStartDatum() {
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
		if (titel.isBlank() || titel == null)
			throw new IllegalArgumentException("Titel moet ingevuld zijn.");
		this.titel = titel;
	}

	private void setLokaal(String lokaal) {
		if (lokaal.isBlank() || lokaal == null)
			throw new IllegalArgumentException("Lokaal moet ingevuld zijn.");
		this.lokaal = lokaal;
	}

	private void setStartDatum(LocalDateTime startDatum) {
		if (eindDatum == null) {
			if (startDatum.toLocalDate().isBefore(LocalDate.now().plusDays(1)))
				throw new IllegalArgumentException("De startdatum moet minstens 1 dag in de toekomst liggen.");
		} else {
			if (startDatum.isBefore(LocalDateTime.now()))
				throw new IllegalArgumentException("De startdatum mag niet in het verleden liggen.");
		}
	}

	private void setStartUur(LocalDateTime startUur) {
		if (eindDatum == null) {
			if (startUur.isAfter(LocalDateTime.now().plusHours(24)))
				this.startDatum = startUur;
			else
				throw new IllegalArgumentException("Het startuur moet minstens 24 uur in de toekomst liggen.");
		} else {
			if (startUur.isAfter(LocalDateTime.now()))
				this.startDatum = startUur;
			else
				throw new IllegalArgumentException("Het startuur mag niet in het verleden liggen.");
		}
	}

	private void setEindDatum(LocalDateTime eindDatum) {
		// leeg want moet nog gecontroleerd worden op uren
//		if (!(ChronoUnit.DAYS.between(startDatum.toLocalDate(), eindDatum.toLocalDate()) == 0
//				|| ChronoUnit.DAYS.between(startDatum.toLocalDate(), eindDatum.toLocalDate()) == 1))
//			throw new IllegalArgumentException("De einddatum moet op dezelfde dag of 1 dag na de startdatum liggen.");
	}

	private void setEindUur(LocalDateTime eindUur) {
		if (eindUur.isAfter(startDatum) && ChronoUnit.MINUTES.between(startDatum, eindUur) >= 30)
			this.eindDatum = eindUur;
		else
			throw new IllegalArgumentException("Het einduur moet minstens 30 minuten na het startuur liggen.");
	}

	private void setCapaciteit(int capaciteit) {
		if (capaciteit < 0)
			throw new IllegalArgumentException("Capaciteit kan geen negatief getal zijn.");
		this.capaciteit = capaciteit;
	}

	private void setVerantwoordelijke(Gebruiker verantwoordelijke) {
		if (verantwoordelijke == null)
			throw new IllegalArgumentException("Verantwoordelijke moet geselecteerd zijn.");
		this.verantwoordelijke = verantwoordelijke;
	}

	private void setTitelSessieProperty() {
		if (titelProperty == null)
			titelProperty = new SimpleStringProperty();
		titelProperty.set(getTitel());
	}

	public StringProperty getTitelSessieProperty() {
		if (titelProperty == null)
			setTitelSessieProperty();
		return titelProperty;
	}

	public StringProperty getAantalIngeschrevenenProperty() {
		if (aantalIngeschrevenenProperty == null)
			setAantalIngeschrevenenProperty();
		return aantalIngeschrevenenProperty;
	}

	private void setAantalIngeschrevenenProperty() {
		if (aantalIngeschrevenenProperty == null)
			aantalIngeschrevenenProperty = new SimpleStringProperty();
		aantalIngeschrevenenProperty.set(String.valueOf(getGebruikerSessieLijst().size()));
	}

	public StringProperty getAantalAanwezigenProperty() {
		if (aantalAanwezigenProperty == null)
			setAantalAanwezigenProperty();
		return aantalAanwezigenProperty;
	}

	private void setAantalAanwezigenProperty() {
		if (aantalAanwezigenProperty == null)
			aantalAanwezigenProperty = new SimpleStringProperty();
		aantalAanwezigenProperty.set(String.valueOf(geefAantalAanwezigen()));
	}

	public StringProperty getGemiddleScoreProperty() {
		if (gemiddleScoreProperty == null)
			setGemiddleScoreProperty();
		return gemiddleScoreProperty;
	}

	private void setGemiddleScoreProperty() {
		if (gemiddleScoreProperty == null)
			gemiddleScoreProperty = new SimpleStringProperty();
		gemiddleScoreProperty.set(String.valueOf(geefGemiddeldeScore()) + "/5");
	}

	private void setStartDatumSessieProperty() {
		if (startDatumSessieProperty == null)
			startDatumSessieProperty = new SimpleStringProperty();
		startDatumSessieProperty.set(getStartDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	public StringProperty getStartDatumSessieProperty() {
		if (startDatumSessieProperty == null)
			setStartDatumSessieProperty();
		return startDatumSessieProperty;
	}

	private void setEindDatumSessieProperty() {
		if (eindDatumSessieProperty == null)
			eindDatumSessieProperty = new SimpleStringProperty();
		eindDatumSessieProperty.set(getEindDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	public StringProperty getEindDatumSessieProperty() {
		if (eindDatumSessieProperty == null)
			setEindDatumSessieProperty();
		return eindDatumSessieProperty;
	}

	private void setAantalDeelnemersProperty() {
		if (aantalDeelnemersProperty == null)
			aantalDeelnemersProperty = new SimpleStringProperty();
		aantalDeelnemersProperty.set(String.valueOf(getGebruikerSessieLijst().size()));
	}

	public StringProperty getAantalDeelnemersProperty() {
		if (aantalDeelnemersProperty == null)
			setAantalDeelnemersProperty();
		return aantalDeelnemersProperty;
	}

	public int getDuur() {
		return (int) ChronoUnit.MINUTES.between(startDatum, eindDatum);
	}

	public void setDuurProperty() {
		if (duurProperty == null)
			duurProperty = new SimpleStringProperty();
		duurProperty.set(String.valueOf(getDuur()) + " minuten");
	}

	public SimpleStringProperty getDuurProperty() {
		if (duurProperty == null)
			setDuurProperty();
		return duurProperty;
	}

	public void setStartUurProperty() {
		if (startUur == null)
			startUur = new SimpleStringProperty();
		startUur.set(String.valueOf(getStartDatum().format(DateTimeFormatter.ofPattern("HH:mm"))));
	}

	public SimpleStringProperty getStartUurProperty() {
		if (startUur == null)
			setStartUurProperty();
		return startUur;
	}

	public void setOpenPlaatsenCapaciteitProperty() {
		if (openPlaatsenCapaciteit == null)
			openPlaatsenCapaciteit = new SimpleStringProperty();
		openPlaatsenCapaciteit.set(String.valueOf(getGebruikerSessieLijst().size()) + "/" + String.valueOf(capaciteit));
	}

	public SimpleStringProperty getOpenPlaatsenCapaciteitProperty() {
		if (openPlaatsenCapaciteit == null)
			setOpenPlaatsenCapaciteitProperty();
		return openPlaatsenCapaciteit;
	}

	public void setStringProperties() {
		setStartDatumSessieProperty();
		setStartUurProperty();
		setEindDatumSessieProperty();
		setTitelSessieProperty();
		setAantalDeelnemersProperty();
		setAantalIngeschrevenenProperty();
		setAantalAanwezigenProperty();
		setGemiddleScoreProperty();
		setOpenPlaatsenCapaciteitProperty();
		setDuurProperty();
	}

	private void setDatums(LocalDateTime startDatum, LocalDateTime eindDatum) {

		if (!(ChronoUnit.DAYS.between(startDatum.toLocalDate(), eindDatum.toLocalDate()) == 0
				|| ChronoUnit.DAYS.between(startDatum.toLocalDate(), eindDatum.toLocalDate()) == 1))
			throw new IllegalArgumentException("De einddatum moet op dezelfde dag of 1 dag na de startdatum liggen.");
		setStartDatum(startDatum);
		setEindDatum(eindDatum);

		if (ChronoUnit.MINUTES.between(startDatum, eindDatum) < 30)
			throw new IllegalArgumentException("De sessie moet een minimumperiode van 30 minuten hebben.");
		setStartUur(startDatum);
		setEindUur(eindDatum);
	}

	public int geefAantalAanwezigen() {
		int ret = 0;

		for (GebruikerSessie gs : getGebruikerSessieLijst()) {
			if (gs.isAanwezig())
				ret++;
		}
		return ret;
	}

	public void wijzigSessie(String titel, String lokaal, LocalDateTime startDatum, LocalDateTime eindDatum,
			int capaciteit, String omschrijving, String gastspreker, boolean open) {

		if (statusSessie == StatusSessie.open || statusSessie == StatusSessie.gesloten)
			throw new IllegalArgumentException("De sessie mag niet geopend zijn om deze te kunnen wijzigen.");

		setDatums(startDatum, eindDatum);

		setTitel(titel);
		setLokaal(lokaal);
		setCapaciteit(capaciteit);
		this.omschrijving = omschrijving;
		this.gastspreker = gastspreker;
		zetInschrijvingenOpen(open);
		setStringProperties();

	}

	public boolean isGebruikerIngeschreven(Gebruiker ingeschrevene) {
		boolean ingeschreven = false;
		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
			if (gebruikerSessie.getIngeschrevene().equals(ingeschrevene)) {
				ingeschreven = true;
				break;
			}
		}
		return ingeschreven;
	}

	public boolean isGebruikerAanwezig(Gebruiker ingeschrevene) {
		boolean aanwezig = false;
		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
			if (gebruikerSessie.getIngeschrevene().equals(ingeschrevene) && gebruikerSessie.isAanwezig()) {
				aanwezig = true;
				break;
			}
		}
		return aanwezig;
	}

	public GebruikerSessie wijzigIngeschrevenen(Gebruiker ingeschrevene, boolean ingeschreven, boolean aanwezig) {
		GebruikerSessie res = null;
		if (!isInschrijvingenOpen())
			throw new IllegalArgumentException("Deze sessie is nog niet open voor inschrijvingen");

		boolean gebruikerGevonden = false;
		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
			if (gebruikerSessie.getIngeschrevene().equals(ingeschrevene)) {
				gebruikerGevonden = true;

				if (ingeschreven)
					gebruikerSessie.wijzigAanwezigheid(aanwezig);
				else {
					ingeschrevene.verwijderGebruikerSessie(gebruikerSessie);
					gebruikerSessieLijst.remove(gebruikerSessie);
				}
				res = gebruikerSessie;
				break;
			}
		}
		if (!gebruikerGevonden) {
			if (ingeschreven) {
				GebruikerSessie gebruikerSessie = new GebruikerSessie(this, ingeschrevene);
				gebruikerSessie.wijzigAanwezigheid(aanwezig);
				ingeschrevene.addGebruikerSessie(gebruikerSessie);
				gebruikerSessieLijst.add(gebruikerSessie);
				res = gebruikerSessie;
			} else
				throw new IllegalArgumentException("Gebruiker is al uitgeschreven voor deze sessie");
		}
		return res;
	}

	public double geefGemiddeldeScore() {
		if (getFeedbackLijst().isEmpty())
			return 0;
		double res = 0;
		for (Feedback feedback : getFeedbackLijst()) {
			res += feedback.getScore();
		}
		return res / getFeedbackLijst().size();
	}

	@Override
	public String toString() {
		return "Sessie [statusSessie=" + statusSessie + ", verantwoordelijke=" + verantwoordelijke
				+ ", gebruikerSessieLijst=" + gebruikerSessieLijst + ", titel=" + titel + ", lokaal=" + lokaal
				+ ", eindDatum=" + eindDatum + ", startDatum=" + startDatum + ", capaciteit=" + capaciteit
				+ ", omschrijving=" + omschrijving + ", gastspreker=" + gastspreker + ", titelProperty=" + titelProperty
				+ ", startDatumSessieProperty=" + startDatumSessieProperty + ", eindDatumSessieProperty="
				+ eindDatumSessieProperty + "]";
	}

	public int getSessieID() {
		return sessieID;
	}
}
