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

//	private boolean openVoorInschrijving;

	@Transient
	private SimpleStringProperty titelProperty = new SimpleStringProperty();
	
	@Transient
	private SimpleStringProperty startDatumSessieProperty = new SimpleStringProperty();
	@Transient
	private SimpleStringProperty eindDatumSessieProperty = new SimpleStringProperty();

	protected Sessie() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
		zetInschrijvingenOpen(false);
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
			if (feedback.getFeedbackID() == feedbackID)
				feedback.wijzigFeedback(content, score);
		}
	}

	private void zetInschrijvingenOpen(boolean open) {
//		this.openVoorInschrijving = open;
		if (!open)
			statusSessie = StatusSessie.nietOpen;
		else
			statusSessie = StatusSessie.InschrijvingenOpen;
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
//		this.startDatum = startDatum;
		if (eindDatum == null) {
			if (startDatum.isAfter(LocalDateTime.now().plusHours(24)))
				this.startDatum = startDatum;
			else
				throw new IllegalArgumentException("De startdatum moet in de toekomst liggen.");
		} else {
			if (
//					ChronoUnit.MINUTES.between(startDatum, eindDatum) >= 30 &&
			(startDatum.isAfter(LocalDateTime.now()) || eindDatum.isBefore(LocalDateTime.now())))
				this.startDatum = startDatum;
			else
				throw new IllegalArgumentException(
						"De startdatum en einddatum moeten beiden in het verleden of in de toekomst liggen.");
		}
	}

	private void setEindDatum(LocalDateTime eindDatum) {
//		this.eindDatum = eindDatum;
		if (eindDatum.isAfter(startDatum) && ChronoUnit.MINUTES.between(startDatum, eindDatum) >= 30)
			this.eindDatum = eindDatum;
		else
			throw new IllegalArgumentException("De einddatum moet minstens 30 minuten na de startdatum liggen.");
	}

	private void setCapaciteit(int capaciteit) {
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
		return titelProperty;
	}

	private void setStartDatumSessieProperty(String startdatum) {
		startDatumSessieProperty.set(startdatum);
	}

	public StringProperty getStartDatumSessieProperty() {
		return startDatumSessieProperty;
	}

	private void setEindDatumSessieProperty(String einddatum) {
		eindDatumSessieProperty.set(einddatum);
	}

	public StringProperty getEindDatumSessieProperty() {
		return eindDatumSessieProperty;
	}

	public void setStringProperties() {
		setStartDatumSessieProperty(getStartDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setEindDatumSessieProperty(getEindDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setTitelSessieProperty(getTitel());
	}

	public void wijzigSessie(String titel, String lokaal, LocalDateTime startDatum, LocalDateTime eindDatum,
			int capaciteit, String omschrijving, String gastspreker, boolean open) {
//		if (!getOpenVoorInschrijving()) //afh van commentaar vd prof
		if (statusSessie == StatusSessie.open || statusSessie == StatusSessie.gesloten)
			throw new IllegalArgumentException("De sessie mag niet geopend zijn om deze te kunnen wijzigen.");
		if (ChronoUnit.MINUTES.between(startDatum, eindDatum) < 30)
			throw new IllegalArgumentException("De sessie moet een minimumperiode van 30 minuten hebben.");

		setTitel(titel);
		setLokaal(lokaal);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
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
				+  ", startDatumSessieProperty=" + startDatumSessieProperty
				+ ", eindDatumSessieProperty=" + eindDatumSessieProperty + "]";
	}
}
