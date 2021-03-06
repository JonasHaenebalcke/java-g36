package domein;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

	// Enums
	@Column(name = "StatusGebruiker")
	private int statusValue;
	@Transient
	private Status status;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TypeGebruiker")
	private TypeGebruiker typeGebruiker;

	@Column(name = "PasswordHashJava")
	private String passwordHashJava;

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
	@Column(name = "NormalizedUserName")
	private String normalizedUserName;
	@Column(name = "NormalizedEmail")
	private String normalizedEmail;
	@Column(name = "EmailConfirmed")
	private boolean emailConfirmed;
	@Column(name = "PasswordHash")
	private String passwordHash;
	@Column(name = "SecurityStamp")
	private String securityStamp;
	@Column(name = "ConcurrencyStamp")
	private String concurrencyStamp;
	@Column(name = "PhoneNumber")
	private String phoneNumber;
	@Column(name = "PhoneNumberConfirmed")
	private boolean phoneNumberConfirmed;
	@Column(name = "TwoFactorEnabled")
	private boolean twoFactorEnabled;
	@Column(name = "LockoutEnd")
	private LocalDateTime lockoutEnd;
	@Column(name = "LockoutEnabled")
	private boolean lockoutEnabled;
	@Column(name = "AccessFailedCount")
	private int accessFailedCount;
	@Column(name = "Barcode")
	private String barcode;
	@Column(name = "AantalKeerAfwezig")
	private int aantalKeerAfwezig;
	@OneToMany(mappedBy = "verantwoordelijke")
	private List<Sessie> OpenTeZettenSessies;
	@OneToMany(mappedBy = "ingeschrevene")
	private List<GebruikerSessie> gebruikerSessieLijst;
	@OneToMany(mappedBy = "auteur")
	private List<Feedback> feedbackLijst;
	@OneToMany(mappedBy = "publicist")
	private List<Aankondiging> aankondigingen;

	@Transient
	private SimpleStringProperty voorNaamProperty;
	@Transient
	private SimpleStringProperty naamProperty;
	@Transient
	private SimpleStringProperty typeProperty;
	@Transient
	private SimpleStringProperty statusProperty;
	@Transient
	private SimpleStringProperty emailProperty;
	@Transient
	private SimpleStringProperty aantalFeedbacksProperty;
	@Transient
	private SimpleStringProperty aantalAanwezigProprty;
	@Transient
	private SimpleStringProperty aantalAfwezigProperty;
	@Transient
	private SimpleStringProperty procentueelAanwezigProperty;

	// Methodes voor Enums te mappen
	@PostLoad
	public void fillTransient() {
		this.status = Status.of(statusValue);
	}

	@PrePersist
	public void fillPersistent() {
		if (status != null)
			this.statusValue = status.getStatus();
	}

	protected Gebruiker() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
		feedbackLijst = new ArrayList<Feedback>();
		aankondigingen = new ArrayList<Aankondiging>();
	}

	/**
	 * 
	 * @param voornaam
	 * @param familienaam
	 * @param mailadres
	 * @param gebruikersnaam
	 * @param profielfoto
	 * @param wachtwoord
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public Gebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam, TypeGebruiker type,
			Status status, String profielfoto) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this();

		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setGebruikersnaam(gebruikersnaam);
		setProfielfoto(profielfoto);
		this.typeGebruiker = type;

		this.normalizedUserName = gebruikersnaam.toUpperCase();
		this.normalizedEmail = mailadres.toUpperCase();
		this.emailConfirmed = true;
		String wachtwoord = "123";
		setPasswordHash(wachtwoord);
		this.securityStamp = UUID.randomUUID().toString();
		this.concurrencyStamp = UUID.randomUUID().toString();
		this.phoneNumber = null;

		this.phoneNumberConfirmed = false;
		this.twoFactorEnabled = false;
		this.lockoutEnd = null;
		this.lockoutEnabled = false;
		this.accessFailedCount = 0;

		setRandomGebruikerID();
		setPasswordHashJava(wachtwoord);
		setStringProperties();
	}

	public int geefAantalKeerAanwezig() {
		int ret = 0;

		for (GebruikerSessie gs : getGebruikerSessieLijst()) {
			if (gs.isAanwezig())
				ret++;
		}
		return ret;
	}

	public int geefProcentueelAanwezig() {
//		if (getGebruikerSessieLijst().size() == 0) {
//			return 0;
//		} else {
//			System.out.println(geefAantalKeerAanwezig() + " / " +  getGebruikerSessieLijst().size() + " = " + (geefAantalKeerAanwezig() / getGebruikerSessieLijst().size()) * 100);
//			return (geefAantalKeerAanwezig() / getGebruikerSessieLijst().size()) * 100;
//		}
		if (geefAantalKeerAanwezig() == 0) {
			return 0;
		} else if (getAantalKeerAfwezig() == 0) {
			return 100;
		} else {
			double ret = (((double) geefAantalKeerAanwezig()
					/ ((double) geefAantalKeerAanwezig() + (double) getAantalKeerAfwezig())) * 100);

			return (int) rondAf(ret, 0);
		}
	}

	private static double rondAf(double value, int places) {
		// Source
		// https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
		if (places < 0)
			throw new IllegalArgumentException("Er ging iets mis bij het afronden.");

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public SimpleStringProperty getAantalAanwezigProprty() {
		if (aantalAanwezigProprty == null) {
			setAantalAanwezigProprty();
		}
		return aantalAanwezigProprty;
	}

	public void setAantalAanwezigProprty() {
		if (aantalAanwezigProprty == null) {
			aantalAanwezigProprty = new SimpleStringProperty();
		}
		aantalAanwezigProprty.set(String.valueOf(geefAantalKeerAanwezig()));
	}

	public SimpleStringProperty getAantalAfwezigProperty() {
		if (aantalAfwezigProperty == null) {
			setAantalAfwezigProperty();
		}
		return aantalAfwezigProperty;
	}

	public void setAantalAfwezigProperty() {
		if (aantalAfwezigProperty == null) {
			aantalAfwezigProperty = new SimpleStringProperty();
		}
		aantalAfwezigProperty.set(String.valueOf(getAantalKeerAfwezig()));
	}

	public SimpleStringProperty getProcentueelAanwezigProperty() {
		if (procentueelAanwezigProperty == null) {
			setProcentueelAanwezigProperty();
		}
		return procentueelAanwezigProperty;
	}

	public void setProcentueelAanwezigProperty() {
		if (procentueelAanwezigProperty == null) {
			procentueelAanwezigProperty = new SimpleStringProperty();
		}
		procentueelAanwezigProperty.set(String.valueOf(geefProcentueelAanwezig() + " %"));
	}

	public SimpleStringProperty getStatusProperty() {
		if (statusProperty == null) {
			setStatusProperty();
		}
		return statusProperty;
	}

	public void setStatusProperty() {
		if (statusProperty == null) {
			statusProperty = new SimpleStringProperty();
		}
		statusProperty.set(getStatus().toString());
	}

	public StringProperty getVoorNaamProperty() {
		if (voorNaamProperty == null) {
			setVoorNaamProperty();
		}
		return voorNaamProperty;
	}

	private void setVoorNaamProperty() {
		if (voorNaamProperty == null) {
			voorNaamProperty = new SimpleStringProperty();
		}
		voorNaamProperty.set(getVoornaam());
	}

	public StringProperty getNaamProperty() {
		if (naamProperty == null) {
			setNaamProperty();
		}
		return naamProperty;
	}

	private void setNaamProperty() {
		if (naamProperty == null) {
			naamProperty = new SimpleStringProperty();
		}
		naamProperty.set(getFamilienaam());
	}

	public StringProperty getTypeProperty() {
		if (typeProperty == null) {
			setTypeProperty();
		}
		return typeProperty;
	}

	private void setTypeProperty() {
		if (typeProperty == null) {
			typeProperty = new SimpleStringProperty();
		}
		typeProperty.set(getType().toString());
	}

	public StringProperty getEmailProperty() {
		if (emailProperty == null) {
			setEmailProperty();
		}
		return emailProperty;
	}

	private void setEmailProperty() {
		if (emailProperty == null) {
			emailProperty = new SimpleStringProperty();
		}
		emailProperty.set(getMailadres());
	}

	public StringProperty getAantalFeedbacksProperty() {
		if (aantalFeedbacksProperty == null) {
			setAantalFeedbacksProperty();
		}

		return aantalFeedbacksProperty;
	}

	private void setAantalFeedbacksProperty() {
		if (aantalFeedbacksProperty == null) {
			aantalFeedbacksProperty = new SimpleStringProperty();
		}

		aantalFeedbacksProperty.set(String.valueOf(this.feedbackLijst.size()));
	}

	private void setStringProperties() {
		setNaamProperty();
		setVoorNaamProperty();
		setTypeProperty();
		setStatusProperty();
		setEmailProperty();
		setAantalFeedbacksProperty();
		setAantalAanwezigProprty();
		setAantalAfwezigProperty();
		setProcentueelAanwezigProperty();
	}

	public String getPasswordHashJava() {
		return passwordHashJava;
	}

	public void setPasswordHashJava(String passwordHashJava) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.passwordHashJava = PasswordHasher.getPasswordHash(passwordHashJava);
	}

	public List<GebruikerSessie> getGebruikerSessieLijst() {
		return gebruikerSessieLijst;
	}

	public void addGebruikerSessie(GebruikerSessie gebruikerSessie) {
		gebruikerSessieLijst.add(gebruikerSessie);
	}

	public void verwijderGebruikerSessie(GebruikerSessie gebruikerSessie) {
		gebruikerSessieLijst.remove(gebruikerSessie);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public TypeGebruiker getType() {
		return typeGebruiker;
	}

	public void setType(TypeGebruiker type) {
		this.typeGebruiker = type;
	}

	public String getGebruikerID() {
		return gebruikerID;
	}

	public void setRandomGebruikerID() {
		if (gebruikerID == null)
			this.gebruikerID = UUID.randomUUID().toString();
	}

	public String getVoornaam() {
		return voornaam;
	}

	private void setVoornaam(String voornaam) {
		if (voornaam == null || voornaam.isBlank())
			throw new NullPointerException("Voornaam mag niet leeg zijn!");
		if (!voornaam.matches("^([A-Za-z�-�\\\\'`��\\- ])+$"))
			throw new IllegalArgumentException("Voornaam is ongeldig");
		this.voornaam = voornaam;
	}

	public String getFamilienaam() {
		return familienaam;
	}

	private void setFamilienaam(String familienaam) {
		if (familienaam == null || familienaam.isBlank())
			throw new NullPointerException("familienaam mag niet leeg zijn!");
		if (!familienaam.matches("^([A-Za-z�-�\\\\'`��\\- ])+$"))
			throw new IllegalArgumentException("familienaam is ongeldig");
		this.familienaam = familienaam;
	}

	public String getMailadres() {
		return mailadres;
	}

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

	private void setGebruikersnaam(String gebruikersnaam) {
		if (gebruikersnaam == null || gebruikersnaam.isBlank())
			throw new NullPointerException("Gebruikersnaam mag niet leeg zijn!");
		if (!gebruikersnaam.matches("[0-9]{6}[a-z]{2}"))
			throw new IllegalArgumentException("Gebruikersnaam moet 6 cijfers gevolgd door 2 initialen");
		if (this.gebruikersnaam != null)
			throw new IllegalArgumentException("Gebruikersnaam kan niet worden gewijzigd!");
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getProfielfoto() {
		return profielFoto;
	}

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
	public void wijzigGebruiker(String voornaam, String familienaam, String mailadres, TypeGebruiker type,
			Status status, String profielfoto) {
		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setProfielfoto(profielfoto);

		setStringProperties();
	}

	public int getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(int statusValue) {
		this.statusValue = statusValue;
	}

	public String getProfielFoto() {
		return profielFoto;
	}

	public void setProfielFoto(String profielFoto) {
		this.profielFoto = profielFoto;
	}

	public String getNormalizedUserName() {
		return normalizedUserName;
	}

	public void setNormalizedUserName(String normalizedUserName) {
		this.normalizedUserName = normalizedUserName;
	}

	public String getNormalizedEmail() {
		return normalizedEmail;
	}

	public void setNormalizedEmail(String normalizedEmail) {
		this.normalizedEmail = normalizedEmail;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.passwordHash = PasswordHasher.getPasswordHash(passwordHash);
	}

	public String getSecurityStamp() {
		return securityStamp;
	}

	public void setSecurityStamp(String securityStamp) {
		this.securityStamp = securityStamp;
	}

	public String getConcurrencyStamp() {
		return concurrencyStamp;
	}

	public void setConcurrencyStamp(String concurrencyStamp) {
		this.concurrencyStamp = concurrencyStamp;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isPhoneNumberConfirmed() {
		return phoneNumberConfirmed;
	}

	public void setPhoneNumberConfirmed(boolean phoneNumberConfirmed) {
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}

	public boolean isTwoFactorEnabled() {
		return twoFactorEnabled;
	}

	public void setTwoFactorEnabled(boolean twoFactorEnabled) {
		this.twoFactorEnabled = twoFactorEnabled;
	}

	public LocalDateTime getLockoutEnd() {
		return lockoutEnd;
	}

	public void setLockoutEnd(LocalDateTime lockoutEnd) {
		this.lockoutEnd = lockoutEnd;
	}

	public boolean isLockoutEnabled() {
		return lockoutEnabled;
	}

	public void setLockoutEnabled(boolean lockoutEnabled) {
		this.lockoutEnabled = lockoutEnabled;
	}

	public int getAccessFailedCount() {
		return accessFailedCount;
	}

	public void setAccessFailedCount(int accessFailedCount) {
		this.accessFailedCount = accessFailedCount;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getAantalKeerAfwezig() {
		return aantalKeerAfwezig;
	}

	public void setAantalKeerAfwezig(int aantalKeerAfwezig) {
		this.aantalKeerAfwezig = aantalKeerAfwezig;
	}

	public void setGebruikerID(String gebruikerID) {
		this.gebruikerID = gebruikerID;
	}

	@Override
	public String toString() {
		return gebruikersnaam + ", " + voornaam + ", " + familienaam + ", " + mailadres + ", " + typeGebruiker + ", "
				+ status;
	}

	public void addFeedback(Feedback feedback) {
		feedbackLijst.add(feedback);
	}
}