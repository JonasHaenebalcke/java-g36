package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("serial")
@Entity
@Table(name = "GebruikerSessie")
public class GebruikerSessie implements Serializable {

	@Column(name = "Aanwezig")
	private boolean aanwezig;
//	@Id
//	@Column(name = "GebruikerID")
//	private String gebruikerID;
//	@Id
//	@Column(name = "SessieID")
//	private int sessieID;
	@EmbeddedId
	GebruikerSessieId id;
	@MapsId("SessieID")
	@ManyToOne
	@JoinColumn(name = "SessieID")
	private Sessie sessie;
	@MapsId("GebruikerID")
	@ManyToOne
	@JoinColumn(name = "GebruikerID")
	private Gebruiker ingeschrevene;
	@Column(name = "InschrijvingsDatum")
	private LocalDateTime inschrijvingsDatum;

	@Transient
	private SimpleStringProperty inschrijvingsDatumProperty;

//	idee
//	private Feedback feedback;

	protected GebruikerSessie() {
	}

	public GebruikerSessie(Sessie sessie, Gebruiker ingeschrevenen) {
		this.aanwezig = false;
		this.sessie = sessie;
		this.ingeschrevene = ingeschrevenen;
		inschrijvingsDatum = LocalDateTime.now();
		this.id = new GebruikerSessieId(sessie.getSessieID(), ingeschrevenen.getGebruikerID());
	}

	public LocalDateTime getInschrijvingsDatum() {
		return inschrijvingsDatum;
	}

	public boolean isAanwezig() {
		return aanwezig;
	}

	public Sessie getSessie() {
		return sessie;
	}

	public Gebruiker getIngeschrevene() {
		return ingeschrevene;
	}

	public void wijzigAanwezigheid(boolean aanwezig) {
		this.aanwezig = aanwezig;
	}

//	idee
//	public void addFeedback(String content, int score) {
//		feedback = new Feedback(content, score);
//	}
//	
//	public Feedback getFeedback() {
//		return feedback;
//	}
//	
//	public void wijzigFeedback(String content, int score) {
//		feedback.wijzigFeedback(content, score);
//	}

	private void setInschrijvingsDatumProperty(String inschrijvingsdatum) {
		inschrijvingsDatumProperty.set(inschrijvingsdatum);
	}

	public StringProperty getInschrijvingsDatumProperty() {
		if (inschrijvingsDatumProperty == null) {
			inschrijvingsDatumProperty = new SimpleStringProperty();
			setInschrijvingsDatumProperty(inschrijvingsDatum.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		return inschrijvingsDatumProperty;
	}
}

@Embeddable
class GebruikerSessieId implements Serializable {

	protected GebruikerSessieId() {
	}

	public GebruikerSessieId(int sessieID2, String gebruikerID2) {
		this.SessieID = sessieID2;
		this.GebruikerID = gebruikerID2;
	}

	int SessieID;
	String GebruikerID;
}
