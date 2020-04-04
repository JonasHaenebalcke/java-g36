package domein;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Gebruiker {

	// Geen idee hoe enum werkt (als niemand het weet kijk ik er nog eens naar)
	private Status status;
	private Type type;
	private SimpleStringProperty gebruikerID = new SimpleStringProperty();
	private SimpleStringProperty voornaam = new SimpleStringProperty();
	private SimpleStringProperty familienaam = new SimpleStringProperty();
	private SimpleStringProperty mailadres = new SimpleStringProperty();
	private SimpleStringProperty gebruikersnaam = new SimpleStringProperty();
	private SimpleStringProperty profielfoto = new SimpleStringProperty();

	public Gebruiker() {
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

	public Status getStatus() {
		return status;
	}

	private void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	private void setType(Type type) {
		this.type = type;
	}

	public String getGebruikerID() {
		return gebruikerID.get();
	}

	public StringProperty getGebruikerIDProperty() {
		return gebruikerID;
	}
//	Hoort niet te veranderen?
//	private void setGebruikerID(String gebruikerID) {
//		this.gebruikerID = gebruikerID;
//	}

	public String getVoornaam() {
		return voornaam.get();
	}

	public StringProperty getVoornaamProperty() {
		return voornaam;
	}

	private void setVoornaam(String voornaam) {
		if (voornaam == null || voornaam.isBlank())
			throw new NullPointerException("Voornaam mag niet leeg zijn!");
		if (!voornaam.matches("^([A-Za-zÀ-ÿ\\\\'`´’\\- ])+$"))
			throw new IllegalArgumentException("Voornaam is ongeldig");
		this.voornaam.set(voornaam);
	}

	public String getFamilienaam() {
		return familienaam.get();
	}

	public StringProperty getFamilienaamProperty() {
		return familienaam;
	}

	private void setFamilienaam(String familienaam) {
		if (familienaam == null || familienaam.isBlank())
			throw new NullPointerException("familienaam mag niet leeg zijn!");
		if (!familienaam.matches("^([A-Za-zÀ-ÿ\\\\'`´’\\- ])+$"))
			throw new IllegalArgumentException("familienaam is ongeldig");
		this.familienaam.set(familienaam);
	}

	public String getMailadres() {
		return mailadres.get();
	}

	public StringProperty getMailadresProperty() {
		return mailadres;
	}

	private void setMailadres(String mailadres) {
		if (mailadres == null || mailadres.isBlank())
			throw new NullPointerException("Mailadres mag niet leeg zijn!");
		if (!mailadres.matches(
				"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))
			throw new IllegalArgumentException("Email moet geldig zijn!");

		this.mailadres.set(mailadres);
	}

	public String getGebruikersnaam() {
		return gebruikersnaam.get();
	}

	public StringProperty getGebruikersnaamProperty() {
		return gebruikersnaam;
	}

	private void setGebruikersnaam(String gebruikersnaam) {

		if (gebruikersnaam == null || gebruikersnaam.isEmpty())
			throw new NullPointerException("Gebruikersnaam mag niet leeg zijn!");
		if (!gebruikersnaam.matches("[0-9]{6}[a-z]{2}"))
			throw new IllegalArgumentException("Gebruikersnaam moet 6 cijfers gevolgd door 2 initialen");
		this.gebruikersnaam.set(gebruikersnaam);
	}

	public String getProfielfoto() {
		return profielfoto.get();
	}

	public StringProperty getProfielfotoProperty() {
		return profielfoto;
	}

	private void setProfielfoto(String profielfoto) {
		this.profielfoto.set(profielfoto);
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
		// Kan dit status zijn of moet ik string gebruiken, weet niet hoe dit van
		// toepassing is op javaFX
		setStatus(status);
		setType(type);
		setVoornaam(voornaam);
		setFamilienaam(familienaam);
		setMailadres(mailadres);
		setGebruikersnaam(gebruikersnaam);
		setProfielfoto(profielfoto);

	}

}