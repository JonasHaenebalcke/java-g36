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
	@Column(name = "Type")
	private String typeDb;

	@Transient
	private List<GebruikerSessie> gebruikerSessieLijst;

	// Methodes voor Enums te mappen
	@PostLoad
	public void fillTransient() {
		this.status = Status.of(statusValue);

		if (this.typeGebruiker == null) {
			this.typeGebruiker = typeGebruiker.Gebruiker;
		}
//		try {
//			if (this.typeGebruiker.toString().equalsIgnoreCase("Verantwoordelijke")) {
//				this.typeGebruiker = typeGebruiker.Hoofdverantwoordelijke;
//			} else {
//				this.typeGebruiker = typeGebruiker.Verantwoordelijke;
//			}
//		} catch (NullPointerException e) {
//			this.typeGebruiker = typeGebruiker.Gebruiker;
//		}

	}

	@PrePersist
	public void fillPersistent() {
		if (status != null)
			this.statusValue = status.getStatus();

		if (this.typeGebruiker == typeGebruiker.Gebruiker)
			this.typeGebruiker = null;
	}

	protected Gebruiker() {
		gebruikerSessieLijst = new ArrayList<GebruikerSessie>();
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
			Status status, String profielfoto, String wachtwoord)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		this();

		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setGebruikersnaam(gebruikersnaam);
		setProfielfoto(profielfoto);

		if (type.equals(TypeGebruiker.Hoofdverantwoordelijke)) {
			this.typeDb = TypeGebruiker.Verantwoordelijke.toString();
		} else {
			this.typeDb = type.toString();
		}

		this.normalizedUserName = gebruikersnaam.toUpperCase();
		this.normalizedEmail = mailadres.toUpperCase();
		this.emailConfirmed = true;
		setPasswordHash(wachtwoord);
		this.securityStamp = UUID.randomUUID().toString();
		this.concurrencyStamp = UUID.randomUUID().toString();
		this.phoneNumber = null;

		this.phoneNumberConfirmed = false;
		this.twoFactorEnabled = false;
		this.lockoutEnd = null;
		this.lockoutEnabled = false;
		this.accessFailedCount = 0;
		this.barcode = barcode;
		this.aantalKeerAfwezig = aantalKeerAfwezig;

		setRandomGebruikerID();
		setPasswordHashJava(wachtwoord);
	}
	
	public String getPasswordHashJava() {
        return passwordHashJava;
    }

    public void setPasswordHashJava(String passwordHashJava) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.passwordHashJava = PasswordHasher.getPasswordHash(passwordHashJava);
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

//	public StringProperty getGebruikerIDProperty() {
//		return gebruikerID;
//	}

	public void setRandomGebruikerID() {
		if (gebruikerID == null)
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
	public void wijzigGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam,
			TypeGebruiker type, Status status, String profielfoto) {
		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setGebruikersnaam(gebruikersnaam);
		setProfielfoto(profielfoto);
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

	public String getTypeDb() {
		return typeDb;
	}

	public void setTypeDb(String typeDb) {
		this.typeDb = typeDb;
	}

	public void setGebruikerID(String gebruikerID) {
		this.gebruikerID = gebruikerID;
	}

	@Override
	public String toString() {
		return gebruikersnaam + ", " + voornaam + ", " + familienaam + ", " + mailadres + ", " + typeGebruiker + ", "
				+ status;

		/*
		 * return "Gebruiker [status=" + status + ", type=" + typeGebruiker +
		 * ", gebruikerID=" + gebruikerID + ", gebruikersnaam=" + gebruikersnaam +
		 * ", voornaam=" + voornaam + ", familienaam=" + familienaam + ", mailadres=" +
		 * mailadres + ", profielFoto=" + profielFoto + "]";
		 */
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