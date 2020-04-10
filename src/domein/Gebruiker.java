package domein;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.*;
import javafx.beans.property.*;

@SuppressWarnings("serial")
@Entity
//@NamedQuery(name= "Gebruiker. ", query = "select * from Gebruiker")
@Table(name = "Gebruiker")
public class Gebruiker implements Serializable {

	// Attributen voor JavaFX
//	@Transient
//	private SimpleStringProperty gebruikerID = new SimpleStringProperty();
//	@Transient
//	private SimpleStringProperty gebruikersnaam = new SimpleStringProperty();
//	@Transient
//	private SimpleStringProperty voornaam = new SimpleStringProperty();
//	@Transient
//	private SimpleStringProperty familienaam = new SimpleStringProperty();
//	@Transient
//	private SimpleStringProperty mailadres = new SimpleStringProperty();
//	@Transient // Dit is eigenlijk een pad, dus moet dit wel stringProp zijn?
//	private StringProperty profielfoto = new SimpleStringProperty();

	// Enums
	@Column(name = "StatusGebruiker")
	private int statusValue;
	@Transient
	private Status status;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "IsHoofdverantwoordelijke")
	private Type type;

	// Data die binnenkomt via databank
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private String gebruikerID;
	@Column(name = "UserName")
	private String gebruikersnaam;
	@Column(name = "Voornaam")
	private String voornaam;
	@Column(name = "Familienaam")
	private String familienaam;
	@Column(name = "Email")
	private String mailadres;
	@Transient
	private String profielFoto;

	// Methodes voor Enums te mappen
	@PostLoad
	public void fillTransient() {
		this.status = Status.of(statusValue);

		try {
			if (this.type.toString().equalsIgnoreCase("Verantwoordelijke")) {
				this.type = type.Hoofdverantwoordelijke;
			} else {
				this.type = type.Verantwoordelijke;
			}
		} catch (NullPointerException e) {
			this.type = type.Gebruiker;
		}

	}

	@PrePersist
	public void fillPersistent() {
		if (status != null)
			this.statusValue = status.getStatus();
	}

	protected Gebruiker() {
	}

	/**
	 * 
	 * @param voornaam
	 * @param familienaam
	 * @param mailadres
	 * @param gebruikersnaam
	 * @param profielfoto
	 */
	public Gebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam, Type type,
			Status status, String profielfoto) {

		
		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setGebruikersnaam(gebruikersnaam);
		setProfielfoto(profielfoto);
	}

	@Transient
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Transient
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getGebruikerID() {
		return gebruikerID;
	}

//	public StringProperty getGebruikerIDProperty() {
//		return gebruikerID;
//	}

	public void setRandomGebruikerID() {
		if(gebruikerID == null)
			this.gebruikerID = UUID.randomUUID().toString();
	}

	public String getVoornaam() {
		return voornaam;
	}

//	public StringProperty getVoornaamProperty() {
//		return voornaam;
//	}

	private void setVoornaam(String voornaam) {
		if (voornaam == null || voornaam.isBlank())
			throw new NullPointerException("Voornaam mag niet leeg zijn!");
		if (!voornaam.matches("^([A-Za-zÀ-ÿ\\\\'`´’\\- ])+$"))
			throw new IllegalArgumentException("Voornaam is ongeldig");
		this.voornaam = voornaam;
	}

	public String getFamilienaam() {
		return familienaam;
	}

//	public StringProperty getFamilienaamProperty() {
//		return familienaam;
//	}

	private void setFamilienaam(String familienaam) {
		if (familienaam == null || familienaam.isBlank())
			throw new NullPointerException("familienaam mag niet leeg zijn!");
		if (!familienaam.matches("^([A-Za-zÀ-ÿ\\\\'`´’\\- ])+$"))
			throw new IllegalArgumentException("familienaam is ongeldig");
		this.familienaam = familienaam;
	}

	public String getMailadres() {
		return mailadres;
	}

//	public StringProperty getMailadresProperty() {
//		return mailadres;
//	}

	private void setMailadres(String mailadres) {
		if (mailadres == null || mailadres.isBlank())
			throw new NullPointerException("Mailadres mag niet leeg zijn!");
		if (!mailadres.matches(
//				"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
				".*@+.*\\.+.*"))
			throw new IllegalArgumentException("Email moet geldig zijn!");

		this.mailadres = mailadres;
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

//	public StringProperty getGebruikersnaamProperty() {
//		return gebruikersnaam;
//	}

	private void setGebruikersnaam(String gebruikersnaam) {

		if (gebruikersnaam == null || gebruikersnaam.isBlank())
			throw new NullPointerException("Gebruikersnaam mag niet leeg zijn!");
		if (!gebruikersnaam.matches("[0-9]{6}[a-z]{2}"))
			throw new IllegalArgumentException("Gebruikersnaam moet 6 cijfers gevolgd door 2 initialen");
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getProfielfoto() {
		return profielFoto;
	}

//	public StringProperty getProfielfotoProperty() {
//		return profielfoto;
//	}

	private void setProfielfoto(String profielfoto) {
		this.profielFoto = profielfoto;
	}

	/**
	 * 
	 * @param voornaam
	 * @param familienaam
	 * @param mailadres
	 * @param gebruikersnaam
	 * @param profielfoto
	 */
	public void wijzigGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam, Type type,
			Status status, String profielfoto) {
		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setGebruikersnaam(gebruikersnaam);
		setProfielfoto(profielfoto);

	}

	@Override
	public String toString() {
		return "Gebruiker [status=" + status + ", type=" + type + ", gebruikerID=" + gebruikerID + ", gebruikersnaam="
				+ gebruikersnaam + ", voornaam=" + voornaam + ", familienaam=" + familienaam + ", mailadres="
				+ mailadres + ", profielFoto=" + profielFoto + "]";
	}

//	@Override
//	public String toString() {
//		return "Gebruiker [gebruikerID=" + gebruikerID + ", gebruikersnaam=" + gebruikersnaam + ", voornaam=" + voornaam
//				+ ", familienaam=" + familienaam + ", mailadres=" + mailadres + ", profielfoto=" + profielfoto
//				+ ", statusValue=" + statusValue + ", status=" + status + ", type=" + type + ", gebruikerIdDb="
//				+ gebruikerIdDb + ", gebruikersnaamDb=" + gebruikersnaamDb + ", voornaamDb=" + voornaamDb
//				+ ", familienaamDb=" + familienaamDb + ", mailadresDb=" + mailadresDb + "]";
//	}

}