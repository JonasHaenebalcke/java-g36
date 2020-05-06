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

@SuppressWarnings("serial")
@Entity
@Table(name = "Sessie")
public class Sessie implements Serializable {

//	@Transient
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SessieID")
	private int sessieID;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "StatusSessie")
	private StatusSessie statusSessie;
//	@Column(name = "StatusSessie")
//	private int statusSessieValue;
	@ManyToOne
	@JoinColumn(name = "VerantwoordelijkeId")
	private Gebruiker verantwoordelijke;
	@OneToMany(mappedBy = "sessie")
	private List<GebruikerSessie> gebruikerSessieLijst;
	@OneToMany(mappedBy = "sessie")
	private List<Feedback> feedbackLijst;
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
	private SimpleStringProperty titelProperty = new SimpleStringProperty();

	@Transient
	private SimpleStringProperty startDatumSessieProperty = new SimpleStringProperty();
	@Transient
	private SimpleStringProperty eindDatumSessieProperty = new SimpleStringProperty();

	protected Sessie() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
		statusSessie = StatusSessie.nietOpen;
	}

	public Sessie(Gebruiker verantwoordelijke, String titel, String lokaal, LocalDateTime startDatum,
			LocalDateTime eindDatum, int capaciteit, String omschrijving, String gastspreker) {
		this();
		setVerantwoordelijke(verantwoordelijke);
		wijzigSessie(titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving, gastspreker, false);
	}

//	@PostLoad
//	public void fillTransient() {
//		this.statusSessie = StatusSessie.of(statusSessieValue);
//	}
//
//	@PrePersist
//	public void fillPersistent() {
//		if (statusSessie != null)
//			this.statusSessieValue = statusSessie.getStatus();
//
//		if (this.typeGebruiker == typeGebruiker.Gebruiker)
//			this.typeGebruiker = null;
//	}

	public List<Feedback> getFeedbackLijst() {
//		List<Feedback> feedbackLijst = new ArrayList<Feedback>();
//		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
//			Feedback feedback = gebruikerSessie.getFeedback();
//			if(feedback != null)
//				feedbackLijst.add(feedback);
//		}
		return feedbackLijst;
	}

	public void addFeedback(Gebruiker auteur, String content, int score) {
		feedbackLijst.add(new Feedback(auteur, this, content, score));
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

	private void setTitelSessieProperty(String titel) {
		titelProperty.set(titel);
	}

	public StringProperty getTitelSessieProperty() {
		if (titelProperty == null) {
			titelProperty = new SimpleStringProperty();
			setTitelSessieProperty(getTitel());
		}
		return titelProperty;
	}

	private void setStartDatumSessieProperty(String startdatum) {
		startDatumSessieProperty.set(startdatum);
	}

	public StringProperty getStartDatumSessieProperty() {
		if (startDatumSessieProperty == null) {
			startDatumSessieProperty = new SimpleStringProperty();
			setStartDatumSessieProperty(getStartDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		return startDatumSessieProperty;
	}

	private void setEindDatumSessieProperty(String einddatum) {
		eindDatumSessieProperty.set(einddatum);
	}

	public StringProperty getEindDatumSessieProperty() {
		if (eindDatumSessieProperty == null) {
			eindDatumSessieProperty = new SimpleStringProperty();
			setEindDatumSessieProperty(getEindDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		return eindDatumSessieProperty;
	}

	public void setStringProperties() {
		setStartDatumSessieProperty(getStartDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setEindDatumSessieProperty(getEindDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setTitelSessieProperty(getTitel());
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

	public void wijzigIngeschrevenen(Gebruiker ingeschrevene, boolean ingeschreven, boolean aanwezig) {
		if (!isInschrijvingenOpen())
			throw new IllegalArgumentException("Deze sessie is nog niet open voor inschrijvingen");

		boolean gebruikerGevonden = false;
		for (GebruikerSessie gebruikerSessie : gebruikerSessieLijst) {
			if (gebruikerSessie.getIngeschrevene().equals(ingeschrevene)) {
				gebruikerGevonden = true;
				if (ingeschreven)
					gebruikerSessie.wijzigAanwezigheid(aanwezig);
				else
					gebruikerSessieLijst.remove(gebruikerSessie);
				break;
			}
		}
		if (!gebruikerGevonden) {
			if (ingeschreven) {
				GebruikerSessie gebruikerSessie = new GebruikerSessie(this, ingeschrevene);
				gebruikerSessie.wijzigAanwezigheid(aanwezig);
				gebruikerSessieLijst.add(gebruikerSessie);
			} else
				throw new IllegalArgumentException("Gebruiker is al uitgeschreven voor deze sessie");
		}

	}

	public int geefGemiddeldeScore() {
		int res = 0;
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
		// TODO Auto-generated method stub
		return sessieID;
	}
}
